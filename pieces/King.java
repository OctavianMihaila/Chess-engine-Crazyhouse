package pieces;

import main.Move;
import main.PieceType;
import main.PlaySide;

import java.util.ArrayList;

public class King extends Piece {
	public King(PlaySide side, PieceType type, int x, int y) {
		super(side, type, x, y);
	}

	@Override
	public boolean canMove(Piece[][] board, int xDest, int yDest) {
		if (xDest < 1 || xDest > 8 || yDest < 1 || yDest > 8) return false;
		int verticalDist = xDest - x;
		int horizontalDist = yDest - y;

		// Cant move if it's not moving 1 square
		if (Math.abs(verticalDist) > 1 || Math.abs(horizontalDist) > 1) return false;
		// Cant move if it's not moving
		if (verticalDist == 0 && horizontalDist == 0) return false;
		// Cant move if there is a piece in the destination
		return board[xDest][yDest] == null;
	}

	@Override
	public boolean canCapture(Piece[][] board, int xDest, int yDest) {
		if (xDest < 1 || xDest > 8 || yDest < 1 || yDest > 8) return false;
		// Can only capture if it's moving 1 square diagonally forward
		if (Math.abs(yDest - y) != 1) return false;
		if (Math.abs(xDest - x) != 1) return false;
		// Can only capture if there is a piece in the destination
		return board[xDest][yDest] != null && board[xDest][yDest].side != side;
	}

	@Override
	public ArrayList<Move> suggestPossibleMoves(Piece[][] board) {
		ArrayList<Move> moves = new ArrayList<>();
		if (canCapture(board, x + 1, y) || canMove(board, x + 1, y)) {
			moves.add(Move.moveTo(getSrcString(), getDstString(x + 1, y)));
		}

		if (canCapture(board, x - 1, y) || canMove(board, x - 1, y)) {
			moves.add(Move.moveTo(getSrcString(), getDstString(x - 1, y)));
		}

		if (canCapture(board, x, y + 1) || canMove(board, x, y + 1)) {
			moves.add(Move.moveTo(getSrcString(), getDstString(x, y + 1)));
		}

		if (canCapture(board, x, y - 1) || canMove(board, x, y - 1)) {
			moves.add(Move.moveTo(getSrcString(), getDstString(x, y - 1)));
		}

		if (canCapture(board, x + 1, y + 1) || canMove(board, x + 1, y + 1)) {
			moves.add(Move.moveTo(getSrcString(), getDstString(x + 1, y + 1)));
		}

		if (canCapture(board, x + 1, y - 1) || canMove(board, x + 1, y - 1)) {
			moves.add(Move.moveTo(getSrcString(), getDstString(x + 1, y - 1)));
		}

		if (canCapture(board, x - 1, y + 1) || canMove(board, x - 1, y + 1)) {
			moves.add(Move.moveTo(getSrcString(), getDstString(x - 1, y + 1)));
		}

		if (canCapture(board, x - 1, y - 1) || canMove(board, x - 1, y - 1)) {
			moves.add(Move.moveTo(getSrcString(), getDstString(x - 1, y - 1)));
		}


		return moves;
	}
}
