import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EasyParser {
    private FileWriter writeThread;
    private FileReader readThread;
    private BufferedReader scan;
    private String filePath;
    private Pattern startTamplate = Pattern.compile("<\\?{0,0}\\w+(\\s*|\\>?)", Pattern.CASE_INSENSITIVE);
    private Pattern closeTamplate = Pattern.compile("\\<?(\\/\\w+|\\w+\\/)\\>?", Pattern.CASE_INSENSITIVE);
    private Pattern attrTamplate = Pattern.compile("(\\w+|\\W+)\\s*=\\s*\".+?\"", Pattern.CASE_INSENSITIVE);
    private Pattern tagValueTamplate = Pattern.compile("\\>?.+?<", Pattern.CASE_INSENSITIVE);

    public void startTag(String tag) {
    }

    public void endTag(String tag) {
    }

    public void tagValue(String value) {
    }

    public void attribute(String attrName, String attrValue) {
    }

    public void attribute(String tagName, String attrName, String attrValue) {
    }

    public EasyParser(String filePath) throws IOException {
        this.filePath = filePath;
        readThread = new FileReader(filePath);
        scan = new BufferedReader(readThread);
    }

    public EasyParser(File file) throws IOException {
        filePath = file.toString();
        readThread = new FileReader(file);
        scan = new BufferedReader(readThread);
    }

    private void devInfo(Matcher matcher) {
        int start = matcher.start();
        int end = matcher.end();
        System.out.println("Match found " + matcher.group() + " " + " from " + start + " to " + (end - 1) + " position");
    }

    public void startPars() throws IOException {
        Matcher matcher;
        Matcher attrFinder;
        Matcher endTagMatcher;
        Matcher tagValue;
        String currentLine;
        while (scan.ready()) {
            currentLine = scan.readLine();
            String[] tags = currentLine.split(">");
            for (String tag : tags) {
                matcher = startTamplate.matcher(tag);
                attrFinder = attrTamplate.matcher(tag);
                endTagMatcher = closeTamplate.matcher(tag);
                tagValue = tagValueTamplate.matcher(tag);
                if (matcher.find()) {
                    startTag(matcher.group().replaceAll("<|>", ""));
                    while (attrFinder.find()) {
                        String[] nameAndValue = attrFinder.group().split("=");
                        attribute(matcher.group().replaceAll("<|>|\\s", ""), nameAndValue[0], nameAndValue[1].replaceAll("\\\"", ""));
                    }
                } else {
                    if (endTagMatcher.find()) {
                        if (tagValue.find()) {
                            tagValue(tagValue.group().replaceAll("<|>|/", ""));
                        }
                        endTag(endTagMatcher.group().replaceAll("<|>|/", ""));
                    } else if (!tag.isEmpty() & !tag.contains("<?")) tagValue(tag);
                }
            }
        }
    }


    public static void main(String[] args) throws Exception {
        System.setProperty("console.encoding", "Cp866");
        EasyParser parser = new EasyParser("src/test.xml") {
            public void startTag(String tag) {
                System.out.println("Open tag: " + tag);
            }

            public void attribute(String tagName, String attrName, String attrValue) {
                System.out.println("Tag name: " + tagName + " attr name: " + attrName + " Value: " + attrValue);
            }

            public void tagValue(String value) {
                System.out.println("Tag value: " + value);
            }

            public void endTag(String tag) {
                System.out.println("Close tag: " + tag);
            }
        };
        parser.startPars();

    }

}
