import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EasyParser {
    private FileWriter writeThread;
    private FileReader readThread;
    private BufferedReader scan;
    private String filePath;
    private Pattern startTamplate = Pattern.compile("<\\w+(\\s+|>)", Pattern.CASE_INSENSITIVE);
    private Pattern endTamplate = Pattern.compile("\\<\\/\\w+\\>", Pattern.CASE_INSENSITIVE);
    private Pattern attrTamplate = Pattern.compile("\\w+\\s*=\\s*\\\"\\w+\\\"", Pattern.CASE_INSENSITIVE);
    private Pattern tagValueTamplate = Pattern.compile("(>\\s*[^<>]+\\s*<|>\\s*[^<>]+\\s*\n|[^<>]\\s*<)", Pattern.CASE_INSENSITIVE);
    public void startTag(String tag) {
    };

    public void endTag(String tag) {
    };

    public void tagValue(String value) {
    };

    public void attribute(String attrName, String attrValue) {
    };
    public void attribute(String tagName, String attrName, String attrValue){
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
        Matcher tagValue;
        String currentLine;
        while (scan.ready()) {
            currentLine = scan.readLine();
            matcher = startTamplate.matcher(currentLine);
            attrFinder = attrTamplate.matcher(currentLine);
            endTagMatcher = endTamplate.matcher(currentLine);
            tagValue=tagValueTamplate.matcher(currentLine);
            while (matcher.find()) {
                startTag(matcher.group().replaceAll("<|>|/", ""));
                while (attrFinder.find()) {
                    String[] nameAndValue = attrFinder.group().split("=");
                    attribute(matcher.group().replaceAll("<|>|\\s", ""), nameAndValue[0], nameAndValue[1].replaceAll("\\\"", ""));
                }
            }
            while (tagValue.find()){
                tagValue(tagValue.group().replaceAll("\\>+|\\<+",""));
                break;
            }
            while (endTagMatcher.find()) {
                endTag(endTagMatcher.group().replaceAll("<|>|/", ""));
                break;
            }
        }

    }


    public static void main(String[] args) throws Exception {
        System.setProperty("console.encoding","Cp866");
        EasyParser parser = new EasyParser("src/test.xml") {
            public void startTag(String tag) {
                System.out.println("Open tag: " + tag);
            }

            public void attribute(String tagName, String attrName, String attrValue) {
                System.out.println("Tag name: "+tagName+" attr name: " + attrName + " Value: " + attrValue);
            }

            public void tagValue(String value){
                System.out.println("Tag value: "+value);
            }
            public void endTag(String tag) {
                System.out.println("Close tag: " + tag);
            }
        };
        parser.startPars();

    }

}
