import java.util.*;

public class SolvePuzzle {
    private int N, M;
    private char[][] board;
    private List<char[][]> blocks;
    private boolean solved = false;
    private int count = 0;

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

    public boolean isSolved() {
        return solved;
    }

    public int getCount() {
        return count;
    }

    public char[][] getBoard() {
        return board;
    }

    public void solve(int blockIdx) {
        if (blockIdx == blocks.size()) {
            if (isBoardFull()) {
                solved = true;
            }
            return; 
        }

        char[][] block = blocks.get(blockIdx);
        List<char[][]> variations = generateVariations(block);

        for (char[][] var : variations) {
            for (int r = 0; r < N - var.length + 1; r++) {
                for (int c = 0; c < M - var[0].length + 1; c++) {
                    if (isValidPlaceBlock(var, r, c)) {
                        placeValidBlock(var, r, c, block[0][0]);
                        count++;
                        solve(blockIdx + 1);
                        if (solved) {
                            return;
                        }
                        removeBlock(var, r, c); // Lakukan backtracking
                    }
                }
            }
        }
    }

    // Pengecekan apakah board sudah terisi penuh
    private boolean isBoardFull() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (board[i][j] == '.') {
                    return false;
                }
            }
        }
        return true;
    }

    // Pengecekan untuk blok bisa diletakkan atau tidak
    private boolean isValidPlaceBlock(char[][] block, int r, int c) {
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

    // Meletakkan blok yang valid
    private void placeValidBlock(char[][] block, int r, int c, char s) {
        int rowBlock = block.length;
        int colBlock = block[0].length;

        for (int i = 0; i < rowBlock; i++) {
            for (int j = 0; j < colBlock; j++) {
                if (block[i][j] != '.') {
                    board[r + i][c + j] = s;
                }
            }
        }
        // printBoard(); // Debugging
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
        // printBoard(); // Debugging
    }

    // Menghasilkan variasi dari blok
    private List<char[][]> generateVariations(char[][] block) {
        List<char[][]> var = new ArrayList<>();
        var.add(block);
        char[][] rotated = block;

        // Terdapat maksimal 4 variasi block yang mungkin (dihitung dengan posisi awalnya juga)
        for (int i = 0; i < 3; i++) {
            rotated = rotate(rotated);
            var.add(rotated);
        }

        // Terdapat maksimal 2 variasi block yang mungkin (dihitung dengan posisi awalnya juga)
        char[][] mirrored = mirror(block);
        var.add(mirrored);
        rotated = mirrored;

        // Terdapat maksimal 4 variasi block yang mungkin (dihitung dengan posisi awalnya juga)
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

    // public void printBoard() {
    //     for (int i = 0; i < N; i++) {
    //         for (int j = 0; j < M; j++) {
    //             System.out.print(board[i][j]);
    //         }
    //         System.out.println();
    //     }
    // }
}
