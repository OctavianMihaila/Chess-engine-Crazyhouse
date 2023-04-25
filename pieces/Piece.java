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

	}

	public PieceType getType() {
		return type;
	}

	//TODO Check getSrcString getDstString.
	public String getSrcString() {
//		System.out.println("###### " + "x: " + x + " y: " + y);
		return Character.toString((char) ('a' + x)) + y;
	}

	public String getDstString(int xDst, int yDst) {
//		System.out.println("###### " + "xDst: " + xDst + " yDst: " + yDst);
		int yFin = this.y + yDst;
		int xFin = this.x + xDst;
		return Character.toString((char) ('a' + xFin)) + yFin;
	}

	public abstract boolean canMove(Piece[][] board, int xDest, int yDest);
	abstract boolean canCapture(Piece[][] board, int xDest, int yDest);
	 public abstract Move suggestRandomMove(Piece[][] board);
}
