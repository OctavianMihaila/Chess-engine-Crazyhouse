public class Bot {
    /* Edit this, escaped characters (e.g newlines, quotes) are prohibited */
    private static final String BOT_NAME = "Verik";

    /* Declare custom fields below */

    private Piece[][] board;

    /* Declare custom fields above */

    public Bot() {

    }

    public void newBoard() {
        this.board = new Piece[9][9];

    }

    /**
     * Record received move (either by enemy in normal play,
     * or by both sides in force mode) in custom structures
     * @param move received move
     * @param sideToMove side to move (either PlaySide.BLACK or PlaySide.WHITE)
     */
    public void recordMove(Move move, PlaySide sideToMove) {
        /* You might find it useful to also separately record last move in another custom field */
    }

    /**
     * Calculate and return the bot's next move
     * @return your move
     */
    public Move calculateNextMove() {
        /* Calculate next move for the side the engine is playing (Hint: Main.getEngineSide())
        * Make sure to record your move in custom structures before returning.
        *
        * Return move that you are willing to submit
        * Pieces.Move is to be constructed via one of the factory methods defined in Pieces.Move.java */
        return Move.resign();
    }

    public static String getBotName() {
        return BOT_NAME;
    }
}
