import java.util.Scanner;

public class Minesweeper {
    /*
    minefield:
    x is row;
    y is column
    z is state, three elements for three states
    z:
    [0] = 0/1 whether revealed;
    [1] = 0/1 whether contains bomb;
    [2] = 0/1 whether flagged
     */
    private static int[][][] minefield;
    private static Scanner keyboardIn;
    /*
    gameOver:
    0 = game in progress;
    1 = game ended, lost;
    2 = game ended, won;
     */
    private static int gameOver;
    /*
    gameStarted:
    0 = game is yet to start;
    1 = planting mine;
    2 = finished planting mine, game now in progress
     */
    private static int gameStarted;
    private static int selectX;
    private static int selectY;

    public static void main(String[] args) {
        minefield = new int[9][9][3];
        gameStarted = 0;
        gameOver = 0;
        selectX = -1;
        selectY = -1;
        keyboardIn = new Scanner(System.in);

        setMinefield();
        refresh();
    }

    public static void setMinefield() {
        for (int x = 0; x < minefield.length; x++) {
            for (int y = 0; y < minefield[x].length; y++) {
                for (int z = 0; z < minefield[x][y].length; z++) {
                    minefield[x][y][z] = 0;
                }
            }
        }
    }

    public static void plant() {
        int i = 0;
        while (i < 10) {
            int mineX = (int) (9 * Math.random());
            int mineY = (int) (9 * Math.random());
            if (minefield[mineX][mineY][1] == 1 || (mineX == selectX && mineY == selectY)) continue;
            minefield[mineX][mineY][1] = 1;
            i++;
        }
        gameStarted = 1;
        sweep();
    }

    public static void sweep() {
        String coordinate = "";

        if (gameStarted != 1) {
            while (coordinate.length() < 2 || Integer.parseInt(coordinate.charAt(1) + "") - 1 > 8 || coordinate.charAt(0) - 'A' > 8) {
                System.out.println("Please enter a valid 2 digit coordinate (Append F to flag):");
                coordinate = keyboardIn.nextLine();
            }

            selectX = Integer.parseInt(coordinate.charAt(1) + "") - 1;
            selectY = coordinate.charAt(0) - 'A';
        } else gameStarted = 2;

        if (gameStarted != 0) {
            if (coordinate.length() == 3 && coordinate.charAt(2) == 'F') minefield[selectX][selectY][2] = 1;
            if (minefield[selectX][selectY][0] == 0 && minefield[selectX][selectY][1] == 0 && minefield[selectX][selectY][2] != 1) {
                minefield[selectX][selectY][0] = 1;
                clearAdj0(selectX, selectY);
            }
            if (minefield[selectX][selectY][0] == 0 && minefield[selectX][selectY][1] == 1 && minefield[selectX][selectY][2] != 1) {
                minefield[selectX][selectY][0] = 1;
                revealMines();
                gameOver = 1;
            }
            refresh();
        } else {
            plant();
        }
    }

    public static void revealMines() {
        for (int x = 0; x < minefield.length; x++) {
            for (int y = 0; y < minefield[x].length; y++) {
                if (minefield[x][y][1] == 1) minefield[x][y][0] = 1;
            }
        }
    }

    public static void clearAdj0(int x, int y) {
        if (checkAdjMines(x, y) != 0) return;
        for (int dX = -1; dX <= 1; dX++) {
            for (int dY = -1; dY <= 1; dY++) {
                if (x + dX >= 0 && x + dX <= 8 && y + dY >= 0 && y + dY <= 8 && checkAdjMines(x + dX, y + dY) == 0) {
                    if (minefield[x + dX][y + dY][0] == 0 && minefield[x + dX][y + dY][1] == 0) {
                        minefield[x + dX][y + dY][0] = 1;
                        clearAdj0(x + dX, y + dY);
                    }
                }
            }
        }
    }

    public static int checkAdjMines(int x, int y) {
        int adjMines = 0;
        for (int dX = -1; dX <= 1; dX++) {
            for (int dY = -1; dY <= 1; dY++) {
                if (x + dX >= 0 && x + dX <= 8 && y + dY >= 0 && y + dY <= 8 && minefield[x + dX][y + dY][1] == 1)
                    adjMines++;
            }
        }
        return adjMines;
    }

    public static void checkWin() {
        int opened = 0;
        for (int x = 0; x < minefield.length; x++) {
            for (int y = 0; y < minefield[x].length; y++) {
                if (minefield[x][y][0] == 1 && minefield[x][y][1] == 0) opened++;
            }
        }

        if (opened == 71) {
            revealMines();
            gameOver = 2;
        }
    }

    public static void refresh() {
        for (int x = 0; x < minefield.length; x++) {
            for (int y = 0; y < minefield[x].length; y++) {
                if (minefield[x][y][0] == 1 && minefield[x][y][1] == 0 && checkAdjMines(x, y) == 0) {
                    for (int dX = -1; dX <= 1; dX++) {
                        for (int dY = -1; dY <= 1; dY++) {
                            if (x + dX >= 0 && x + dX <= 8 && y + dY >= 0 && y + dY <= 8)
                                minefield[x + dX][y + dY][0] = 1;
                        }
                    }
                }
            }
        }

        checkWin();

        for (int x = 0; x < minefield.length; x++) {
            System.out.print(x + 1 + " | ");
            for (int y = 0; y < minefield[x].length; y++) {
                if (minefield[x][y][0] == 0 && minefield[x][y][2] == 0) System.out.print("# ");
                if (minefield[x][y][0] == 1 && minefield[x][y][1] == 1 && minefield[x][y][2] == 0)
                    System.out.print("X ");
                if (minefield[x][y][0] == 1 && minefield[x][y][2] == 0 && checkAdjMines(x, y) == 0)
                    System.out.print("  ");
                if (minefield[x][y][0] == 1 && minefield[x][y][1] == 0 && minefield[x][y][2] == 0 && checkAdjMines(x, y) != 0)
                    System.out.print(checkAdjMines(x, y) + " ");
                if (minefield[x][y][2] == 1) System.out.print("F ");
            }
            System.out.println("|");
        }
        System.out.println("  | A B C D E F G H I |");

        if (gameOver == 1) System.out.println("You lose fool!");
        if (gameOver == 2) System.out.println("You won!");
        if (gameOver != 0) return;
        sweep();
    }
}
