package main;

public class Bot {
    /* Edit this, escaped characters (e.g newlines, quotes) are prohibited */
    private static final String BOT_NAME = "Verik";

    /* Declare custom fields below */

    public Board board;
    private Move lastMove;

    /* Declare custom fields above */

    public Bot() {
        this.board = new Board();
    }

    /**
     * Record received move (either by enemy in normal play,
     * or by both sides in force mode) in custom structures
     * @param move received move
     * @param sideToMove side to move (either main.PlaySide.BLACK or main.PlaySide.WHITE)
     */
    public void recordMove(Move move, PlaySide sideToMove) {
        board.registerMove(move);
        this.lastMove = move;
    }

    /**
     * Calculate and return the bot's next move
     * @return your move
     */
    public Move calculateNextMove(PlaySide engineSide) {
        Move move = board.aggressiveMode(engineSide);
        board.registerMove(move);
        return move;
    }

    public static String getBotName() {
        return BOT_NAME;
    }
}
