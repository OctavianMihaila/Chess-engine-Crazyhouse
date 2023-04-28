package pieces;

import main.Board;
import main.PieceType;
import main.PlaySide;

public class Queen extends Piece {
	public static final int[][] moveDirections = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

	public Queen(PlaySide side, PieceType type, int x, int y) {
		super(side, type, x, y);
	}

	@Override
	public boolean clearPath(Board board, int xDest, int yDest) {
		int verticalDist = xDest - x;
		int horizontalDist = yDest - y;

		// Cant move if there are pieces in the path
		if (horizontalDist == 0) {
			int direction = verticalDist > 0 ? 1 : -1;
			for (int i = 1; i < Math.abs(verticalDist); i++) {
				Piece piece = board.getPiece(x + i * direction, y);
				if (piece != null && !(piece instanceof King)) return false;
			}
		} else if (verticalDist == 0) {
			int direction = horizontalDist > 0 ? 1 : -1;
			for (int i = 1; i < Math.abs(horizontalDist); i++) {
				Piece piece = board.getPiece(x, y + i * direction);
				if (piece != null && !(piece instanceof King)) return false;
			}
		} else {
			int verticalDirection = verticalDist > 0 ? 1 : -1;
			int horizontalDirection = horizontalDist > 0 ? 1 : -1;
			for (int i = 1; i < Math.abs(horizontalDist); i++) {
				Piece piece = board.getPiece(x + i * verticalDirection, y + i * horizontalDirection);
				if (piece != null && !(piece instanceof King)) return false;
			}
		}

		return true;
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
