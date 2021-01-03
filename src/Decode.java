import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class Decode {
    static int length;
    static HashMap<String, Character> dictionary = new HashMap<>();
    public static void main(String[] args) {
        Scanner read = null;
        File header = new File("src/header.txt");
        try {
            read = new Scanner(header);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //extract codes from file
        length = read.nextInt();
        read.nextLine();
        for (int i=0; i<length; i++){
            String line = read.nextLine();
            char value;
            String key;
            if (line.length() == 0){
                value = '\n';
                line = read.nextLine();
                key = line.substring(1);
            }else {
                value = line.charAt(0);
                key = line.substring(2);
            }

            dictionary.put(key, value);
        }

        String string = "";
        try {
            File file = new File("src/encoded.dat");
            InputStream inputStream = new FileInputStream(file);
            byte byteRead;

            while ((byteRead = (byte) inputStream.read()) != -1) {
                System.out.println(byteRead);
                string += Integer.toBinaryString(byteRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        String output ="";
        String temp = "";

        for (int i=0; i<string.length(); i++){
            temp += string.charAt(i);
            if (dictionary.containsKey(temp)){
                output += dictionary.get(temp);
                temp="";
            }
        }
        if (!temp.isEmpty())output += dictionary.get(temp);

        writeFile(stringToBytes(output));
    }

    //convert the string to bytes
    public static byte[] stringToBytes(String s){
        byte[] bytes = new byte[s.length()];
        for (int i=0; i<s.length();i++){
            bytes[i] = (byte) s.charAt(i);
        }
        return bytes;
    }

    //write byte array to a file
    public static void writeFile(byte[] bytes){
        File file = new File("src/decoded.txt");
        try (
                OutputStream outputStream = new FileOutputStream(file);
        ) {
            outputStream.write(bytes);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
