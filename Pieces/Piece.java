package Pieces;

public abstract class Piece {
	private boolean isMine;
	private PieceType type;
	int x,y;

	public Piece(boolean isMine, PieceType type, int x, int y) {
		this.isMine = isMine;
		this.type = type;
		this.x = x;

	}

	abstract boolean canMove(Piece[][] board, int xDest, int yDest);
	abstract boolean canCapture(Piece[][] board, int xDest, int yDest);
	 public abstract Move suggestRandomMove(Piece[][] board, String source);
}
