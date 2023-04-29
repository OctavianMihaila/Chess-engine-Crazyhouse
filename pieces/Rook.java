package pieces;

import main.Board;
import main.PieceType;
import main.PlaySide;

public class Rook extends Piece {
	public static final int[][] moveDirections = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

	public boolean moved;

	public Rook(PlaySide side, int x, int y) {
		super(side, PieceType.ROOK, x, y, 2);
		this.moved = false;
	}

	public void setMoved(boolean moved) {
		this.moved = moved;
	}

	public boolean isMoved() {
		return moved;
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
