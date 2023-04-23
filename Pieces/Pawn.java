package Pieces;

public class Pawn extends Piece {
	public Pawn(boolean isMine, PieceType type, int x, int y) {
		super(isMine, type, x, y);
	}

	@Override
	boolean canMove(Piece[][] board, int xDest, int yDest) {
		if (xDest < 1 || xDest > 9 || yDest < 1 || yDest > 9) return false;
		int verticalDist = yDest - y;
		int horizontalDist = xDest - x;

		// Move 2 squares if it's the first move
		if (y == 2 && verticalDist != 1 && verticalDist != 2) return false;
		// Move 1 square if it's not the first move
		if (verticalDist != 1) return false;
		// Cant move horizontally
		if (Math.abs(horizontalDist) > 1) return false;
		// Can move only if there is no piece in the destination
		return board[xDest][yDest] == null;
	}

	@Override
	boolean canCapture(Piece[][] board, int xDest, int yDest) {
		if (xDest < 1 || xDest > 9 || yDest < 1 || yDest > 9) return false;
		// Can only capture if it's moving 1 square diagonally forward
		if (yDest - y != 1) return false;
		if (Math.abs(xDest - x) != 1) return false;
		// Can only capture if there is a piece in the destination
		return board[xDest][yDest] != null;
	}

	@Override
	public Move suggestRandomMove(Piece[][] board, String source) {
		return null;
	}
}
