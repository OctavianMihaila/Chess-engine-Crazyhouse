package pieces;

import main.Board;
import main.PieceType;
import main.PlaySide;

public class King extends Piece {
	private boolean moved = false;
	public static final int[][] moveDirections = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

	public King(PlaySide side, int x, int y) {
		super(side, PieceType.KING, x, y, 10);
	}

	@Override
	public void updatePosition(int x, int y) {
		moved = true;
		super.updatePosition(x, y);
	}

	@Override
	public boolean validMove(Board board, int xDest, int yDest) {
		if (!onTable(xDest, yDest)) return false;

		int verticalDist = xDest - x;
		int horizontalDist = yDest - y;

		// Cant move if trying to move more than 2 in any direction
		if (Math.abs(verticalDist) > 1 || Math.abs(horizontalDist) > 1) return false;

		// Cant move if it's not moving
		return verticalDist != 0 || horizontalDist != 0;
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
