package pieces;

import main.Board;
import main.PieceType;
import main.PlaySide;

public class Rook extends Piece {
	public static final int[][] moveDirections = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

	public Rook(PlaySide side, PieceType type, int x, int y) {
		super(side, type, x, y);
	}

	@Override
	public boolean clearPath(Board board, int xDest, int yDest) {
		int verticalDist = xDest - x;
		int horizontalDist = yDest - y;

		// Check vertical path or horizontal path
		if (verticalDist != 0) {
			int xDir = (int) Math.signum(verticalDist);
			for (int i = 1; i < Math.abs(verticalDist); i++) {
				if (board.getPiece(x + i * xDir, y) != null) return false;
			}
		} else {
			int yDir = (int) Math.signum(horizontalDist);
			for (int i = 1; i < Math.abs(horizontalDist); i++) {
				if (board.getPiece(x, y + i * yDir) != null) return false;
			}
		}

		return true;
	}

	@Override
	public boolean validMove(Board board, int xDest, int yDest) {
		if (!onTable(xDest, yDest)) return false;

		int verticalDist = xDest - x;
		int horizontalDist = yDest - y;

		// Cant move if it's not moving horizontally or vertically
		if (verticalDist != 0 && horizontalDist != 0) return false;
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
		return 7;
	}
}
