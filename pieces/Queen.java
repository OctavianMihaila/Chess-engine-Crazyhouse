package pieces;

import main.Board;
import main.PieceType;
import main.PlaySide;

public class Queen extends Piece {
	public static final int[][] moveDirections = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

	public Queen(PlaySide side, int x, int y) {
		super(side, PieceType.QUEEN, x, y);
	}

	@Override
	public boolean validMove(Board board, int xDest, int yDest) {
		if (!onTable(xDest, yDest)) return false;

		int verticalDist = xDest - x;
		int horizontalDist = yDest - y;

		// Cant move if it's not moving horizontally or vertically or diagonally
		if (horizontalDist != 0 && verticalDist != 0 && Math.abs(horizontalDist) != Math.abs(verticalDist)) return false;

		// Cant move if it's not moving
		return horizontalDist != 0 || verticalDist != 0;
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
