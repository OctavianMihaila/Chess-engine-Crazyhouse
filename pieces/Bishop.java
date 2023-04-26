package pieces;

import main.Move;
import main.PieceType;
import main.PlaySide;

import java.util.ArrayList;

public class Bishop extends Piece {
	public Bishop(PlaySide side, PieceType type, int x, int y) {
		super(side, type, x, y);
	}

	@Override
	public boolean canMove(Piece[][] board, int xDest, int yDest) {
		if (xDest < 1 || xDest > 8 || yDest < 1 || yDest > 8) return false;
		int verticalDist = xDest - x;
		int horizontalDist = yDest - y;

		// Cant move if it's not moving diagonally
		if (Math.abs(verticalDist) != Math.abs(horizontalDist)) return false;
		// Cant move if it's not moving
		if (verticalDist == 0) return false;
		// Cant move if there is a piece in the destination
		if (board[xDest][yDest] != null) return false;

		int xDir = verticalDist > 0 ? 1 : -1;
		int yDir = horizontalDist > 0 ? 1 : -1;
		for (int i = 1; i <= Math.abs(verticalDist); i++) {
			if (board[x + i * xDir][y + i * yDir] != null) return false;
		}

		return true;
	}

	@Override
	public boolean canCapture(Piece[][] board, int xDest, int yDest) {
		if (xDest < 1 || xDest > 8 || yDest < 1 || yDest > 8) return false;
		int verticalDist = xDest - x;
		int horizontalDist = yDest - y;

		// Cant capture if it's not moving diagonally
		if (Math.abs(verticalDist) != Math.abs(horizontalDist)) return false;
		// Cant capture if it's not moving
		if (verticalDist == 0) return false;
		// Cant capture if there is no piece in the destination
		if (board[xDest][yDest] == null) return false;

		// Cant capture if the piece is same side
		if (board[xDest][yDest].side == side) return false;

		// Check if there is a piece in the way
		int xDir = verticalDist > 0 ? 1 : -1;
		int yDir = horizontalDist > 0 ? 1 : -1;
		for (int i = 1; i <= Math.abs(verticalDist); i++) {
			if (board[x + i * xDir][y + i * yDir] != null) return false;
		}

		return true;
	}

	@Override
	public ArrayList<Move> suggestPossibleMoves(Piece[][] board) {
		ArrayList<Move> moves = new ArrayList<>();
		for (int i = 1; i <= 8; i++) {
			if (canCapture(board, x + i, y + i) || canMove(board, x + i, y + i)) {
				moves.add(Move.moveTo(getSrcString(), getDstString(x + i, y + i)));
			}
			if (canCapture(board, x + i, y - i) || canMove(board, x + i, y - i)) {
				moves.add(Move.moveTo(getSrcString(), getDstString(x + i, y - i)));
			}
			if (canCapture(board, x - i, y + i) || canMove(board, x - i, y + i)) {
				moves.add(Move.moveTo(getSrcString(), getDstString(x - i, y + i)));
			}
			if (canCapture(board, x - i, y - i) || canMove(board, x - i, y - i)) {
				moves.add(Move.moveTo(getSrcString(), getDstString(x - i, y - i)));
			}
		}

		return moves;
	}
}
