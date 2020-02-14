/**
 * Main class to start a Minesweeper instance
 *
 * @author Haoye Tang and Daniel Zhang
 * @version 1581707998
 */

public class Main {
    /**
     * Starts and runs a minesweeper game.
     */
    public static void main(String[] args) {
        Minesweeper m = new Minesweeper();
        m.setMinefield();
        m.refresh();
    }
}
