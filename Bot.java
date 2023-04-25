public class Bot {
    /* Edit this, escaped characters (e.g newlines, quotes) are prohibited */
    private static final String BOT_NAME = "Verik";

    /* Declare custom fields below */

    private Board board = new Board();
    private Move lastMove;

    /* Declare custom fields above */

    public Bot() {
        board.newBoard();
    }

    /**
     * Record received move (either by enemy in normal play,
     * or by both sides in force mode) in custom structures
     * @param move received move
     * @param sideToMove side to move (either PlaySide.BLACK or PlaySide.WHITE)
     */
    public void recordMove(Move move, PlaySide sideToMove) {
        /* You might find it useful to also separately record last move in another custom field */
        int sourceY = move.getSource().get().charAt(0) - 'a' + 1;
        int sourceX = move.getSource().get().charAt(1) - '0';

        int destY = move.getDestination().get().charAt(0) - 'a' + 1;
        int destX = move.getDestination().get().charAt(1) - '0';

        Piece piece = board.getPiece(sourceX, sourceY);
        board.movePiece(piece, destX, destY);
        this.lastMove = move;
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
        return board.getRandMove();
    }

    public static String getBotName() {
        return BOT_NAME;
    }
}
