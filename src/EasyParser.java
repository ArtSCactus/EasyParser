import java.io.*;
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
    this.filePath=filePath;
    writeThread= new FileWriter(filePath);
    readThread = new FileReader(filePath);
    scan = new BufferedReader(readThread);
}
    public static void main(String[] args) throws Exception {

    }

}
