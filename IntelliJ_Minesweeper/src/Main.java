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
