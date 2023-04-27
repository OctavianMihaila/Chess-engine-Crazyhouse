package pieces;

import main.*;

public class Pawn extends Piece {
	public static final int[][] moveDirections = {{1, 0}, {2, 0}};
	public static final int[][] captureDirections = {{1, 1}, {1, -1}};

	public Pawn(PlaySide side, PieceType type, int x, int y) {
		super(side, type, x, y);
	}

	@Override
	public boolean clearPath(Piece[][] board, int xDest, int yDest) {
		if (!validMove(board, xDest, yDest)) return false;
		if (x == 2) return board[x + 1][y] == null;
		return true;
	}

	@Override
	public boolean validMove(Piece[][] board, int xDest, int yDest) {
		if (!onTable(xDest, yDest)) return false;

		int horizontalDist = yDest - y;
		int verticalDist = xDest - x;

		// Cant move more than 1 square horizontally
		if (Math.abs(horizontalDist) > 1) return false;

		// Cant capture horizontally in other directions
		if (Math.abs(horizontalDist) == 1 && verticalDist != 1) return false;

		// Can move 2 squares only if moving from starting position
		if (x == 2 && (verticalDist < 0 || verticalDist > 2)) return false;

		// Can move only forward 1 square or 2 if moving from starting position
		return verticalDist == 1 || (x == 2 && verticalDist == 2);
	}

	public Move performEnPassant(Piece[][] board, int xDestLastMove, int yDestLastMove, String sourceNewMove, String destinationNewMove) {
		// Moving pawn to do en passant and removing the enemy's pawn.
		board[xDestLastMove][yDestLastMove] = null;
		return Move.moveTo(sourceNewMove, destinationNewMove);
	}


	@Override
	public int[][] getMoveDirections() {
		return moveDirections;
	}

	@Override
	public int[][] getCaptureDirections() {
		return captureDirections;
	}

	@Override
	public int getMaxMoves() {
		return 1;
	}
}
