package main;

import pieces.Piece;

import java.util.Optional;

public class Move {
    /* Positions (source, destination) are encoded in coordinate notation
     as strings (i.e. "e1", "f6", "a4" etc.) */
    private Optional<String> source;
    private Optional<String> destination;

    /* main.Piece to promote a pawn advancing to last row, or
    *  piece to drop-in (from captured assets) */
    private final Optional<PieceType> replacement;
    private final int score;

    /*
      Use the following 4 constructors for Pieces.main.Move:
      moveTo(src, dst), if emitting a standard move (advance, capture, castle)
      promote(src, dst, replace), if advancing a pawn to last row
      dropIn(dst, replace), if placing a captured piece
      resign(), if you want to resign
     */

    public Optional<String> getSource() {
        return source;
    }

    public Optional<String> getDestination() {
        return destination;
    }

    public Optional<PieceType> getReplacement() {
        return replacement;
    }

    public int getScore() {
        return score;
    }

    private Move(String source, String destination, PieceType replacement) {
        this.source = Optional.ofNullable(source);
        this.destination = Optional.ofNullable(destination);
        this.replacement = Optional.ofNullable(replacement);
        this.score = 0;
    }

    private Move(String source, String destination, PieceType replacement, int score) {
        this.source = Optional.ofNullable(source);
        this.destination = Optional.ofNullable(destination);
        this.replacement = Optional.ofNullable(replacement);
        this.score = score;
    }

    public int getSourceX() {
        return this.source.get().charAt(1) - '0';
    }

    public int getSourceY() {
        return this.source.get().charAt(0) - 'a' + 1;
    }

    public int getDestinationX() {
        return this.destination.get().charAt(1) - '0';
    }

    public int getDestinationY() {
        return this.destination.get().charAt(0) - 'a' + 1;
    }

    /**
     * Checks whether the move is an usual move/capture
     * @return true if move is NOT a drop-in or promotion, false otherwise
     */
    public boolean isNormal() {
        return source.isPresent() && destination.isPresent() && replacement.isEmpty();
    }

    /**
     * Check whether move is a promotion (pawn advancing to last row)
     * @return true if move is a promotion (promotion field set and source is not null)
     */
    public boolean isPromotion() {
        return source.isPresent() && destination.isPresent() && replacement.isPresent();
    }

    /**
     * Check whether the move is a crazyhouse drop-in (place a captured enemy piece
     * to fight on your side)
     */
    public boolean isDropIn() {
        return source.isEmpty() && destination.isPresent() && replacement.isPresent();
    }

    public boolean isCastle() {
        return source.isPresent() && destination.isPresent() && (Math.abs(getDestinationY() - getSourceY()) == 2);
    }

    /**
     * Emit a move from src to dst. Validity is to be checked by engine (your implementation)
     * Positions are encoded as stated at beginning of file
     * Castles are encoded as follows:
     * source: position of king
     * destination: final position of king (two tiles away)
     * @param source initial tile
     * @param destination destination tile
     * @return move to be sent to board
     */
    public static Move moveTo(String source, String destination) {
        return new Move(source, destination, null);
    }

    /**
     * Emit a promotion move. Validity is to be checked by engine
     * (i.e. source contains a pawn in second to last row, etc.)
     * @param source initial tile of pawn
     * @param destination next tile (could be diagonal if also capturing)
     * @param replacement piece to promote to (must not be pawn or king)
     * @return move to be sent to board
     */
    public static Move promote(String source, String destination, PieceType replacement) {
        return new Move(source, destination, replacement);
    }

    /**
     * Emit a drop-in (Crazyhouse specific move where player summons
     * a captured piece onto a free tile. Pawns can not be dropped in first and last rows)
     * @param destination
     * @param replacement
     * @return
     */
    public static Move dropIn(String destination, PieceType replacement) {
        return new Move(null, destination, replacement);
    }

    public static Move resign() {
        return new Move(null, null, null);
    }

    public static String serializeMove(Move move) {
        if (move.isNormal())
            return move.getSource().orElse("") + move.getDestination().orElse("");
        else if (move.isPromotion() && move.getReplacement().isPresent()) {
            String pieceCode = switch (move.getReplacement().get()) {
                case BISHOP -> "b";
                case KNIGHT -> "n";
                case ROOK -> "r";
                case QUEEN -> "q";
                default -> "";
            };
            return move.getSource().orElse("") + move.getDestination().orElse("") + pieceCode;
        } else if (move.isDropIn() && move.getReplacement().isPresent()) {
            String pieceCode = switch (move.getReplacement().get()) {
                case BISHOP -> "B";
                case KNIGHT -> "N";
                case ROOK -> "R";
                case QUEEN -> "Q";
                case PAWN -> "P";
                default -> "";
            };
            return pieceCode + "@" + move.getDestination();
        } else {
            return "resign";
        }
    }

    public static Move deserializeMove(String s) {
        if (s.charAt(1) == '@') {
            /* Drop-in */

            PieceType piece = switch (s.charAt(0)) {
                case 'P' -> PieceType.PAWN;
                case 'R' -> PieceType.ROOK;
                case 'B' -> PieceType.BISHOP;
                case 'N' -> PieceType.KNIGHT;
                case 'Q' -> PieceType.QUEEN;
                case 'K' -> PieceType.KING; /* This is an illegal move */
                default -> null;
            };

            return Move.dropIn(s.substring(2, 4), piece);
        } else if (s.length() == 5) {
            /* Pawn promotion */
            PieceType piece = switch (s.charAt(4)) {
                case 'p' -> PieceType.PAWN; /* This is an illegal move */
                case 'r' -> PieceType.ROOK;
                case 'b' -> PieceType.BISHOP;
                case 'n' -> PieceType.KNIGHT;
                case 'q' -> PieceType.QUEEN;
                case 'k' -> PieceType.KING; /* This is an illegal move */
                default -> null;
            };

            return Move.promote(s.substring(0, 2), s.substring(2, 4), piece);
        }

        /* Normal move/capture/castle/en passant */
        return Move.moveTo(s.substring(0, 2), s.substring(2, 4));
    }
}
