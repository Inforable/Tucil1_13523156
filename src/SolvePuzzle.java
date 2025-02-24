import java.util.*;

public class SolvePuzzle {
    private int N, M;
    private char[][] board;
    private List<char[][]> blocks;
    private boolean solved = false;
    private long count = 0;

    public SolvePuzzle(int N, int M, List<char[][]> blocks) {
        this.N = N;
        this.M = M;
        this.blocks = blocks;
        this.board = new char[N][M];

        // Inisialisasi board dengan '.'
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                board[i][j] = '.';
            }
        }
    }

    public boolean isSolved() {
        return solved;
    }

    public long getCount() {
        return count;
    }

    public char[][] getBoard() {
        return board;
    }

    public void solve(int blockIdx) {
        // Kalau jumlah alfabet di blok kurang dari N * M, maka board tidak mungkin penuh
        if (countAlphabetsInBlocks(blocks) < N * M) {
            solved = false;
            return;
        }

        // Jika semua blok sudah diletakkan, maka board uda selesai (tinggal di cek aja penuh atau engga)
        if (blockIdx >= blocks.size()) {
            if (isBoardFull()) {
                solved = true;
            }
            return;
        }

        char[][] block = blocks.get(blockIdx);
        List<char[][]> variations = generateVariations(block);
        // printVariations(variations); // Debugging

        for (int r = 0; r <= N; r++) {
            for (int c = 0; c <= M; c++) {
                for (char[][] var : variations) {
                     if (isValidPlaceBlock(var, r, c)) {
                        // Untuk kasus input yang awalnya bukan alfabet, cari alfabetnya dulu
                        char s = findAlfabet(var);
                        placeValidBlock(var, r, c, s);
                        count++;
                        solve(blockIdx + 1);
                        if (solved) {
                            return;
                        }
                        removeLastBlock(var, r, c);
                    }
                }
            }
        }
    }

    // Mengecek apakah board sudah terisi penuh
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

    // Mencari alfabet pada blok (khusus input yang awalnya bukan alfabet)
    public char findAlfabet(char[][] block) {
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[i].length; j++) {
                if (block[i][j] != '.') {
                    return block[i][j];
                }
            }
        }
        return '\0';
    }

    // Mengecek apakah block dapat diletakkan pada board
    private boolean isValidPlaceBlock(char[][] block, int r, int c) {
        int rowBlock = block.length;
        int colBlock = block[0].length;

        if (r < 0 || r + rowBlock > N || c < 0 || c + colBlock > M) {
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

    // Menempatkan block yang valid pada board
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
    }

    // Menghapus block terakhir dari board (backtracking)
    private void removeLastBlock(char[][] block, int r, int c) {
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

    // Menghasilkan semua variasi unik dari blok (rotasi 90 derajat dan pencerminan horizontal)
    private List<char[][]> generateVariations(char[][] block) {
        List<char[][]> variations = new ArrayList<>();

        // Variasi dari blok asli sebanyak 4 kali
        char[][] currentBlock = block;
        for (int i = 0; i < 4; i++) {
            variations.add(copyMatrix(currentBlock));
            currentBlock = rotate(currentBlock);
        }

        // Variasi dari blok yang dicerminkan dan rotasinya sebanya 4 kali juga
        char[][] mirrored = mirror(block);
        for (int i = 0; i < 4; i++) {
            variations.add(copyMatrix(mirrored));
            mirrored = rotate(mirrored);
        }
        return variations;
    }

    // Membuat salinan matrix
    private char[][] copyMatrix(char[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;
        char[][] copy = new char[row][col];
        for (int i = 0; i < row; i++) {
            System.arraycopy(matrix[i], 0, copy[i], 0, col);
        }
        return copy;
    }

    // Rotasi matrix 90 derajat
    private char[][] rotate(char[][] block) {
        int row = block.length;
        int col = block[0].length;
        char[][] rotated = new char[col][row];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                rotated[j][row - 1 - i] = block[i][j];
            }
        }
        return rotated;
    }

    // Pencerminan horizontal matrix
    private char[][] mirror(char[][] block) {
        int row = block.length;
        int col = block[0].length;
        char[][] mirrored = new char[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                mirrored[i][col - 1 - j] = block[i][j];
            }
        }
        return mirrored;
    }

    // Menghitung jumlah alfabet pada semua blok
    public int countAlphabetsInBlocks(List<char[][]> blocks) {
        int total = 0;
    
        for (char[][] block : blocks) {
            total += countAlphabetsInBlock(block);
        }
    
        return total;
    }
    
    // Menghitung jumlah alfabet pada satu blok
    private int countAlphabetsInBlock(char[][] block) {
        int cnt = 0;
    
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[i].length; j++) {
                if (block[i][j] != '.' && block[i][j] != ' ') {
                    cnt++;
                }
            }
        }
    
        return cnt;
    }

    // Menampilkan board saat ini untuk debugging
    public void printWhiteBoard() {
        System.out.println("===== Board =====");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
        System.out.println("=================\n");
    }

    // Menampilkan semua variasi blok untuk debugging
    public void printVariations(List<char[][]> variations) {
        int index = 1;
        for (char[][] var : variations) {
            System.out.println("----- Variation #" + index++ + " -----");
            for (char[] row : var) {
                for (char ch : row) {
                    System.out.print(ch);
                }
                System.out.println();
            }
            System.out.println("---------------------\n");
        }
    }
}