import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
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
            if (solver.solve(0)) {
                System.out.println("Solusi ditemukan:");
                solver.printBoard();
            } else {
                System.out.println("Tidak ada solusi");
            }
        } catch (IOException e) {
            System.out.println("File tidak ditemukan");
        }
    }
}