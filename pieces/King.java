package pieces;

import main.Move;
import main.PieceType;
import main.PlaySide;

import java.util.ArrayList;

public class King extends Piece {
	public static final int[][] moveDirections = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

	public King(PlaySide side, PieceType type, int x, int y) {
		super(side, type, x, y);
	}

	@Override
	public boolean clearPath(Piece[][] board, int xDest, int yDest) {
		return validMove(board, xDest, yDest);
	}

	@Override
	public boolean validMove(Piece[][] board, int xDest, int yDest) {
		if (!onTable(xDest, yDest)) return false;
		int verticalDist = xDest - x;
		int horizontalDist = yDest - y;

		// Cant move if trying to move more than 2 in any direction
		if (Math.abs(verticalDist) > 1 && Math.abs(horizontalDist) > 1) return false;
		// Cant move if it's not moving
		return verticalDist == 0 || horizontalDist == 0;
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
