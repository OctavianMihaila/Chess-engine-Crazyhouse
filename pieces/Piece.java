package pieces;

import main.Move;
import main.PieceType;
import main.PlaySide;

import java.util.ArrayList;

public abstract class Piece {
	public boolean captured = false;
	public PlaySide side;
	private PieceType type;
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

	public abstract boolean canMove(Piece[][] board, int xDest, int yDest);

	public abstract boolean canCapture(Piece[][] board, int xDest, int yDest);

	public abstract ArrayList<Move> suggestPossibleMoves(Piece[][] board);
}
