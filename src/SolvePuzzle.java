import java.util.*;

public class SolvePuzzle {
    private int N, M;
    private char[][] board;
    private List<char[][]> blocks;
    private boolean solved = false;

    public SolvePuzzle(int N, int M, List<char[][]> blocks) {
        this.N = N;
        this.M = M;
        this.blocks = blocks;
        this.board = new char[N][M];

        // Inisialisasi board
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                board[i][j] = '.';
            }
        }
    }

    public boolean solve(int idx) {
        if (idx == blocks.size()) {
            solved = true;
            return true;
        }

        char[][] block = blocks.get(idx);
        List<char[][]> variations = generateVariations(block);

        for (char[][] var : variations) {
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < M; c++) {
                    if (canPlace(var, r, c)) {
                        placeBlock(var, r, c, block[0][0]);
                        if (solve(idx + 1)) {
                            return true; // Jika berhasil menyelesaikan semua blok
                        }
                        removeBlock(var, r, c); // Lakukan backtracking
                    }
                }
            }
        }
        return false;
    }

    // Pengecekan untuk blok bisa diletakkan atau tidak
    private boolean canPlace(char[][] block, int r, int c) {
        int rowBlock = block.length;
        int colBlock = block[0].length;

        if (r + rowBlock > N || c + colBlock > M) {
            return false;
        }

        for (int i = 0; i < rowBlock; i++) {
            for (int j = 0; j < colBlock; j++) {
                if (block[i][j] != '.' && board[r + i][c + j] != '.') {
                    return false;
                }
            }
        }
        return true;
    }

    private void placeBlock(char[][] block, int r, int c, char s) {
        int rowBlock = block.length;
        int colBlock = block[0].length;

        for (int i = 0; i < rowBlock; i++) {
            for (int j = 0; j < colBlock; j++) {
                if (block[i][j] != '.') {
                    board[r + i][c + j] = s;
                }
            }
        }
    }

    // Melakukan backtracking
    private void removeBlock(char[][] block, int r, int c) {
        int rowBlock = block.length;
        int colBlock = block[0].length;

        for (int i = 0; i < rowBlock; i++) {
            for (int j = 0; j < colBlock; j++) {
                if (block[i][j] != '.') {
                    board[r + i][c + j] = '.';
                }
            }
        }
    }

    private List<char[][]> generateVariations(char[][] block) {
        List<char[][]> var = new ArrayList<>();
        var.add(block);
        char[][] rotated = block;

        // Terdapat 4 variasi block yang mungkin (dihitung dengan posisi awalnya juga)
        for (int i = 0; i < 3; i++) {
            rotated = rotate(rotated);
            var.add(rotated);
        }

        // Terdapat 2 variasi block yang mungkin (dihitung dengan posisi awalnya juga)
        char[][] mirrored = mirror(block);
        var.add(mirrored);
        rotated = mirrored;

        // Terdapat 4 variasi block yang mungkin (dihitung dengan posisi awalnya juga)
        for (int i = 0; i < 3; i++) {
            rotated = rotate(rotated);
            var.add(rotated);
        }
        return var;
    }

    // Melakukan rotasi 90 derajat
    private char[][] rotate(char[][] block) {
        int rowBlock = block.length;
        int colBlock = block[0].length;
        char[][] rotated = new char[colBlock][rowBlock];

        for (int i = 0; i < rowBlock; i++) {
            for (int j = 0; j < colBlock; j++) {
                rotated[j][rowBlock - 1 - i] = block[i][j];
            }
        }
        return rotated;
    }

    // Melakukan pencerminan secara horizontal
    private char[][] mirror(char[][] block) {
        int rowBlock = block.length;
        int colBlock = block[0].length;
        char[][] mirrored = new char[rowBlock][colBlock];

        for (int i = 0; i < rowBlock; i++) {
            for (int j = 0; j < colBlock; j++) {
                mirrored[i][colBlock - 1 - j] = block[i][j];
            }
        }
        return mirrored;
    }

    public void printBoard() {
        if (solved) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    System.out.print(board[i][j]);
                }
                System.out.println();
            }
        } else {
            System.out.println("Tidak ada solusi");
        }
    }
}
