package pieces;

import main.Board;
import main.Move;
import main.PieceType;
import main.PlaySide;

import java.util.ArrayList;

public abstract class Piece {
	public PlaySide side;
	private final PieceType type;
	public int x;
	public int y;
	public int value;

	public Piece(PlaySide side, PieceType type, int x, int y, int value) {
		this.side = side;
		this.type = type;
		this.x = x;
		this.y = y;
		this.value = value;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * Setter for the location of the piece on the board
	 */
	public void updatePosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Getter for the type of the piece
	 *
	 * @return the type of the piece
	 */
	public PieceType getType() {
		return type;
	}

	/**
	 * Getter for the location of the piece on the board
	 *
	 * @return a String of the location of the piece on the board
	 */
	public String getSrcString() {
		return Character.toString((char) ('a' + y - 1)) + x;
	}

	/**
	 * Getter for converting location on the board to a String
	 *
	 * @param x the vertical coordinate
	 * @param y the horizontal coordinate
	 * @return a String of the location on the board
	 */
	public static String getDstString(int x, int y) {
		return Character.toString((char) ('a' + y - 1)) + x;
	}

	/**
	 * Checks if the coordinates are on the table
	 *
	 * @param x the vertical coordinate
	 * @param y the horizontal coordinate
	 * @return true if the coordinates are on the table, false otherwise
	 */
	public static boolean onTable(int x, int y) {
		return x >= 1 && x <= 8 && y >= 1 && y <= 8;
	}

	/**
	 * Checks if a path is clear between the piece and the destination.
	 * Should always check if the move is valid first
	 *
	 * @param board the board to check the path on
	 * @param xDest the vertical coordinate of the destination
	 * @param yDest the horizontal coordinate of the destination
	 * @return true if the path is clear, false otherwise
	 */
	public boolean validPath(Board board, int xDest, int yDest) {
		int verticalDist = xDest - x;
		int horizontalDist = yDest - y;

		int xDir = (int) Math.signum(verticalDist);
		int yDir = (int) Math.signum(horizontalDist);
		for (int i = 1; i < Math.max(Math.abs(horizontalDist), Math.abs(verticalDist)); i++) {
			if (board.getPiece(x + i * xDir, y + i * yDir) != null) return false;
		}

		return true;
	}

	/**
	 * Checks if a move is valid
	 *
	 * @param board the board to check the move on
	 * @param xDest the vertical coordinate of the destination
	 * @param yDest the horizontal coordinate of the destination
	 * @return true if the move is valid, false otherwise
	 */
	public abstract boolean validMove(Board board, int xDest, int yDest);

	/**
	 * Checks if a capture is valid
	 *
	 * @param board the board to check the capture on
	 * @param xDest the vertical coordinate of the destination
	 * @param yDest the horizontal coordinate of the destination
	 * @return true if the capture is valid, false otherwise
	 */
	public boolean validCapture(Board board, int xDest, int yDest) {
		return validMove(board, xDest, yDest);
	}

	/**
	 * Checks if a piece can move at the destination
	 *
	 * @param board the board to check the move on
	 * @param xDest the vertical coordinate of the destination
	 * @param yDest the horizontal coordinate of the destination
	 * @return true if the piece can move at the destination, false otherwise
	 */
	public boolean canMove(Board board, int xDest, int yDest) {
		if (!validMove(board, xDest, yDest)) return false;
		if (!validPath(board, xDest, yDest)) return false;
		Piece piece = board.getPiece(xDest, yDest);
		return piece == null;
	}

	/**
	 * Checks if a piece can capture at the destination
	 *
	 * @param board the board to check the capture on
	 * @param xDest the vertical coordinate of the destination
	 * @param yDest the horizontal coordinate of the destination
	 * @return true if the piece can capture at the destination, false otherwise
	 */
	public boolean canCapture(Board board, int xDest, int yDest) {
		if (!validCapture(board, xDest, yDest)) return false;
		if (!validPath(board, xDest, yDest)) return false;
		Piece piece = board.getPiece(xDest, yDest);
		return piece != null && piece.side != side;
	}

	/**
	 * Gets all possible moves for the piece
	 *
	 * @param board the board to get the moves on
	 * @return an ArrayList of all possible moves
	 */
	public ArrayList<Move> getPossibleCaptures(Board board) {
		ArrayList<Move> moves = new ArrayList<>();

		Piece myKing = board.getSameKing(side);
		for (int[] moveDir : getCaptureDirections()) {
			for (int i = 1; i <= getMaxMoves(); i++) {
				int xDest = x + i * moveDir[0];
				int yDest = y + i * moveDir[1];
				if (canCapture(board, xDest, yDest)) {
					Move potentialCapture = Move.moveTo(getSrcString(), getDstString(xDest, yDest));
					// Simulate the capture and check if the king is in check
					board.doMove(potentialCapture);
					// Can do capture if king isn't in check
					if (board.getAllCapturesOnPiece(myKing).isEmpty()) moves.add(potentialCapture);
					board.undoMove();
				}
			}
		}

		return moves;
	}

	/**
	 * Gets all possible captures for the piece
	 *
	 * @param board the board to get the captures on
	 * @return an ArrayList of all possible moves, not including captures
	 */
	public ArrayList<Move> getPossibleMoves(Board board) {
		ArrayList<Move> moves = new ArrayList<>();

		Piece myKing = board.getSameKing(side);
		for (int[] moveDir : getMoveDirections()) {
			for (int i = 1; i <= getMaxMoves(); i++) {
				int xDest = x + i * moveDir[0];
				int yDest = y + i * moveDir[1];
				if (canMove(board, xDest, yDest)) {
					Move potentialMove = Move.moveTo(getSrcString(), getDstString(xDest, yDest));
					// Simulate the move and check if the king is in check
					board.doMove(potentialMove);
					// Can do move if king isn't in check
					if (board.getAllCapturesOnPiece(myKing).isEmpty()) moves.add(potentialMove);
					board.undoMove();
				}
			}
		}

		return moves;
	}

	/**
	 * Gets all possible moves and captures for the piece
	 *
	 * @param board the board to get the moves and captures on
	 * @return an ArrayList of all possible moves and captures
	 */
	public ArrayList<Move> getAllMoves(Board board) {
		ArrayList<Move> moves = new ArrayList<>();
		moves.addAll(getPossibleMoves(board));
		moves.addAll(getPossibleCaptures(board));
		return moves;
	}

	/**
	 * Gets the directions the piece can move in
	 *
	 * @return an array of the directions the piece can move in
	 */
	public abstract int[][] getMoveDirections();

	/**
	 * Gets the directions the piece can capture in
	 *
	 * @return an array of the directions the piece can capture in
	 */
	public abstract int[][] getCaptureDirections();

	/**
	 * Gets the maximum number of moves the piece can make
	 *
	 * @return the maximum number of moves the piece can make
	 */
	public abstract int getMaxMoves();
}
