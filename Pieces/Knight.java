package Pieces;

public class Knight extends Piece {
	public Knight(boolean isMine, PieceType type, int x, int y) {
		super(isMine, type, x, y);
	}

	@Override
	public boolean canMove(Piece[][] board, int xDest, int yDest) {
		if (xDest < 1 || xDest > 9 || yDest < 1 || yDest > 9) return false;
		int verticalDist = yDest - y;
		int horizontalDist = xDest - x;

		// Cant move if it's not moving 2 squares vertically and 1 square horizontally
		if (Math.abs(verticalDist) != 2 || Math.abs(horizontalDist) != 1) return false;
		// Cant move if it's not moving
		if (verticalDist == 0) return false;
		// Cant move if there is a piece in the destination
		return board[xDest][yDest] == null;
	}

	@Override
	boolean canCapture(Piece[][] board, int xDest, int yDest) {
		if (xDest < 1 || xDest > 9 || yDest < 1 || yDest > 9) return false;
		// Cant capture if it's not moving 2 squares vertically and 1 square horizontally
		if (Math.abs(yDest - y) != 2 || Math.abs(xDest - x) != 1) return false;
		// Cant capture if it's not moving
		if (yDest - y == 0) return false;
		// Can only capture if there is a piece in the destination
		return board[xDest][yDest] != null;
	}

	@Override
	public Move suggestRandomMove(Piece[][] board) {
		return null;
	}
}
