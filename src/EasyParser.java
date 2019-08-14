package testasserver.Parser;


import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EasyParser {

    private FileWriter writeThread;
    private FileReader readThread;
    private BufferedReader scan;
    /**
     * Consist path to file, that will be parsing.
     *
     */
    private String filePath;
    /**
     * Pattern to find rows like <openTag>.
     *
     */
    private Pattern startTamplate = Pattern.compile("<\\?{0,0}\\w+(\\s*|\\>?)", Pattern.CASE_INSENSITIVE) /**
     * Pattern to find rows like <</closeTag>.
     *
     */
            ;
    private Pattern closeTamplate = Pattern.compile("\\<?(\\/\\w+|\\w+\\/)\\>?", Pattern.CASE_INSENSITIVE) /**
     * Pattern to find rows like attr="attrValue".
     *
     */
            ;
    private Pattern attrTamplate = Pattern.compile("(\\w+|\\W+)\\s*=\\s*\".+?\"", Pattern.CASE_INSENSITIVE) /**
     * Pattern to find value of tags.
     *
     */
            ;
    private Pattern tagValueTamplate = Pattern.compile("\\>?.+?<", Pattern.CASE_INSENSITIVE);

    /**
     * Method, that will be overrided. On this method will be comming
     * information from parser.
     *
     * @param tag
     */
    public void startTag(String tag) {
    }

    /**
     * Method, that will be overrided. On this method will be comming
     * information from parser.
     *
     * @param tag
     */
    public void endTag(String tag) {
    }

    /**
     * Method, that will be overrided. On this method will be comming
     * information from parser.
     *
     * @param value
     */
    public void tagValue(String value) {
    }

    /**
     * Method, that will be overrided. On this method will be comming
     * information from parser.
     *
     * @param attrName
     * @param attrValue
     */
    public void attribute(String attrName, String attrValue) {
    }

    /**
     * Method, that weill be overrided. On this method will be comming
     * information from parser.
     *
     * @param tagName
     * @param attrName
     * @param attrValue
     */
    public void attribute(String tagName, String attrName, String attrValue) {
    }

    /**
     * Constructs new parser with file placed by path.
     *
     * @param filePath
     * @throws IOException
     * @throws NullPointerException if filePath are null
     */
    public EasyParser(String filePath) throws IOException {
        if (filePath == null) {
            throw new NullPointerException("File path cannot be null");
        }
        this.filePath = filePath;
        readThread = new FileReader(filePath);
        scan = new BufferedReader(readThread);
    }

    /**
     * Constructs the parser with file as object of File.
     *
     * @param file
     * @throws IOException
     */
    public EasyParser(File file) throws IOException {
        if (filePath == null) {
            throw new NullPointerException("File cannot be null");
        }
        filePath = file.toString();
        readThread = new FileReader(file);
        scan = new BufferedReader(readThread);
    }

    /**
     * Prints to console where matching was founded.
     *
     * @param matcher
     */
    private void devInfo(Matcher matcher) {
        int start = matcher.start();
        int end = matcher.end();
        System.out.println("Match found " + matcher.group() + " " + " from " + start + " to " + (end - 1) + " position");
    }

    /**
     * Main parser function. Parser are going by each row in a file and
     * searching there tags.If it will found tag or tag value, it will be sended
     * to the dedicated methods.
     * <code>startTag()</code>
     * <code>endTag()</code>
     * <code>attribute()</code>
     * <code>tagValue()</code>
     * All this methods MUST be overrided while parser initializing.
     *
     *
     * @throws IOException
     */
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
                    } else if (!tag.isEmpty() & !tag.contains("<?") & !tag.contains("<!")) {
                        tagValue(tag);
                    }
                }
            }
        }
        readThread.close();
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
