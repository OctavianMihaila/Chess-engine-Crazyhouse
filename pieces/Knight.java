package pieces;

import main.Board;
import main.PieceType;
import main.PlaySide;

public class Knight extends Piece {
	public static final int[][] moveDirections = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {-1, 2}, {1, -2}, {-1, -2}};

	public Knight(PlaySide side, PieceType type, int x, int y) {
		super(side, type, x, y);
	}

	@Override
	public boolean validPath(Board board, int xDest, int yDest) {
		return true;
	}

	@Override
	public boolean validPathIgnoring(Board board, int xDest, int yDest, Piece piece) {
		return true;
	}

	@Override
	public boolean validMove(Board board, int xDest, int yDest) {
		if (!onTable(xDest, yDest)) return false;

		int verticalDist = xDest - x;
		int horizontalDist = yDest - y;

		if ((Math.abs(verticalDist) == 2 && Math.abs(horizontalDist) == 1) || (Math.abs(horizontalDist) == 2 && Math.abs(verticalDist) == 1))
			return true;

		return false;
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
		return 1;
	}
}
