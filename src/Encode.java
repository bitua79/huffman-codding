import java.io.*;
import java.util.*;

public class Encode {
    public static int length;
    public static String output ="";
    public static String header ="";
    public static ArrayList<HuffmanNode> codeBook = new ArrayList<>();
    public static HashMap<Character, String> dictionary = new HashMap<>();

    public static void main(String[] args) {
        String input = readFile();
        char[] chars = input.toCharArray();
        sort(chars);

        PriorityQueue<Node> pq = new PriorityQueue<>(new CompareCount());

        //count the count of each character
        counter(chars, pq);

        //struct the huffman tree and returns its root
        Node root = huffmanTree(pq);

        //struct huffman code of each character and put them in codeBook
        huffmanCoding(root, "");
        //sort nodes by their length
        sort(codeBook);

        //struct canonical code of each character and put them in dictionary
        canonicalCoding(codeBook, dictionary);

        //write the header to file
        header+= length +"\n";
        for (int i = 0; i< length; i++){
            header+=codeBook.get(i).data+" "+codeBook.get(i).canonicalCode+"\n";
        }

        //write encoded form of input
        for (int i=0; i<chars.length;i++){
            System.out.print(input.charAt(i));
            output += dictionary.get(input.charAt(i));
        }
        ArrayList<Byte> bytes = new ArrayList<>();
        byte[] hBytes = header.getBytes();
        for (byte hByte : hBytes) {
            bytes.add(hByte);
        }

        File header = new File("src/header.txt");
        writeFile(header, bytes);

        bytes = new ArrayList<>();
        File encodedF = new File("src/encoded.dat");
        writeFile(encodedF, stringToBytes(bytes, output));
    }

    //read the file and return it as a string
    public static String readFile(){
        File file = new File("src/inputString.txt");
        String s = "";
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine())s+=(scanner.nextLine())+"\n";
            s = s.substring(0, s.length()-1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return s;
    }

    //convert the string to bytes
    public static ArrayList<Byte> stringToBytes(ArrayList<Byte> bytes, String s){
        int i=0;

        if (s.length()%8!=0) s = addZero(s, s.length()%8);
        while (s.length() > 8){
            bytes.add((byte) Integer.parseInt(s.substring(i, i+8), 2));
            s = s.substring(i+9);
        }

        return bytes;
    }

    //write byte array to a file
    public static void writeFile(File file, ArrayList<Byte> bytes){
        byte[] bytesArr = new byte[bytes.size()];
        for (int i=0; i<bytes.size(); i++)bytesArr[i] = bytes.get(i);
        try (
                OutputStream outputStream = new FileOutputStream(file);
        ) {
            outputStream.write(bytesArr);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //count the count of each character
    public static void counter(char[] chars, PriorityQueue<Node> priorityQueue){
        int pointer = 0;
        int count = 1;
        while (pointer < chars.length-1){
            if (chars[pointer] == chars[pointer+1]){
                count++;
            }else {
                priorityQueue.add(new Node(chars[pointer], count));
                count = 1;
            }
            pointer++;
            if (pointer == chars.length-1){
                priorityQueue.add(new Node(chars[pointer], count));
                count = 0;
            }
        }
        length = priorityQueue.size();
    }

    //struct the huffman tree and returns its root
    public static Node huffmanTree(PriorityQueue<Node> priorityQueue){

        Node root = null;
        while (priorityQueue.size() > 1){
            Node first = priorityQueue.remove();
            Node second = priorityQueue.remove();
            int sum = first.freq+second.freq;
            Node newNode = new Node(sum);

//            System.out.println(first.data+" "+second.data+"  "+ newNode.freq);
            priorityQueue.add(newNode);
            newNode.left = first;
            newNode.right = second;
            root = newNode;
        }
        return root;
    }

    //struct huffman code of each character and put them in codeBook
    public static void huffmanCoding(Node root, String s){
        if (root.right == null && root.left == null) {
//            System.out.println(root.data + ": " + s);
            codeBook.add(new HuffmanNode(root.data, s));
        }
        else {
            if (root.left != null) huffmanCoding(root.left, s+"0");
            if (root.right != null) huffmanCoding(root.right, s+"1");
        }
    }

    //struct canonical code of each character and put them in dictionary
    public static void canonicalCoding(ArrayList<HuffmanNode> nodes, HashMap<Character, String> map){
        int k = 0;

        String firstCode = addZero("", nodes.get(k).huffmanCode.length());
        nodes.get(k).canonicalCode = firstCode;
        map.put(nodes.get(k).data, firstCode);

        while (k < length -1){
            String temp = nodes.get(k).canonicalCode;
            temp = sum(Integer.parseInt(temp), 1, temp.length())+"";

            if (nodes.get(k).len < nodes.get(k+1).len) {
                int difference = nodes.get(k + 1).len - nodes.get(k).len;
                temp = addZero(temp, difference);
            }

            nodes.get(k + 1).canonicalCode = temp;
            map.put(nodes.get(k + 1).data, temp);
            k++;
        }
    }

    public static String addZero(String s, int count){
        for (int i=0; i<count; i++){
            s = s+"0";
        }
        return s;
    }

    //summarize two binary number
    public static String sum(int b1, int b2, int len){
        int i = 0, carry = 0;
        int[] sum = new int[10];
        String out ="";

        while (b1 != 0 || b2 != 0)
        {
            sum[i++] = (b1 % 10 + b2 % 10 + carry) % 2;
            carry = (b1 % 10 + b2 % 10 + carry) / 2;
            b1 = b1 / 10;
            b2 = b2 / 10;
        }
        if (carry != 0) {
            sum[i++] = carry;
        }
        --i;
        while (i >= 0) {
            out+=sum[i--];
        }
        if (len > out.length()){
            for (int j=0; j<len - out.length(); j++){
                out = "0" + out;
            }
        }
        return out;
    }

    public static void sort(char[] chars){
        for (int i=0; i<chars.length-1; i++){
            if (chars[i]>chars[i+1]){
                swap(i, chars);
                i = -1;
            }
        }
    }

    public static void swap(int i, char[] chars){
        char temp = chars[i];
        chars[i] = chars[i+1];
        chars[i+1] = temp;
    }

    public static void sort(ArrayList<HuffmanNode> arrayList){
        for (int i = 0; i< arrayList.size() -1; i++){
            if (arrayList.get(i).len>arrayList.get(i+1).len){
                swap(i, arrayList);
                i = -1;
            }
        }
    }

    public static void swap(int i, ArrayList<HuffmanNode> arrayList){
        HuffmanNode temp = arrayList.get(i);
        arrayList.set(i, arrayList.get(i+1));
        arrayList.set(i+1, temp);
    }

}
class Node {
    int freq;
    char data;

    Node left;
    Node right;

    public Node(char data, int freq) {
        this.freq = freq;
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public Node(int freq) {
        this.freq = freq;
        this.data = '_';
    }

    @Override
    public String toString() {
        return this.data+"  "+this.freq;
    }
}

class HuffmanNode {
    char data;
    String huffmanCode;
    String canonicalCode;
    int len;

    public HuffmanNode(char data, String huffmanCode) {
        this.data = data;
        this.huffmanCode = huffmanCode;
        this.len = huffmanCode.length();
    }

    @Override
    public String toString() {
        return data +" "+huffmanCode+" "+len+" "+canonicalCode+"\n";
    }
}

class CompareCount implements Comparator<Node> {
    public int compare(Node a, Node b)
    {

        if (a.freq == b.freq) return b.data - a.data;

        return a.freq - b.freq;
    }
}