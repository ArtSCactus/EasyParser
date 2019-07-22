import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EasyParser {
    private FileWriter writeThread;
    private FileReader readThread;
    private BufferedReader scan;
    private String filePath;
    private Pattern startTamplate = Pattern.compile("<\\w+[\\s+|>]", Pattern.CASE_INSENSITIVE);
    private Pattern endTamplate = Pattern.compile("\\<\\/\\w+\\>", Pattern.CASE_INSENSITIVE);
    private Pattern attrTamplate = Pattern.compile("\\w+\\=\\\"\\w+\\\"", Pattern.CASE_INSENSITIVE);

    public void startTag(String tag) {
    };

    public void endTag(String tag) {
    };

    public void value(String value) {
    };

    public void attribute(String attrName, String attrValue) {
    };

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
        String currentLine;
        while (scan.ready()) {
            currentLine = scan.readLine();
            matcher = startTamplate.matcher(currentLine);
            attrFinder = attrTamplate.matcher(currentLine);
            endTagMatcher = endTamplate.matcher(currentLine);
            while (matcher.find()) {
                devInfo(matcher);
                startTag(matcher.group().replaceAll("<|>|/", ""));

                while (attrFinder.find()) {
                    devInfo(attrFinder);
                    String[] nameAndValue = attrFinder.group().split("=");
                    attribute(nameAndValue[0], nameAndValue[1].replaceAll("\\\"", ""));
                }
            }
            while (endTagMatcher.find()) {
                endTag(endTagMatcher.group().replaceAll("<|>|/", ""));
            }
        }

    }


    public static void main(String[] args) throws Exception {
        EasyParser parser = new EasyParser("src/test.xml") {
            public void startTag(String tag) {
                System.out.println("Open tag: " + tag);
            }

            public void attribute(String attrName, String attrValue) {
                System.out.println("Attribute name: " + attrName + " Value: " + attrValue);
            }

            public void endTag(String tag) {
                System.out.println("Close tag: " + tag);
            }
        };
        parser.startPars();

    }

}
