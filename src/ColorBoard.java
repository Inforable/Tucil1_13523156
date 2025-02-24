import java.util.*;
import java.io.*;
import java.awt.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;

public class ColorBoard {
    private static final String reset = "\u001B[0m";
    private static final String[] colors = {
        "\u001B[38;5;9m",  // A - Red
        "\u001B[38;5;10m", // B - Green
        "\u001B[38;5;11m", // C - Yellow
        "\u001B[38;5;12m", // D - Blue
        "\u001B[38;5;13m", // E - Purple
        "\u001B[38;5;14m", // F - Cyan
        "\u001B[38;5;1m",  // G - Bright Red
        "\u001B[38;5;2m",  // H - Bright Green
        "\u001B[38;5;3m",  // I - Bright Yellow
        "\u001B[38;5;4m",  // J - Bright Blue
        "\u001B[38;5;5m",  // K - Bright Purple
        "\u001B[38;5;6m",  // L - Bright Cyan
        "\u001B[38;5;0m",  // M - Black
        "\u001B[38;5;1m",  // N - Red Background
        "\u001B[38;5;2m",  // O - Green Background
        "\u001B[38;5;3m",  // P - Yellow Background
        "\u001B[38;5;4m",  // Q - Blue Background
        "\u001B[38;5;5m",  // R - Purple Background
        "\u001B[38;5;6m",  // S - Cyan Background
        "\u001B[38;5;7m",  // T - Gray Background
        "\u001B[38;5;8m",  // U - Bright Red Background
        "\u001B[38;5;9m",  // V - Bright Green Background
        "\u001B[38;5;10m", // W - Bright Yellow Background
        "\u001B[38;5;11m", // X - Bright Blue Background
        "\u001B[38;5;12m", // Y - Bright Purple Background
        "\u001B[38;5;13m"  // Z - Bright Cyan Background
    };

    public static final Color[] image_colors = {
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

    // Mencetak board dengan block berwarna ke layar
    public static void printBoard(char[][] board) {
        Map<Character, String> CM = new HashMap<>();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                char c = board[i][j];
                if (c != '.' && !CM.containsKey(c)) {
                    CM.put(c, colors[c - 'A']);
                }
                if (c != '.') {
                    System.out.print(CM.get(c) + c + reset);
                } else {
                    System.out.print(c);
                }
            }
            System.out.println();
        }
    }

    // Menyimpan solusi dalam format txt
    public static void saveTextSolution(char[][] board, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    writer.write(board[i][j]);
                }
                writer.write(System.lineSeparator());
            }
        }
    }

    // Menyimpan solusi dalam format jpg
    public static void saveImageSolution(char[][] board, String filePath) throws IOException {
        int cellSize = 100;
        int width = board[0].length * cellSize;
        int height = board.length * cellSize;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Map<Character, Color> CM = new HashMap<>();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                char c = board[i][j];
                if (c != '.' && !CM.containsKey(c)) {
                    CM.put(c, image_colors[c - 'A']);
                }
                g.setColor(c == '.' ? Color.WHITE : CM.get(c));
                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
                if (c != '.') {
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Lexend", Font.BOLD, cellSize / 2));
                    FontMetrics fm = g.getFontMetrics();
                    int x = j * cellSize + (cellSize - fm.charWidth(c)) / 2;
                    int y = i * cellSize + ((cellSize - fm.getHeight()) / 2) + fm.getAscent();
                    g.drawString(String.valueOf(c), x, y);
                }
            }
        }
        g.dispose();
        ImageIO.write(image, "jpg", new File(filePath));
    }
}