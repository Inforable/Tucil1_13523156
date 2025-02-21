import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        // Membaca path file dari input user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan path file: ");
        String filePath = scanner.nextLine();
        scanner.close();

        try {
            boolean isValidInput = ReadInput.readFile(filePath);
            if (!isValidInput) {
                System.out.println("Input ada yang tidak valid. Program dihentikan,");
                return;
            }
            ReadInput.printParsedData();

            SolvePuzzle solver = new SolvePuzzle(ReadInput.N, ReadInput.M, ReadInput.blocks);
            long startTime = System.currentTimeMillis();
            solver.solve(0);
            long endTime = System.currentTimeMillis();

            if (solver.isSolved()) {
                System.out.println("Solusi ditemukan!");
                solver.printBoard();
                System.out.println("Waktu eksekusi: " + (endTime - startTime) + " ms");
            } else {
                System.out.println("Solusi tidak ditemukan.");
            }
            System.out.println("Jumlah Iterasi: " + solver.getCount());
        } catch (IOException e) {
            System.out.println("File tidak ditemukan");
        }
    }
}