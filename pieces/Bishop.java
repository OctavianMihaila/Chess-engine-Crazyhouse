package pieces;

import main.Board;
import main.PieceType;
import main.PlaySide;

public class Bishop extends Piece {
	public static final int[][] moveDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

	public Bishop(PlaySide side, PieceType type, int x, int y) {
		super(side, type, x, y);
	}

	@Override
	public boolean clearPath(Board board, int xDest, int yDest) {
		int verticalDist = xDest - x;
		int horizontalDist = yDest - y;
		int xDir = (int) Math.signum(verticalDist);
		int yDir = (int) Math.signum(horizontalDist);

		for (int i = 1; i < Math.abs(verticalDist); i++) {
			if (board.getPiece(x + i * xDir, y + i * yDir) != null) return false;
		}

		return true;
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
