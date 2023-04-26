package pieces;

import main.*;

import java.util.ArrayList;

public class Pawn extends Piece {
	public Pawn(PlaySide side, PieceType type, int x, int y) {
		super(side, type, x, y);
	}

	@Override
	public boolean canMove(Piece[][] board, int xDest, int yDest) {
		if (xDest < 1 || xDest > 8 || yDest < 1 || yDest > 8) return false;
		int horizontalDist = yDest - y;
		int verticalDist = xDest - x;

		// Cant move horizontally
		if (horizontalDist != 0) return false;

		// If it can do enpesant

		if (x == 2) {
			if (verticalDist < 0 || verticalDist > 2) return false;
			return board[x+1][y] == null && board[x+2][y] == null;
		}

		// Can move only forward 1 square
		if (verticalDist != 1) return false;

		// Can only move if there is no piece at destination
		return board[xDest][yDest] == null;
	}

	@Override
	public boolean canCapture(Piece[][] board, int xDest, int yDest) {
		if (xDest < 1 || xDest > 8 || yDest < 1 || yDest > 8) return false;
		int verticalDist = xDest - x;
		int horizontalDist = yDest - y;
		// Can only capture if it's moving 1 square diagonally
		if (verticalDist != 1) return false;
		if (Math.abs(horizontalDist) != 1) return false;

		// Can only capture if there is a piece in the destination
		return board[xDest][yDest] != null && board[xDest][yDest].side != side;
	}

	@Override
	public ArrayList<Move> suggestPossibleMoves(Piece[][] board) {
		ArrayList<Move> moves = new ArrayList<>();
		if (canMove(board, x + 1, y)) moves.add(Move.moveTo(getSrcString(), getDstString(x + 1, y)));
		if (canMove(board, x + 2, y)) moves.add(Move.moveTo(getSrcString(), getDstString(x + 2, y)));

		if (canCapture(board, x + 1, y + 1)) moves.add(Move.moveTo(getSrcString(), getDstString(x + 1, y + 1)));
		if (canCapture(board, x + 1, y - 1)) moves.add(Move.moveTo(getSrcString(), getDstString(x + 1, y - 1)));
		return moves;
	}

	@Override
	public String toString() {
		return "Pawn at " + getSrcString();
	}
}
