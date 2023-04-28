package pieces;

import main.Board;
import main.PieceType;
import main.PlaySide;

public class Bishop extends Piece {
	public static final int[][] moveDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

	public Bishop(PlaySide side, int x, int y) {
		super(side, PieceType.BISHOP, x, y);
	}

	@Override
	public boolean validMove(Board board, int xDest, int yDest) {
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
