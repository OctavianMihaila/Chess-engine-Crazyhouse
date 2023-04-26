package pieces;

import main.Move;
import main.PieceType;
import main.PlaySide;

import java.util.ArrayList;

public class Bishop extends Piece {
	public static final int[][] moveDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

	public Bishop(PlaySide side, PieceType type, int x, int y) {
		super(side, type, x, y);
	}

	@Override
	public boolean clearPath(Piece[][] board, int xDest, int yDest) {
		if (!validMove(board, xDest, yDest)) return false;

		int verticalDist = xDest - x;
		int horizontalDist = yDest - y;
		int xDir = verticalDist > 0 ? 1 : -1;
		int yDir = horizontalDist > 0 ? 1 : -1;

		for (int i = 1; i < Math.abs(verticalDist); i++) {
			if (board[x + i * xDir][y + i * yDir] != null) return false;
		}

		return true;
	}

	@Override
	public boolean validMove(Piece[][] board, int xDest, int yDest) {
		if (!onTable(xDest, yDest)) return false;

		int verticalDist = xDest - x;
		int horizontalDist = yDest - y;

		// Cant move if it's not moving diagonally or if it's not moving at all
		return Math.abs(verticalDist) == Math.abs(horizontalDist) && verticalDist != 0;
	}

	@Override
	public int[][] getMoveDirections() {
		return moveDirections;
	}

	@Override
	public int[][] getCaptureDirections() {
		return moveDirections;
	}

	@Override
	public int getMaxMoves() {
		return 7;
	}
}
