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
		int verticalDist = yDest - y;
		int horizontalDist = xDest - x;

		// main.Move 2 squares if it's the first move
		if (y == 2 && verticalDist != 1 && verticalDist != 2) return false;
		// main.Move 1 square if it's not the first move
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
		if (yDest - y != 1 && yDest - y != -1) return false;
		if (Math.abs(xDest - x) != 1) return false;
		// Can only capture if there is a piece in the destination
		return board[xDest][yDest] != null;
	}

	@Override
	public Move suggestRandomMove(Piece[][] board) {
		System.out.println(">>>>>>> " + x + " " + y);
		if (canMove(board, x, y + 1)) {
			System.out.println("Pawn case 1: " + x + " " + y);
			return Move.moveTo(getSrcString(), getDstString(0, 1));
		} else {
			System.out.println("Case 1 not possible");
		}

		if (canMove(board, x, y + 2)) {
			System.out.println("Pawn case 2: " + x + " " + y);
			return Move.moveTo(getSrcString(), getDstString(0, 2));
		} else {
			System.out.println("Case 2 not possible");
		}

		if (canCapture(board, x + 1, y + 1)) {
			System.out.println("Pawn case 3: " + x + " " + y);
			return Move.moveTo(getSrcString(), getDstString(1, 1));
		} else {
			System.out.println("Case 3 not possible");
		}

		if (canCapture(board, x + 1, y - 1)) {
			System.out.println("Pawn case 4: " + x + " " + y);
			return Move.moveTo(getSrcString(), getDstString(1, -1));
		} else {
			System.out.println("Case 4 not possible");
		}

		return null;
	}
}
