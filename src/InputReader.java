import java.io.*;
import java.util.*;

public class InputReader {
    static int N, M, P;
    static String S;
    static List<char[][]> blocks = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan path file: ");
        String filePath = scanner.nextLine();
        scanner.close();

        try {
            readFile(filePath);
            printParsedData(); // Untuk mengecek apakah output sudah sesuai
        } catch (IOException e) {
            System.out.println("File tidak ditemukan");
        }
    }

    // Membaca file dan memparsing input
    static void readFile(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        // Membaca N, M, P
        String[] firstLine = br.readLine().split(" ");
        N = Integer.parseInt(firstLine[0]);
        M = Integer.parseInt(firstLine[1]);
        P = Integer.parseInt(firstLine[2]);

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

            String firstChar = String.valueOf(L.charAt(0));

            // Jika karakter pertama berubah, maka blok sebelumnya sudah selesai
            if (!blockLines.isEmpty() && !firstChar.equals(prev)) {
                blocks.add(convertToBlock(blockLines));
                blockLines.clear();
            }

            blockLines.add(L);
            prev = firstChar;
        }

        // Jika sudah sampai di blok terakhir
        if (!blockLines.isEmpty()) {
            blocks.add(convertToBlock(blockLines));
        }

        br.close();
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
        for (int i = 0; i < row; i++) {
            String L = blockLines.get(i);
            for (int j = 0; j < col; j++) {
                block[i][j] = j < L.length() ? L.charAt(j) : '.';
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
                System.out.print((cell == '.' ? ' ' : cell) + "");
            }
            System.out.println();
        }
    }
}