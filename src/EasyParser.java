import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EasyParser {
    private FileWriter writeThread;
    private FileReader readThread;
    private BufferedReader scan;
    private String filePath;

    public EasyParser(String filePath) throws IOException {
        this.filePath = filePath;
        readThread = new FileReader(filePath);
        scan = new BufferedReader(readThread);
    }

    public EasyParser (File file) throws IOException{
        filePath=file.toString();
        readThread= new FileReader(file);
        scan = new BufferedReader(readThread);
    }

    public List<String> getRowsInList() throws IOException {
        List<String> rows = new ArrayList<String>();
        while (scan.ready())
               rows.add(scan.readLine());
               return rows;
    }
    public static void main(String[] args) throws Exception {
         EasyParser parser = new EasyParser("src/test.txt");
         List<String> rows=parser.getRowsInList();
         System.out.println(rows.toString());

    }

}
