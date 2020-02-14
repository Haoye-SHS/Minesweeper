/**
 * Represents a playable minesweeper game.
 *
 * @author Haoye Tang and Daniel Zhang
 * @version 1581704594
 */

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
    private int[][][] minefield;
    private Scanner keyboardIn;
    private String coordinate = "";
    /*
    gameOver:
    0 = game in progress;
    1 = game ended, lost;
    2 = game ended, won;
     */
    private int gameOver;
    /*
    gameStarted:
    0 = game is yet to start;
    1 = planting mine;
    2 = finished planting mine, game now in progress
     */
    private int gameStarted;
    // X and Y for selected block
    private int sX;
    private int sY;

    /**
     * Constructor to initialize field values necessary for a Minesweeper game
     */
    public Minesweeper() {
        minefield = new int[9][9][3];
        gameStarted = 0;
        gameOver = 0;
        sX = -1;
        sY = -1;
        keyboardIn = new Scanner(System.in);
    }

    /**
     * Creates a new minefield by clearing all bombs and flipping each square
     */
    public void setMinefield() {
        for (int x = 0; x < minefield.length; x++) {
            for (int y = 0; y < minefield[x].length; y++) {
                for (int z = 0; z < minefield[x][y].length; z++) {
                    minefield[x][y][z] = 0;
                }
            }
        }
    }

    /**
     * Plants ten mines in random positions. Then starts the sweeping and planting procedure
     */
    public void plant() {
        int i = 0;
        while (i < 10) {
            int mineX = (int) (9 * Math.random());
            int mineY = (int) (9 * Math.random());
            if (minefield[mineX][mineY][1] == 1 || (mineX == sX && mineY == sY)) continue;
            minefield[mineX][mineY][1] = 1;
            i++;
        }
        gameStarted = 1;
        sweep();
    }

    /**
     * Takes input and flips the selected square.
     */
    public void sweep() {
        if (gameStarted != 1) {
            coordinate = "";
            while (coordinate.length() < 2 || Integer.parseInt(coordinate.charAt(1) + "") - 1 > 8 || coordinate.charAt(0) - 'A' > 8) {
                System.out.println("Please enter a valid 2 digit coordinate (Append F to flag):");
                coordinate = keyboardIn.nextLine();
            }

            sX = Integer.parseInt(coordinate.charAt(1) + "") - 1;
            sY = coordinate.charAt(0) - 'A';
        } else gameStarted = 2;

        if (gameStarted != 0) {
            if (coordinate.length() == 3 && coordinate.charAt(2) == 'F' && minefield[sX][sY][2] == 0) minefield[sX][sY][2] = 1;
            else if (coordinate.length() == 3 && coordinate.charAt(2) == 'F' && minefield[sX][sY][2] == 1) minefield[sX][sY][2] = 0;

            if (coordinate.length() == 2 && minefield[sX][sY][0] == 0 && minefield[sX][sY][1] == 0 && minefield[sX][sY][2] != 1) {
                minefield[sX][sY][0] = 1;
                clearAdj0(sX, sY);
            } else if (coordinate.length() == 2 && minefield[sX][sY][0] == 0 && minefield[sX][sY][1] == 1 && minefield[sX][sY][2] != 1) {
                minefield[sX][sY][0] = 1;
                revealMines();
                gameOver = 1;
            }
            refresh();
        } else {
            plant();
        }
    }

    public void revealMines() {
        for (int x = 0; x < minefield.length; x++) {
            for (int y = 0; y < minefield[x].length; y++) {
                if (minefield[x][y][1] == 1) minefield[x][y][0] = 1;
            }
        }
    }

    /**
     * Reveals the squares around a selected square if there are no bombs
     * around the surrounding squares.
     * @param x the x coordinate of the square being checked
     * @param y the y coordinate of the square being checked
     */
    public void clearAdj0(int x, int y) {
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

    /**
     * Checks the number of bombs around a square.
     * @param x the x coordinate of the square being checked
     * @param y the y coordinate of the square being checked
     */
    public int checkAdjMines(int x, int y) {
        int adjMines = 0;
        for (int dX = -1; dX <= 1; dX++) {
            for (int dY = -1; dY <= 1; dY++) {
                if (x + dX >= 0 && x + dX <= 8 && y + dY >= 0 && y + dY <= 8 && minefield[x + dX][y + dY][1] == 1)
                    adjMines++;
            }
        }
        return adjMines;
    }

    /**
     * Checks every square to determine whether or not the user has opened every
     * non-bomb square.
     */
    public void checkWin() {
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

    /**
     * After each user input, checks the squares around the square selected
     * and displays the number of bombs, checks if you've won, and prints the
     * new board state.
     */
    public void refresh() {
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
        System.out.println("    A B C D E F G H I  ");

        if (gameOver == 1) System.out.println("You lose fool!");
        if (gameOver == 2) System.out.println("You won!");
        if (gameOver != 0) return;
        sweep();
    }
}
