package pieces;

import main.Move;
import main.PieceType;
import main.PlaySide;

import java.util.ArrayList;

public class Knight extends Piece {
	public Knight(PlaySide side, PieceType type, int x, int y) {
		super(side, type, x, y);
	}

	@Override
	public boolean canMove(Piece[][] board, int xDest, int yDest) {
		if (xDest < 1 || xDest > 8 || yDest < 1 || yDest > 8) return false;
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
	public boolean canCapture(Piece[][] board, int xDest, int yDest) {
		if (xDest < 1 || xDest > 8 || yDest < 1 || yDest > 8) return false;
		// Cant capture if it's not moving 2 squares vertically and 1 square horizontally
		if (Math.abs(yDest - y) != 2 || Math.abs(xDest - x) != 1) return false;
		// Cant capture if it's not moving
		if (yDest - y == 0) return false;
		// Can only capture if there is a piece in the destination
		return board[xDest][yDest] != null && board[xDest][yDest].side != side;
	}

	@Override
	public ArrayList<Move> suggestPossibleMoves(Piece[][] board) {
		ArrayList<Move> moves = new ArrayList<>();
		if (canCapture(board, x + 2, y + 1) || canMove(board, x + 2, y + 1)) {
			moves.add(Move.moveTo(getSrcString(), getDstString(x + 2, y + 1)));
		}
		if (canCapture(board, x + 2, y - 1) || canMove(board, x + 2, y - 1)) {
			moves.add(Move.moveTo(getSrcString(), getDstString(x + 2, y - 1)));
		}

		if (canCapture(board, x - 2, y + 1) || canMove(board, x - 2, y + 1)) {
			moves.add(Move.moveTo(getSrcString(), getDstString(x - 2, y + 1)));
		}
		if (canCapture(board, x - 2, y - 1) || canMove(board, x - 2, y - 1)) {
			moves.add(Move.moveTo(getSrcString(), getDstString(x - 2, y - 1)));
		}

		if (canCapture(board, x + 1, y + 2) || canMove(board, x + 1, y + 2)) {
			moves.add(Move.moveTo(getSrcString(), getDstString(x + 1, y + 2)));
		}
		if (canCapture(board, x - 1, y + 2) ||  canMove(board, x - 1, y + 2)) {
			moves.add(Move.moveTo(getSrcString(), getDstString(x - 1, y + 2)));
		}

		if (canCapture(board, x + 1, y - 2) ||  canMove(board, x + 1, y - 2)) {
			moves.add(Move.moveTo(getSrcString(), getDstString(x + 1, y - 2)));
		}
		if (canCapture(board, x - 1, y - 2) ||  canMove(board, x - 1, y - 2)) {
			moves.add(Move.moveTo(getSrcString(), getDstString(x - 1, y - 2)));
		}

		return moves;
	}
}
