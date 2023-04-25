package pieces;

import main.Move;
import main.PieceType;

public class Pawn extends Piece {
	public Pawn(boolean isMine, PieceType type, int x, int y) {
		super(isMine, type, x, y);
	}

	@Override
	public boolean canMove(Piece[][] board, int xDest, int yDest) {
		if (xDest < 1 || xDest > 8 || yDest < 1 || yDest > 8) return false;
		int verticalDist = xDest - x;
		int horizontalDist = yDest - y;

		// Move 2 squares if it's the first move
//		if (x == 2 && verticalDist != 1 && verticalDist != 2) return false;

		// Move 1 square if it's not the first move
		if (verticalDist != 1) {
			System.out.println("Pawn can only move forward square verticalDist");
			return false;
		}
		// Cant move horizontally
		if (Math.abs(horizontalDist) > 1) {
			System.out.println("Pawn can only move forward square horizontalDist");
			return false;
		}
		// Can move only if there is no piece in the destination
		if (board[xDest][yDest] != null) {
			System.out.println("Pawn can only move forward square board[xDest][yDest]");
			return false;
		}

		return board[xDest][yDest] == null;
	}

	@Override
	boolean canCapture(Piece[][] board, int xDest, int yDest) {
		if (xDest < 1 || xDest > 9 || yDest < 1 || yDest > 9) return false;
		int verticalDist = xDest - x;
		int horizontalDist = yDest - y;
		// Can only capture if it's moving 1 square diagonally
		if (Math.abs(verticalDist) != 1) return false;
		if (Math.abs(horizontalDist) != 1) return false;
		// Can only capture if there is a piece in the destination
		return board[xDest][yDest] != null;
	}

	@Override
	public Move suggestRandomMove(Piece[][] board) {
		if (!canMove(board, x + 1, y)) return null;
		String src = getSrcString();
		String dst = getDstString(x + 1, y);
		x = x + 1;
		return Move.moveTo(src, dst);
	}
}
