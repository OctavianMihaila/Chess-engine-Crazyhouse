package pieces;

import main.Move;
import main.PieceType;
import main.PlaySide;

import java.util.ArrayList;

public class Queen extends Piece {
	public static final int[][] moveDirections = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

	public Queen(PlaySide side, PieceType type, int x, int y) {
		super(side, type, x, y);
	}

	@Override
	public boolean clearPath(Piece[][] board, int xDest, int yDest) {
		if (!validMove(board, xDest, yDest)) return false;

		int verticalDist = xDest - x;
		int horizontalDist = yDest - y;


		// If it's moving horizontally or vertically
		if (horizontalDist == 0 || verticalDist == 0) {
			int xDir = verticalDist == 0 ? 0 : verticalDist / Math.abs(verticalDist);
			int yDir = horizontalDist == 0 ? 0 : horizontalDist / Math.abs(horizontalDist);
			for (int i = 1; i < Math.abs(verticalDist) + Math.abs(horizontalDist); i++) {
				if (board[x + xDir * i][y + yDir * i] != null) return false;
			}
		} else {
			int xDir = verticalDist / Math.abs(verticalDist);
			int yDir = horizontalDist / Math.abs(horizontalDist);
			for (int i = 1; i < Math.abs(verticalDist); i++) {
				if (board[x + xDir * i][y + yDir * i] != null) return false;
			}
		}

		return true;
	}

	@Override
	public boolean validMove(Piece[][] board, int xDest, int yDest) {
		if (!onTable(xDest, yDest)) return false;
		int verticalDist = xDest - x;
		int horizontalDist = yDest - y;

		// Cant move if it's not moving horizontally or vertically or diagonally
		if (horizontalDist != 0 && verticalDist != 0 && Math.abs(horizontalDist) != Math.abs(verticalDist))
			return false;

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
