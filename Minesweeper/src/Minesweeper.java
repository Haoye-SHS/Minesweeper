import java.util.Scanner;

public class Minesweeper {
    private static String minefield[][];
    private static Scanner keyboardIn;
    private static boolean gameOver = false;
    private static boolean gameStarted = false;

    public static void main(String[] args) {
        plantMines();
        minesweeper();
    }

    public static void plantMines() {
        minefield = new String[9][9];
        for(int x = 0; x < minefield.length; x++) {
            System.out.print(x + 1 + " | ");
            for(int y = 0; y < minefield[x].length; y++) {
                minefield[x][y] = "00";
                System.out.print("█ ");
            }
            System.out.println("");
        }
        System.out.println("    A B C D E F G H I");

        int i = 0;
        while(i < 10) {
            int mineX = (int)(9*Math.random());
            int mineY = (int)(9*Math.random());
            if(minefield[mineX][mineY].charAt(0) == '2') continue;
            minefield[mineX][mineY] = '2' + minefield[mineX][mineY].substring(1);
            i++;
        }

        int adjMine = 0;
        for(int x = 0; x < minefield.length; x++) {
            for(int y = 0; y < minefield[x].length; y++) {
                for(int ax = -1; ax <= 1; ax++) {
                    for(int ay = -1; ay <= 1; ay++) {
                        if(x+ax>=0 && x+ax<=8 && y+ay>=0 && y+ay<=8 && minefield[x+ax][y+ay].charAt(0) == '2') adjMine++;
                    }
                }
                minefield[x][y] = minefield[x][y].substring(0,1) + adjMine;
                adjMine = 0;
            }
        }
    }

    public static void minesweeper() {
        keyboardIn = new Scanner(System.in);

        String coordinate = "";
        while(coordinate.length()<2 || Integer.parseInt(coordinate.charAt(1)+"")-1 > 8 || coordinate.charAt(0) - 'A' > 8){
            coordinate = keyboardIn.nextLine();
            System.out.println("Please input a 2 digit valid coordinate!");
        }

        int selectX = Integer.parseInt(coordinate.charAt(1)+"")-1;
        int selectY = coordinate.charAt(0) - 'A';
        if(minefield[selectX][selectY].charAt(0) == '0') {
            minefield[selectX][selectY] = '1' + minefield[selectX][selectY].substring(1);
            clearAdjacent(selectX,selectY);
        }
        if(minefield[selectX][selectY].charAt(0) == '2') minefield[selectX][selectY] = '3' + minefield[selectX][selectY].substring(1);

        refreshMinefield();
    }

    public static void clearAdjacent(int x, int y) {
        for(int i = -1; i <= 1; i++) {
            for (int ii = -1; ii <= 1; ii++) {
                if(x+i>=0 && x+i<=8 && y+ii>=0 && y+ii<=8 && minefield[x+i][y+ii].charAt(1) == '0' && minefield[x+i][y+ii].charAt(0) == '0') {
                    minefield[x+i][y+ii] = "1" + minefield[x+i][y+ii].charAt(1);
                    clearAdjacent(x+i,y+ii);
                }
            }
        }
    }

    public static void refreshMinefield() {
        for(int x = 0; x < minefield.length; x++) {
            System.out.print(x + 1 + " | ");
            for(int y = 0; y < minefield[x].length; y++) {
                if(minefield[x][y].charAt(0) == '0' || minefield[x][y].charAt(0) == '2') System.out.print("█ ");
                if(minefield[x][y].charAt(0) == '3') System.out.print("X ");
                if(minefield[x][y].charAt(0) == '1' && minefield[x][y].charAt(1) == '0') {
                    System.out.print("  ");
                }
                if(minefield[x][y].charAt(0) == '1' && minefield[x][y].charAt(1) != '0') System.out.print(minefield[x][y].charAt(1) + " ");
            }
            System.out.println("");
        }
        System.out.println("    A B C D E F G H I");
        if(gameOver == true) return;
        minesweeper();
    }
}
