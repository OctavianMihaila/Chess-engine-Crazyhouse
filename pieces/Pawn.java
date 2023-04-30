package pieces;

import main.*;

public class Pawn extends Piece {
	public static final int[][] whiteDirections = {{1, 0}, {2, 0}};
	public static final int[][] blackDirections = {{-1, 0}, {-2, 0}};
	public static final int[][] whiteCaptureDirections = {{1, 1}, {1, -1}};
	public static final int[][] blackCaptureDirections = {{-1, 1}, {-1, -1}};

	public Pawn(PlaySide side, int x, int y) {
		super(side, PieceType.PAWN, x, y, 1);
	}

	@Override
	public boolean validPath(Board board, int xDest, int yDest) {
		if (x == 2 && side == PlaySide.WHITE) return board.getPiece(x + 1, y) == null;
		if (x == 7 && side == PlaySide.BLACK) return board.getPiece(x - 1, y) == null;
		return true;
	}

	@Override
	public boolean validMove(Board board, int xDest, int yDest) {
		if (!onTable(xDest, yDest)) return false;

		int horizontalDist = yDest - y;
		int verticalDist = xDest - x;

		// Cant move more than 1 square horizontally
		if (Math.abs(horizontalDist) > 0) return false;
		if (Math.abs(verticalDist) > 2) return false;

		if (side == PlaySide.WHITE) {
			if (verticalDist <= 0) return false;
			if (verticalDist == 2 && x != 2) return false;
		} else {
			if (verticalDist >= 0) return false;
			if (verticalDist == -2 && x != -2) return false;
		}

		return true;
	}

	@Override
	public boolean validCapture(Board board, int xDest, int yDest) {
		if (!onTable(xDest, yDest)) return false;

		int horizontalDist = yDest - y;
		int verticalDist = xDest - x;

		// Capture can only be made in diagonal
		if (Math.abs(horizontalDist) != 1) return false;

		return (side == PlaySide.BLACK && verticalDist == -1) || (side == PlaySide.WHITE && verticalDist == 1);
	}

	@Override
	public int[][] getMoveDirections() {
		if (side == PlaySide.WHITE) return whiteDirections;
		return blackDirections;
	}

	@Override
	public int[][] getCaptureDirections() {
		if (side == PlaySide.WHITE) return whiteCaptureDirections;
		return blackCaptureDirections;
	}

	@Override
	public int getMaxMoves() {
		return 1;
	}
}
