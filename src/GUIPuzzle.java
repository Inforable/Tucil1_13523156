import javax.swing.*;
import java.awt.*;
import java.io.*;

public class GUIPuzzle {
    private JFrame frame;
    private JPanel mainPanel, topPanel, bottomPanel;
    private JButton loadButton, solveButton, saveButton;
    private BoardPanel boardPanel;
    private JLabel infoLabel;
    private JFileChooser fileChooser;
    private SolvePuzzle currentSolver; // Stores current solver after loading
    private File currentPuzzleFile; // Stores current puzzle file after loading

    public GUIPuzzle() {
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Puzzle Solver");
        mainPanel = new JPanel(new BorderLayout());

        // untuk tombol di atas
        topPanel = new JPanel();
        loadButton = new JButton("Load Puzzle");
        solveButton = new JButton("Solve Puzzle");
        saveButton = new JButton("Save Solution");

        loadButton.addActionListener(_ -> loadPuzzle());
        solveButton.addActionListener(_ -> solvePuzzle());
        saveButton.addActionListener(_ -> saveSolution());

        topPanel.add(loadButton);
        topPanel.add(solveButton);
        topPanel.add(saveButton);

        // untuk board
        boardPanel = new BoardPanel();
        boardPanel.setPreferredSize(new Dimension(400, 400));

        JPanel boardContainer = new JPanel(new GridBagLayout());
        boardContainer.add(boardPanel);

        // untuk info di bawah
        bottomPanel = new JPanel();
        infoLabel = new JLabel("Selamat Datang di IQ Puzzle Solver!");
        bottomPanel.add(infoLabel);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(boardContainer, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.pack();
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        fileChooser = new JFileChooser();
    }

    private void loadPuzzle() {
        // me-reset solver dan board
        currentSolver = null;
        boardPanel.setBoard(null);
        infoLabel.setText("Selamat Datang di IQ Puzzle Solver!");
        
        // Me-reset input data di ReadInput
        ReadInput.N = 0;
        ReadInput.M = 0;
        ReadInput.P = 0;
        ReadInput.S = "";
        ReadInput.blocks.clear();
        
        String[] options = {"Browse File", "Enter Path"};
        int choice = JOptionPane.showOptionDialog(frame, 
                "Pilih metode untuk load file:",
                "Load Puzzle", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, options, options[0]);
    
        String filePath = null;
        if (choice == 0) { // Untuk metode browser file
            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                filePath = file.getAbsolutePath();
                currentPuzzleFile = file;
            }
        } else if (choice == 1) { // Untuk metode input path
            filePath = JOptionPane.showInputDialog(frame, "Masukkan path file:");
            if (filePath != null && !filePath.trim().isEmpty()) {
                currentPuzzleFile = new File(filePath);
            }
        }
    
        if (filePath == null || filePath.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "File tidak valid atau tidak dipilih.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            // Baca dan parse file puzzle
            boolean isValidInput = ReadInput.readFile(filePath);
            if (!isValidInput) {
                JOptionPane.showMessageDialog(frame, "Input ada yang tidak valid. Program dihentikan", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Update info label dengan detail puzzle
            String info = "<html>Ukuran Papan: " + ReadInput.N + " x " + ReadInput.M +
                          "<br>Jumlah Blok: " + ReadInput.blocks.size() +
                          "<br>Jenis Kasus: " + ReadInput.S + "</html>";
            infoLabel.setText(info);
    
            // inisialiasi board
            char[][] initialBoard = new char[ReadInput.N][ReadInput.M];
            for (int i = 0; i < ReadInput.N; i++) {
                for (int j = 0; j < ReadInput.M; j++) {
                    initialBoard[i][j] = '.';
                }
            }
            boardPanel.setBoard(initialBoard);
            boardPanel.setPreferredSize(new Dimension(ReadInput.M * boardPanel.getCellSize(), 
                                                      ReadInput.N * boardPanel.getCellSize()));
            boardPanel.revalidate();
    
            // inisialisasi solvepuzzle
            currentSolver = new SolvePuzzle(ReadInput.N, ReadInput.M, ReadInput.blocks);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void solvePuzzle() {
        if (currentSolver == null) {
            JOptionPane.showMessageDialog(frame, "Puzzle belum di-load.", "Selamat Datang di IQ Puzzle Solver!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        long startTime = System.currentTimeMillis();
        currentSolver.solve(0);
        long endTime = System.currentTimeMillis();

        if (currentSolver.isSolved()) {
            boardPanel.setBoard(currentSolver.getBoard());
            String info = "<html>Solusi ditemukan!<br>" +
                          "Waktu Pencarian: " + (endTime - startTime) + " ms<br>" +
                          "Banyak Kasus yang Ditinjau: " + currentSolver.getCount() + "</html>";
            infoLabel.setText(info);
        } else {
            String info = "<html>Solusi tidak ditemukan.<br>" +
                          "Banyak Kasus yang Ditinjau: " + currentSolver.getCount() + "</html>";
            infoLabel.setText(info);
        }
    }

    private void saveSolution() {
        // Mengecek uda solve atau belom
        if (currentSolver == null || !currentSolver.isSolved()) {
            JOptionPane.showMessageDialog(frame, "Tidak ada solusi untuk disimpan.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    
        String[] formatOptions = {"TXT", "JPG"};
        int formatChoice = JOptionPane.showOptionDialog(frame,
                "Ingin Menyimpan Solusi dalam Format Apa?",
                "Pilih Format",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                formatOptions,
                formatOptions[0]);
        if (formatChoice == JOptionPane.CLOSED_OPTION) {
            return;
        }
       // browse folder untuk menyimpan file
        int returnValue = fileChooser.showSaveDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filePath = file.getAbsolutePath();
            try {
                if (formatChoice == 0) { // Untuk format text
                    if (!filePath.toLowerCase().endsWith(".txt")) {
                        filePath += ".txt";
                    }
                    ColorBoard.saveTextSolution(currentSolver.getBoard(), filePath);
                    JOptionPane.showMessageDialog(frame, "Solusi berhasil disimpan di " + filePath, 
                            "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                } else if (formatChoice == 1) { // Untuk format image
                    if (!(filePath.toLowerCase().endsWith(".jpg") || filePath.toLowerCase().endsWith(".jpeg"))) {
                        filePath += ".jpg";
                    }
                    ColorBoard.saveImageSolution(currentSolver.getBoard(), filePath);
                    JOptionPane.showMessageDialog(frame, "Solusi berhasil disimpan di " + filePath, 
                            "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Terjadi kesalahan saat menyimpan: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Panel untuk menampilkan board
    class BoardPanel extends JPanel {
        private char[][] board;
        private int cellSize = 40;

        public int getCellSize() {
            return cellSize;
        }

        public void setBoard(char[][] board) {
            this.board = board;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (board == null) {
                return;
            }
            int rows = board.length;
            int cols = board[0].length;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    char c = board[i][j];
                    if (c == '.') {
                        g.setColor(Color.WHITE);
                    } else {
                        g.setColor(getColorForChar(c));
                    }
                    g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);

                    if (c != '.') {
                        g.setColor(Color.BLACK);
                        Font lexendFont = new Font("Lexend", Font.BOLD, cellSize - 10);
                        g.setFont(lexendFont);
                        FontMetrics fm = g.getFontMetrics(lexendFont);
                        String letter = String.valueOf(c);
                        int textWidth = fm.stringWidth(letter);
                        int textHeight = fm.getAscent();
                        int x = j * cellSize + (cellSize - textWidth) / 2;
                        int y = i * cellSize + ((cellSize - fm.getHeight()) / 2) + fm.getAscent();
                        g.drawString(letter, x, y);
                    }
                }
            }
        }

        private Color getColorForChar(char c) {
            int index = c - 'A';
            Color[] colors = {
                new Color(194, 54, 33),    // Red
                new Color(37, 188, 36),    // Green
                new Color(173, 173, 39),   // Yellow
                new Color(73, 46, 225),    // Blue
                new Color(211, 56, 211),   // Purple
                new Color(51, 187, 200),   // Cyan
                new Color(255, 0, 0),      // Bright Red
                new Color(0, 255, 0),      // Bright Green
                new Color(255, 255, 0),    // Bright Yellow
                new Color(0, 0, 255),      // Bright Blue
                new Color(255, 0, 255),    // Bright Purple
                new Color(0, 255, 255),    // Bright Cyan
                Color.BLACK,               // Black
                new Color(194, 54, 33),    // Red Background
                new Color(37, 188, 36),    // Green Background
                new Color(173, 173, 39),   // Yellow Background
                new Color(73, 46, 225),    // Blue Background
                new Color(211, 56, 211),   // Purple Background
                new Color(51, 187, 200),   // Cyan Background
                Color.GRAY,                // Gray Background
                new Color(255, 0, 0),      // Bright Red Background
                new Color(0, 255, 0),      // Bright Green Background
                new Color(255, 255, 0),    // Bright Yellow Background
                new Color(0, 0, 255),      // Bright Blue Background
                new Color(255, 0, 255),    // Bright Purple Background
                new Color(0, 255, 255)     // Bright Cyan Background
            };
            if (index >= 0 && index < colors.length)
                return colors[index];
            return Color.DARK_GRAY;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUIPuzzle::new);
    }
}