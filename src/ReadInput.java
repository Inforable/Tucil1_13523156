import java.io.*;
import java.util.*;

public class ReadInput {
    static int N, M, P;
    static String S;
    static List<char[][]> blocks = new ArrayList<>();

    // Membaca file dan memparsing input
    static boolean readFile(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        // Membaca N, M, P
        String[] firstLine = br.readLine().split(" ");
        // Validasi inputan N, M dan P
        try {
            N = Integer.parseInt(firstLine[0]);
            M = Integer.parseInt(firstLine[1]);
            P = Integer.parseInt(firstLine[2]); 
        } catch (NumberFormatException e) {
            System.out.println("Input N, M, atau P tidak valid.");
            return false;
        }

        // Membaca S
        S = br.readLine().trim();

        // Membaca blok
        List<String> blockLines = new ArrayList<>();
        String prev = ""; // Menyimpan karakter sebelumnya

        String L;
        while ((L = br.readLine()) != null) {
            L = L.trim();
            if (L.isEmpty()) {
                continue;
            }

            // Validasi inputan blok apakah semuanya alfabet atau spasi
            if (!L.matches("[A-Z ]+")) {
                System.out.println("Inputan block ada yang tidak valid");
                return false;
            }

            String firstChar = String.valueOf(L.charAt(0));

            // Jika karakter pertama berubah, maka blok sebelumnya sudah selesai
            if (!blockLines.isEmpty() && !firstChar.equals(prev)) {
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
            System.out.println("Jumlah blok tidak sesuai dengan P");
            return false;
        }

        br.close();
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

    // Mencetak hasil parsing
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