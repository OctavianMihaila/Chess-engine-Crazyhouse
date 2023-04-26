package pieces;

import main.Move;
import main.PieceType;
import main.PlaySide;

import java.util.ArrayList;

public class Knight extends Piece {
	public static final int[][] moveDirections = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {-1, 2}, {1, -2}, {-1, -2}};
	public Knight(PlaySide side, PieceType type, int x, int y) {
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

		// Cant move if it's not moving 2 squares vertically and 1 square horizontally
		if (Math.abs(verticalDist) == 2 && Math.abs(horizontalDist) != 1) return false;

		// Cant move if it's not moving 1 square vertically and 2 squares horizontally
		return Math.abs(verticalDist) != 1 || Math.abs(horizontalDist) == 2;
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
