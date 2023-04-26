package pieces;

import main.Move;
import main.PieceType;
import main.PlaySide;

import java.util.ArrayList;

public abstract class Piece {
	public static final int[][] moveDirections = {{0}};
	public static final int maxMoves = 1;
	public boolean captured = false;
	public PlaySide side;
	private final PieceType type;
	public int x;
	public int y;

	public Piece(PlaySide side, PieceType type, int x, int y) {
		this.side = side;
		this.type = type;
		this.x = x;
		this.y = y;
	}

	public PieceType getType() {
		return type;
	}

	public String getSrcString() {
		return Character.toString((char) ('a' + y - 1)) + x;
	}

	public static String getDstString(int x, int y) {
		return Character.toString((char) ('a' + y - 1)) + x;
	}

	public static boolean onTable(int x, int y) {
		return x >= 1 && x <= 8 && y >= 1 && y <= 8;
	}

	public abstract boolean clearPath(Piece[][] board, int xDest, int yDest);

	public abstract boolean validMove(Piece[][] board, int xDest, int yDest);

	public abstract int[][] getMoveDirections();
	public abstract int[][] getCaptureDirections();
	public abstract int getMaxMoves();

	// Standard function for checking if a piece can move to a destination
	public boolean canMove(Piece[][] board, int xDest, int yDest) {
		if (!clearPath(board, xDest, yDest)) return false;
		return board[xDest][yDest] == null;
	}

	// Standard function for checking if a piece can capture a piece at a destination
	public boolean canCapture(Piece[][] board, int xDest, int yDest) {
		if (!clearPath(board, xDest, yDest)) return false;
		return board[xDest][yDest] != null && board[xDest][yDest].side != side;
	}

	public ArrayList<Move> getPossibleCaptures(Piece[][] board) {
		ArrayList<Move> moves = new ArrayList<>();

		for (int[] moveDir : getCaptureDirections()) {
			for (int i = 1; i <= getMaxMoves(); i++) {
				int xDest = x + i * moveDir[0];
				int yDest = y + i * moveDir[1];
				if (canCapture(board, xDest, yDest)) {
					moves.add(Move.moveTo(getSrcString(), getDstString(xDest, yDest)));
				}
			}
		}

		return moves;
	}

	public ArrayList<Move> getPossibleMoves(Piece[][] board) {
		ArrayList<Move> moves = new ArrayList<>();

		for (int[] moveDir : getMoveDirections()) {
			for (int i = 1; i <= getMaxMoves(); i++) {
				int xDest = x + i * moveDir[0];
				int yDest = y + i * moveDir[1];
				if (canMove(board, xDest, yDest)) {
					moves.add(Move.moveTo(getSrcString(), getDstString(xDest, yDest)));
				}
			}
		}

		return moves;
	}

	public ArrayList<Move> getAllMoves(Piece[][] board) {
		System.out.println("Getting all moves for " + type + " at " + getSrcString());
		ArrayList<Move> moves = new ArrayList<>();
		moves.addAll(getPossibleMoves(board));
		moves.addAll(getPossibleCaptures(board));
		return moves;
	}
}
