import java.io.*;
import java.util.*;

public class ReadInput {
    static int N, M, P;
    static String S;
    static List<char[][]> blocks = new ArrayList<>();

    // Membaca file dan memparsing input
    static boolean readFile(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Membaca N, M, P
            String[] firstLine = br.readLine().split(" ");
            // Validasi inputan N, M dan P (Harus berbentuk integer)
            try {
                N = Integer.parseInt(firstLine[0]);
                M = Integer.parseInt(firstLine[1]);
                P = Integer.parseInt(firstLine[2]); 
            } catch (NumberFormatException e) {
                System.out.println("Input N, M, atau P tidak valid.");
                return false;
            }

            // Membaca Jenis Kasus
            S = br.readLine().trim();

            // Membaca block
            List<String> blockLines = new ArrayList<>();
            char prev = '0'; 

            String L;
            while ((L = br.readLine()) != null) {
                // System.out.println(L); // Debugging
                if (L.trim().isEmpty()) {
                    continue;
                }

                // Melakukan validasi inputan blok apakah semuanya alfabet atau spasi
                if (!L.matches("[A-Z ]+")) {
                    System.out.println("Inputan block ada yang tidak valid");
                    return false;
                }

                // Menentukan alfabet pertama
                String charWithoutSpace = L.trim();
                char firstChar = charWithoutSpace.charAt(0);

                // Jika blockLines tidak kosong dan alfabet berubah, blok sebelumnya uda kelar
                if (!blockLines.isEmpty() && firstChar != prev) {
                    char[][] block = convertToBlock(blockLines);
                    if (block == null) {
                        System.out.println("Blok tidak valid");
                        return false;
                    }
                    blocks.add(block);
                    blockLines.clear();
                }

                blockLines.add(L); 
                prev = firstChar;
            }

            // Jika sudah sampai di blok terakhir
            if (!blockLines.isEmpty()) {
                char[][] block = convertToBlock(blockLines);
                if (block == null) {
                    System.out.println("Blok tidak valid");
                    return false;
                }
                blocks.add(block);
            }

            // Validasi Jumlah Block
            if (blocks.size() != P) {
                System.out.println(blocks.size());
                System.out.println("Jumlah blok tidak sesuai dengan P");
                return false;
            }

            br.close();
        }
        // printParsedData(); // Debugging
        return true;
    }

    // Mengubah list string menjadi array 2D
    static char[][] convertToBlock(List<String> blockLines) {
        int row = blockLines.size();
        int col = 0;

        // Mencari lebar maksimum dari suatu blok
        for (String line : blockLines) {
            col = Math.max(col, line.length());
        }

        char[][] block = new char[row][col];
        char firstBlockChar = ' ';
        boolean validBlock = false;

        for (int i = 0; i < row; i++) {
            String L = blockLines.get(i);
            for (int j = 0; j < col; j++) {
                if (j < L.length()) {}
                char c = j < L.length() ? L.charAt(j) : '.';
                block[i][j] = (c == ' ' ? '.' : c);

                // Validasi apakah block memiliki alfabet yang sama
                if (c != '.' && c != ' ') {
                    if (!validBlock) {
                        firstBlockChar = c;
                        validBlock = true;
                    } else if (c != firstBlockChar) {
                        return null;
                    }
                }
            }
        } 
        return block;
    }

    // Mencetak hasil data input yang uda di-parsing
    static void printParsedData() {
        System.out.println("Ukuran Papan: " + N + " x " + M);
        System.out.println("Jumlah Blok: " + P);
        System.out.println("Jenis Kasus: " + S);
        System.out.println("Blok Puzzle:");
        for (char[][] block : blocks) {
            printBlock(block);
        }
    }

    // Mencetak blok dalam bentuk matriks
    static void printBlock(char[][] block) {
        for (char[] row : block) {
            for (char cell : row) {
                System.out.print(cell + "");
            }
            System.out.println();
        }
    }
}