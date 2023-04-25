package pieces;

import main.Move;
import main.PieceType;

public abstract class Piece {
	public boolean isMine;
	private PieceType type;
	public int x;
	public int y;

	public Piece(boolean isMine, PieceType type, int x, int y) {
		this.isMine = isMine;
		this.type = type;
		this.x = x;
		this.y = y;
	}

	public PieceType getType() {
		return type;
	}

	public String getSrcString() {
		return Character.toString((char) ('a' + y)) + x;
	}

	public String getDstString(int x, int y) {
		return Character.toString((char) ('a' + y)) + x;
	}

	public abstract boolean canMove(Piece[][] board, int xDest, int yDest);
	abstract boolean canCapture(Piece[][] board, int xDest, int yDest);
	 public abstract Move suggestRandomMove(Piece[][] board);
}
