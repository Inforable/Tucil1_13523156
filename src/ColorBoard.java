import java.util.*;

public class ColorBoard {
    private static final String RESET = "\u001B[0m";
    private static final String[] COLORS = {
        "\u001B[31m", // Red
        "\u001B[32m", // Green
        "\u001B[33m", // Yellow
        "\u001B[34m", // Blue
        "\u001B[35m", // Purple
        "\u001B[36m", // Cyan
        "\u001B[37m",  // Putih
        "\u001B[90m",  // Gray
        "\u001B[91m",  // Bright Red
        "\u001B[92m",  // Bright Green
        "\u001B[93m",  // Bright Yellow
        "\u001B[94m",  // Bright Blue
        "\u001B[95m",  // Bright Purple
        "\u001B[96m",  // Bright Cyan
        "\u001B[40m",  // Black
        "\u001B[41m",  // Red Background
        "\u001B[42m",  // Green Background
        "\u001B[43m",  // Yellow Background
        "\u001B[44m",  // Blue Background
        "\u001B[45m",  // Purple Background
        "\u001B[46m",  // Cyan Background
        "\u001B[100m",  // Gray Background
        "\u001B[101m",  // Bright Red Background
        "\u001B[102m",  // Bright Green Background
        "\u001B[103m",  // Bright Yellow Background
        "\u001B[104m",  // Bright Blue Background
    };

    public static void printBoard(char[][] board) {
        Map<Character, String> CM = new HashMap<>();
        int colorIdx = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                char c = board[i][j];
                if (c != '.' && !CM.containsKey(c)) {
                    CM.put(c, COLORS[colorIdx % COLORS.length]);
                    colorIdx++;
                }
                if (c != '.') {
                    System.out.print(CM.get(c) + c + RESET);
                } else {
                    System.out.print(c);
                }
            }
            System.out.println();
        }
    }
}
