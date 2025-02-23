import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        // Membaca path file dari input user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan path file: ");
        String filePath = scanner.nextLine();

        try {
            boolean isValidInput = ReadInput.readFile(filePath);
            if (!isValidInput) {
                System.out.println("Input ada yang tidak valid. Program dihentikan,");
                return;
            }
            // ReadInput.printParsedData(); // Debugging

            SolvePuzzle solver = new SolvePuzzle(ReadInput.N, ReadInput.M, ReadInput.blocks);
            long startTime = System.currentTimeMillis();
            solver.solve(0);
            long endTime = System.currentTimeMillis();

            if (solver.isSolved()) {
                System.out.println("Solusi ditemukan!!!");
                System.out.println("===== Board =====");
                ColorBoard.printBoard(solver.getBoard());
                System.out.println("=================\n");

                System.out.println("Waktu Pencarian " + (endTime - startTime) + " ms");
                System.out.println("Banyak Kasus yang Ditinjau: " + solver.getCount());

                System.out.println("Apakah Anda ingin Menyimpan Solusi (y/n)? ");
                String save = scanner.nextLine().trim().toLowerCase();
                if (save.equals("y")) {
                    System.out.println("Ingin Menyimpan Solusi dalam Format Apa (txt/jpg)? ");
                    String format = scanner.nextLine().trim().toLowerCase();

                    // Menyesuaikan nama file sesuai dengan tc ke berapa
                    String fileName = new File(filePath).getName();
                    String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
                    String suffix = baseName.replace("tc", "");
                    String resultFileName = "result" + suffix + "." + format;
                    String savePath = "../test/result/" + resultFileName;

                    if (format.equals("txt")) { // Menyimpan solusi dalam format text
                        ColorBoard.saveTextSolution(solver.getBoard(), savePath);
                        System.out.println("Solusi berhasil disimpan di " + savePath);
                    } else if (format.equals("jpg")) { // Menyimpan solusi dalam format jpg
                        ColorBoard.saveImageSolution(solver.getBoard(), savePath);
                        System.out.println("Solusi berhasil disimpan di " + savePath);
                    } else {
                        System.out.println("Format tidak valid");
                    }
                } else if (save.equals("n")) {
                    System.out.println("Solusi tidak disimpan");
                } else {
                    System.out.println("Input tidak valid");
                }
            } else {
                System.out.println("Solusi tidak ditemukan.");
                System.out.println("Banyak Kasus yang Ditinjau: " + solver.getCount());
            }
        } catch (IOException e) {
            System.out.println("Ada kesalahan");
        } finally {
            scanner.close();
        }
    }
}