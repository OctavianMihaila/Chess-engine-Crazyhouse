package Pieces;

public class Rook extends Piece {
	public Rook(boolean isMine, PieceType type, int x, int y) {
		super(isMine, type, x, y);
	}

	@Override
	public boolean canMove(Piece[][] board, int xDest, int yDest) {
		if (xDest < 1 || xDest > 9 || yDest < 1 || yDest > 9) return false;
		int verticalDist = yDest - y;
		int horizontalDist = xDest - x;

		// Cant move if it's not moving horizontally or vertically
		if (verticalDist != 0 && horizontalDist != 0) return false;
		// Cant move if it's not moving
		if (verticalDist == 0 && horizontalDist == 0) return false;
		// Cant move if there is a piece in the destination
		if (board[xDest][yDest] != null) return false;

		// Check if there is a piece in the way
		int xDir = (int) Math.signum(horizontalDist);
		int yDir = (int) Math.signum(verticalDist);
		for (int i = 1; i < Math.abs(verticalDist) + Math.abs(horizontalDist); i++) {
			if (board[x + i * xDir][y + i * yDir] != null) return false;
		}

		return true;
	}

	@Override
	boolean canCapture(Piece[][] board, int xDest, int yDest) {
		if (xDest < 1 || xDest > 9 || yDest < 1 || yDest > 9) return false;
		int verticalDist = yDest - y;
		int horizontalDist = xDest - x;

		// Cant capture if it's not moving horizontally or vertically
		if (verticalDist != 0 && horizontalDist != 0) return false;
		// Cant capture if it's not moving
		if (verticalDist == 0 && horizontalDist == 0) return false;
		// Cant capture if there is no piece in the destination
		if (board[xDest][yDest] == null) return false;

		// Check if there is a piece in the way
		int xDir = (int) Math.signum(horizontalDist);
		int yDir = (int) Math.signum(verticalDist);
		for (int i = 1; i < Math.abs(verticalDist) + Math.abs(horizontalDist); i++) {
			if (board[x + i * xDir][y + i * yDir] != null) return false;
		}

		return true;
	}

	@Override
	public Move suggestRandomMove(Piece[][] board) {
		return null;
	}
}
