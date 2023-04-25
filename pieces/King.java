package pieces;

import main.Move;
import main.PieceType;

public class King extends Piece {
	public King(boolean isMine, PieceType type, int x, int y) {
		super(isMine, type, x, y);
	}

	@Override
	public boolean canMove(Piece[][] board, int xDest, int yDest) {
		if (xDest < 1 || xDest > 9 || yDest < 1 || yDest > 9) return false;
		int verticalDist = yDest - y;
		int horizontalDist = xDest - x;

		// Cant move if it's not moving 1 square
		if (Math.abs(verticalDist) > 1 || Math.abs(horizontalDist) > 1) return false;
		// Cant move if it's not moving
		if (verticalDist == 0 && horizontalDist == 0) return false;
		// Cant move if there is a piece in the destination
		return board[xDest][yDest] == null;
	}

	@Override
	boolean canCapture(Piece[][] board, int xDest, int yDest) {
		if (xDest < 1 || xDest > 9 || yDest < 1 || yDest > 9) return false;
		// Can only capture if it's moving 1 square diagonally forward
		if (Math.abs(yDest - y) != 1) return false;
		if (Math.abs(xDest - x) != 1) return false;
		// Can only capture if there is a piece in the destination
		return board[xDest][yDest] != null;
	}

	@Override
	public Move suggestRandomMove(Piece[][] board) {
		return null;
	}
}
