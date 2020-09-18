package encryptdecrypt;

        import java.io.*;
        import java.nio.file.Files;
        import java.nio.file.Path;
        import java.nio.file.Paths;
        import java.util.ArrayList;

public class Main {

    private static final ArrayList<Character> lowerCaseSimbol = new ArrayList<>();
    private static final ArrayList<Character> upperCaseSimbol = new ArrayList<>();
    private static String operation = "enc";
    private static String str = "";
    private static int key = 0;
    private static String inFile = null;
    private static File outFile = null;
    private static String algorithm = "shift";
    private static String resultMessage = null;

    public static void main(String[] args) {

        getParametrs(args);
        initAlphabet();
        readFromFile();
        chooseAlgorithm();
        outputData();

    }

    private static void outputData() {
        if (outFile == null) {
            System.out.println(resultMessage);
        } else {
            writeDataInFile(resultMessage);
        }
    }

    private static void chooseAlgorithm() {
        if ("shift".equalsIgnoreCase(algorithm)) {
            if ("enc".equals(operation)) {
                resultMessage = shiftEncryption(str, key);
            } else if ("dec".equals(operation)) {
                resultMessage = shiftDecryption(str, -key);
            }
        } else {
            if ("enc".equals(operation)) {
                resultMessage = unicodeEncryption(str, key);
            } else if ("dec".equals(operation)) {
                resultMessage = unicodeDecryption(str, key);
            }
        }
    }

    private static void readFromFile() {
        if (str.equals("") && inFile != null) {
            try {
                str = readFileAsString(inFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    public static void writeDataInFile(String msg) {
        try (FileWriter writer = new FileWriter(outFile)) {
            outFile.createNewFile();
            writer.write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getParametrs(String[] args){
        for (int i = 0; i < args.length; i++) {
            if ("-mode".equals(args[i])) {
                operation = args[i + 1];
            } else if ("-key".equals(args[i])) {
                key = Integer.parseInt(args[i + 1]);
            } else if ("-data".equals(args[i])) {
                str = args[i + 1];
            } else if ("-out".equals(args[i])) {
                outFile = new File(args[i + 1]);
            } else if ("-in".equals(args[i])) {
                inFile = args[i + 1];
            } else if ("-alg".equals(args[i])) {
                algorithm = args[i + 1];
            }
        }
    }

    private static String shiftEncryption(String msg, int number) {
        StringBuilder sb = new StringBuilder();

        for (char ch: msg.toCharArray()) {
            ArrayList<Character> array = null;

            if (lowerCaseSimbol.contains(ch)) {
                array = lowerCaseSimbol;
            } else if (upperCaseSimbol.contains(ch)) {
                array = upperCaseSimbol;
            }

            if (array != null && array.contains(ch)) {
                int index = array.indexOf(ch) + number;

                if (index > array.size() - 1) {
                    index -= array.size();
                }
                ch = array.get(index);
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    private static String shiftDecryption(String msg, int number) {
        StringBuilder sb = new StringBuilder();

        for (char ch: msg.toCharArray()) {
            ArrayList<Character> array = null;

            if (lowerCaseSimbol.contains(ch)) {
                array = lowerCaseSimbol;
            } else if (upperCaseSimbol.contains(ch)) {
                array = upperCaseSimbol;
            }

            if (array != null && array.contains(ch)) {
                int index = array.indexOf(ch) + number;

                if (index < 0) {
                    index += array.size();
                }
                ch = array.get(index);

            }
            sb.append(ch);
        }
        return sb.toString();
    }

    private static String unicodeDecryption(String msg, int number) {
        return decryptEncryptOperation(msg, -number);
    }

    private static String unicodeEncryption(String msg, int number) {
        return decryptEncryptOperation(msg, number);
    }

    private static String decryptEncryptOperation (String msg, int number) {
        StringBuilder sb = new StringBuilder();

        for (char ch: msg.toCharArray()) {
            ch += number;
            sb.append(ch);
        }
        return sb.toString();
    }

    private static void initAlphabet() {
        for (char i = 'a'; i <= 'z'; i++) {
            lowerCaseSimbol.add(i);
        }
        for(char i = 'A'; i <= 'Z'; i++) {
            upperCaseSimbol.add(i);
        }
    }
}

