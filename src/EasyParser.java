import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EasyParser {
    private FileWriter writeThread;
    private FileReader readThread;
    private BufferedReader scan;
    private String filePath;
    private Pattern startTamplate = Pattern.compile("\\<\\w+\\>", Pattern.CASE_INSENSITIVE);
    private Pattern endTamplate = Pattern.compile("\\<\\/\\w+\\>", Pattern.CASE_INSENSITIVE);
    public void startTag(String tag){};
    public void endTag(String tag){};
    public void value(String value){};

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

    public void startPars() throws IOException {
        List<String> rows = new ArrayList<String>();
        Matcher  matcher;
     while (scan.ready()) {
         matcher = startTamplate.matcher(scan.readLine());
         while (matcher.find()) {
             rows.add(matcher.group());
             int start = matcher.start();
             int end = matcher.end();
             System.out.println("Найдено совпадение " +matcher.group()+" " + " с " + start + " по " + (end - 1) + " позицию");
             startTag(matcher.group());
         }
         matcher = endTamplate.matcher(scan.readLine());
         while (matcher.find()) {
             rows.add(matcher.group());
             int start = matcher.start();
             int end = matcher.end();
             System.out.println("Найдено совпадение " +matcher.group()+" " + " с " + start + " по " + (end - 1) + " позицию");
             endTag(matcher.group());
         }

     }
       }

    public static void main(String[] args) throws Exception {
        EasyParser parser = new EasyParser("src/test.txt"){
            public void startTag(String tag){
                System.out.println("Open tag: "+tag);
            }
            public void endTag(String tag){
                System.out.println("Close tag: "+tag);
            }
        };
        parser.startPars();

    }

}
