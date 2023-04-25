package main;

import pieces.Piece;

public class Bot {
    /* Edit this, escaped characters (e.g newlines, quotes) are prohibited */
    private static final String BOT_NAME = "Verik";

    /* Declare custom fields below */

    private Board board;
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
        PlaySide engineSide = Main.getEngineSide();

        if (engineSide == PlaySide.NONE) {
            System.out.println(">>> Engine side is NONE <<<");
            return;
        }

        if (engineSide != sideToMove) {
            move.translateMove();
            System.out.println("Source after translation: " + move.getSource());
            System.out.println("Dest after translation: " + move.getDestination());
        }

        //TODO Handle drop scenario.

        board.movePiece(move);

        System.out.println("<<< Table after recording first move >>>");
        DebugTools.printBoardPretty(board.getBoard());

        this.lastMove = move;
    }

    /**
     * Calculate and return the bot's next move
     * @return your move
     */
    public Move calculateNextMove() {
        System.out.println(" >>>>>>>>>>>> Calculating next move...");
        /* Calculate next move for the side the engine is playing (Hint: main.Main.getEngineSide())
        * Make sure to record your move in custom structures before returning.
        *
        * Return move that you are willing to submit
        * Pieces.main.Move is to be constructed via one of the factory methods defined in Pieces.main.Move.java */
        return board.getRandMove();
    }

    public static String getBotName() {
        return BOT_NAME;
    }
}
