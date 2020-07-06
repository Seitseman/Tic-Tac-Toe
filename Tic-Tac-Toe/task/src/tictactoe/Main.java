package tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private  static String checkRowsCols(char[][] table) {
        List<Character> winningCharsRows = new ArrayList<>();
        List<Character> winningCharsCols = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            char chRow = table[i][0];
            if (chRow != ' ' && table[i][1] == table[i][2]
            && chRow == table[i][1]) {
                winningCharsRows.add(chRow);
            }

            char chCol = table[0][i];
            if (chCol != ' ' &&table[1][i] == table[2][i]
            && chCol == table[1][i]) {
                winningCharsCols.add(chCol);
            }
        }

        if (winningCharsRows.size() == 1)
            return winningCharsRows.get(0) + " wins";
        if (winningCharsRows.size() > 1)
            return "Impossible";

        if (winningCharsCols.size() == 1)
            return winningCharsCols.get(0) + " wins";
        if (winningCharsCols.size() > 1)
            return "Impossible";

        return "";
    }

    private static String checkDiags(char[][] table) {
        List<Character> winningCharsDiag = new ArrayList<>();
        char topLeft = table[0][0];
        char topRight = table[0][2];

        if (topLeft != ' ' && topLeft == table[1][1] && topLeft == table[2][2]) {
            winningCharsDiag.add(topLeft);
        }

        if (topRight != ' ' && topRight == table[1][1] && topRight == table[2][0]) {
            if (winningCharsDiag.isEmpty())
                winningCharsDiag.add(topRight);
            if (winningCharsDiag.size() == 1 && winningCharsDiag.get(0) != topRight) {
                return "Impossible";
            }
        }

        if (winningCharsDiag.size() > 1) {
            return "Impossible";
        }
        if (winningCharsDiag.size() == 1) {
            return winningCharsDiag.get(0) + " wins";
        }

        return "";
    }

    public static String checkForDraw(char[][] table) {
        int emptySlots = 0;
        int delta = 0;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (table[i][j] == '_' || table[i][j] == ' ') {
                    ++emptySlots;
                }
                if (table[i][j] == 'X') {
                    ++delta;
                } else if (table[i][j] == 'O') {
                    --delta;
                }
            }
        }

        if (Math.abs(delta) > 1) {
            return "Impossible";
        }
        if (emptySlots > 0) {
            return "Game not finished";
        }
        return "Draw";
    }

    private static String stateOfField(char[][] table) {
        String winner = checkRowsCols(table);
        if (winner.isEmpty()) {
            winner = checkDiags(table);
            if (winner.isEmpty()) {
                winner = checkForDraw(table);
                if (winner.isEmpty()) {
                    winner = "Game not finished";
                }
            }
        }
        return winner;
    }

    private static String fillRow(char[] rawString, int a, int b, int c) {
        return  "| " + rawString[a] + " " + rawString[b] + " " + rawString[c] + " |";
    }

    private static void formatFriendlyStrings(String[] friendlyString, char[] row) {
        friendlyString[0] = fillRow(row, 0, 1, 2);
        friendlyString[1] = fillRow(row, 3, 4, 5);
        friendlyString[2] = fillRow(row, 6, 7, 8);
    }

    private static void printTable(String[] friendlyStrings) {
        System.out.println("---------");
        System.out.println(String.join("\n", Arrays.asList(friendlyStrings)));
        System.out.println("---------");
    }

    private static String playRoundReturnState(char[] rawString, char playerCh, Scanner scanner) {
        int x = 0;
        int y = 0;

        while (true) {
            System.out.print("Enter the coordinates: ");
            x = scanner.nextInt();
            y = scanner.nextInt();

            if (x > 3 || y > 3 || x < 1 || y < 1) {
                System.out.println("Coordinates should be from 1 to 3!");
                continue;
            }

            char curVal = rawString[3 * (3 - y) + (x - 1)];
            if (curVal == 'X' || curVal == 'O') {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            }

            break;
        }

        rawString[3 * (3 - y) + (x - 1)] = playerCh;
        String[] friendlyStrings = new String[3];
        formatFriendlyStrings(friendlyStrings, rawString);
        printTable(friendlyStrings);

        char stringToAnalyze[][] = new char[3][3];
        for (int i = 0; i < 3; ++i) {
            stringToAnalyze[i][0] = rawString[i * 3];
            stringToAnalyze[i][1] = rawString[i * 3 + 1];
            stringToAnalyze[i][2] = rawString[i * 3 + 2];
        }

        return stateOfField(stringToAnalyze);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] friendlyStrings = new String[3];

        char[] row = new char[9];
        for (int i = 0; i < 9; ++i) {
            row[i] = ' ';
        }

        formatFriendlyStrings(friendlyStrings, row);
        printTable(friendlyStrings);

        String stateOfGame = "";
        char curPlayer = 'X';

        while (true) {
            stateOfGame = playRoundReturnState(row, curPlayer, scanner);
            curPlayer = curPlayer == 'X' ? 'O' : 'X';

            if (stateOfGame.equals("Draw") || stateOfGame.endsWith("wins")) {
                System.out.println(stateOfGame);
                return;
            }
        }

    }
}
