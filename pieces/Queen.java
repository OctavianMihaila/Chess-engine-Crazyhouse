package pieces;

import main.Move;
import main.PieceType;
import main.PlaySide;

import java.util.ArrayList;

public class Queen extends Piece {

	public Queen(PlaySide side, PieceType type, int x, int y) {
		super(side, type, x, y);
	}

	@Override
	public boolean canMove(Piece[][] board, int xDest, int yDest) {
		if (xDest < 1 || xDest > 8 || yDest < 1 || yDest > 8) return false;
		int verticalDist = xDest - x;
		int horizontalDist = yDest - y;

		// Cant move if it's not moving horizontally or vertically
		if (verticalDist != 0 && horizontalDist != 0) return false;
		// Cant move if it's not moving
		if (verticalDist == 0 && horizontalDist == 0) return false;
		// Cant move if there is a piece in the destination
		if (board[xDest][yDest] != null) return false;

		if (verticalDist != 0) {
			int yDir = (int) Math.signum(verticalDist);
			for (int i = 1; i <= Math.abs(verticalDist); i++) {
				if (board[x + i * yDir][y] != null) return false;
			}
		} else {
			int xDir = (int) Math.signum(horizontalDist);
			for (int i = 1; i <= Math.abs(horizontalDist); i++) {
				if (board[x][y + i * xDir] != null) return false;
			}
		}

		return true;
	}

	@Override
	public boolean canCapture(Piece[][] board, int xDest, int yDest) {
		if (xDest < 1 || xDest > 8 || yDest < 1 || yDest > 8) return false;
		int verticalDist = xDest - x;
		int horizontalDist = yDest - y;

		// Cant capture if it's not moving horizontally or vertically
		if (verticalDist != 0 && horizontalDist != 0) return false;
		// Cant capture if it's not moving
		if (verticalDist == 0 && horizontalDist == 0) return false;
		// Cant capture if there is no piece in the destination
		if (board[xDest][yDest] == null) return false;

		if (verticalDist != 0) {
			int yDir = (int) Math.signum(verticalDist);
			for (int i = 1; i <= Math.abs(verticalDist); i++) {
				if (board[x + i * yDir][y] != null) return false;
			}
		} else {
			int xDir = (int) Math.signum(horizontalDist);
			for (int i = 1; i <= Math.abs(horizontalDist); i++) {
				if (board[x][y + i * xDir] != null) return false;
			}
		}
		return true;
	}

	@Override
	public ArrayList<Move> suggestPossibleMoves(Piece[][] board) {
		ArrayList<Move> moves = new ArrayList<>();
		for (int i = 1; i <= 8; i++) {
			if (canCapture(board, x + i, y) || canMove(board, x + i, y)) {
				moves.add(Move.moveTo(getSrcString(), getDstString(x + i, y)));
			}
			if (canCapture(board, x - i, y) || canMove(board, x - i, y)) {
				moves.add(Move.moveTo(getSrcString(), getDstString(x - i, y)));
			}
			if (canCapture(board, x, y + i) || canMove(board, x, y + i)) {
				moves.add(Move.moveTo(getSrcString(), getDstString(x, y + i)));
			}
			if (canCapture(board, x, y - i) || canMove(board, x , y - i)) {
				moves.add(Move.moveTo(getSrcString(), getDstString(x, y - i)));
			}
		}

		return moves;
	}
}
