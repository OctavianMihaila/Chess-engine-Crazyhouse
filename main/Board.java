package main;

import pieces.*;

import java.util.ArrayList;
import java.util.Random;

public class Board {
	private final Piece[][] board;

	private boolean imBlack;

	private final ArrayList<Piece> whites = new ArrayList<>();
	private final ArrayList<Piece> blacks = new ArrayList<>();
	private final ArrayList<Piece> whiteCaptures = new ArrayList<>();
	private final ArrayList<Piece> blacksCaptures = new ArrayList<>();
	private final Piece whiteKing;


	public Board() {
		board = new Piece[9][9];
		// Setting up the pawns
		for (int i = 1; i <= 8; i++) {
			board[2][i] = new Pawn(PlaySide.WHITE, 2, i);
		}

		for (int i = 1; i <= 8; i++) {
			board[7][i] = new Pawn(PlaySide.BLACK, 7, i);
		}

		// Setting up the rooks
		board[1][1] = new Rook(PlaySide.WHITE, 1, 1);
		board[1][8] = new Rook(PlaySide.WHITE, 1, 8);
		board[8][1] = new Rook(PlaySide.BLACK, 8, 1);
		board[8][8] = new Rook(PlaySide.BLACK, 8, 8);

		// Setting up the knights
		board[1][2] = new Knight(PlaySide.WHITE, 1, 2);
		board[1][7] = new Knight(PlaySide.WHITE, 1, 7);
		board[8][2] = new Knight(PlaySide.BLACK, 8, 2);
		board[8][7] = new Knight(PlaySide.BLACK, 8, 7);

		// Setting up the bishops
		board[1][3] = new Bishop(PlaySide.WHITE, 1, 3);
		board[1][6] = new Bishop(PlaySide.WHITE, 1, 6);
		board[8][3] = new Bishop(PlaySide.BLACK, 8, 3);
		board[8][6] = new Bishop(PlaySide.BLACK, 8, 6);

		// Setting up the queens
		board[1][4] = new Queen(PlaySide.WHITE, 1, 4);
		board[8][4] = new Queen(PlaySide.BLACK, 8, 4);

		// Setting up the kings
		board[1][5] = new King(PlaySide.WHITE, 1, 5);
		whiteKing = board[1][5];
		board[8][5] = new King(PlaySide.BLACK, 8, 5);

		// Setting up the pieces
		for (int i = 1; i <= 8; i++) {
			for (int j = 1; j <= 8; j++) {
				if (board[i][j] != null) {
					if (board[i][j].side == PlaySide.WHITE) {
						whites.add(board[i][j]);
					} else {
						blacks.add(board[i][j]);
					}
				}
			}
		}
	}

	/**
	 * Getter for pieces of the same player
	 * @param side the side of the player requesting the pieces
	 * @return pieces of the player
	 */
	public ArrayList<Piece> getSame(PlaySide side) {
		if (side == PlaySide.WHITE) return whites;
		return blacks;
	}

	/**
	 * Getter for pieces of the opposite player
	 * @param side the side of the player requesting the opposite pieces
	 * @return pieces of the opposite player
	 */
	public ArrayList<Piece> getOpposites(PlaySide side) {
		if (side == PlaySide.WHITE) return blacks;
		return whites;
	}

	public Piece[][] getBoard() {
		return board;
	}

	/**
	 * Getter for a piece on the table
	 * @param x vertical coordinate on table
	 * @param y horizontal coordinate on table
	 * @return piece at the position
	 */
	public Piece getPiece(int x, int y) {
		return board[x][y];
	}

	/**
	 * Setter for a piece on the table. Also updates the piece internal location.
	 * Can return null if the position is empty.
	 * @param piece the piece to set on the table
	 * @param x vertical coordinate on table
	 * @param y horizontal coordinate on table
	 * @return the piece that was on the table before putting current piece
	 */
	public Piece setPiece(Piece piece, int x, int y) {
		if (piece == null) return null;
		Piece current = board[x][y];
		board[x][y] = piece;
		piece.x = x;
		piece.y = y;
		return current;
	}

	public boolean checkIfEnPassantIsEnabled(int srcX, int srcY, int dstX, int dstY) {
		// Check if the pawn has moved before
		if (srcX != 7 && srcX != 2) {
			return false;
		}

		// Check if the pawn moved 2 squares forward
		if (Math.abs(dstX - srcX) == 2 && dstY == srcY) {
			return true;
		}

		return false;
	}

	public boolean checkIfEnPassantCapture(int srcX, int srcY, int dstX, int dstY) {
		// Normal capture
		if (board[dstX][dstY] != null) {
			return false;
		}

		Piece capturedPiece = board[dstX + 1][dstY];

		// TODO: Solve problem with imBlack when the bot can play both sides.

		// There is no pawn that can be captured with en passant (bot's pawn).
		if (capturedPiece != null && capturedPiece.getType() == PieceType.PAWN
				&& capturedPiece.side != (imBlack ? PlaySide.WHITE : PlaySide.BLACK)
				&& board[dstX + 1][dstY].side == (imBlack ? PlaySide.BLACK : PlaySide.WHITE)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Registers a move and also update the internals of the board
	 * @param move the move to register
	 */
	public void registerMove(Move move) {
		if (!move.isNormal() && !move.isDropIn() && !move.isPromotion()) return;
		int srcY = move.getSource().get().charAt(0) - 'a' + 1;
		int srcX = move.getSource().get().charAt(1) - '0';
		int dstY = move.getDestination().get().charAt(0) - 'a' + 1;
		int dstX = move.getDestination().get().charAt(1) - '0';
		Piece srcPiece = board[srcX][srcY];
		Piece dstPiece = board[dstX][dstY];
		if (srcPiece == null) return;

		// En passant
		if (move.isNormal() && srcPiece.getType() == PieceType.PAWN) { // moved piece is a pawn
			// Bot performs en passant.
			if (srcPiece.side == (imBlack ? PlaySide.WHITE : PlaySide.BLACK)
					&& checkIfEnPassantIsEnabled(srcX, srcY, dstX, dstY)) {
				move.setEnablesEnPassant(true);
			}

			// Bot receives en passant and updates its internal structures.
			if (checkIfEnPassantCapture(srcX, srcY, dstX, dstY)) {
				Piece capturedPiece = board[dstX + 1][dstY];
				if (capturedPiece.side == PlaySide.BLACK) {
					blacks.remove(capturedPiece);
					whiteCaptures.add(capturedPiece);
				} else if (capturedPiece.side == PlaySide.WHITE) {
					whites.remove(capturedPiece);
					blacksCaptures.add(capturedPiece);
				}

				capturedPiece = null; // Remove the captured pawn (with en passant).
			}
		}

		// When destination is not null, it is a capture
		if (dstPiece != null) {
			board[dstX][dstY] = srcPiece;
			if (dstPiece.side == PlaySide.BLACK) {
				whiteCaptures.add(dstPiece);
				blacks.remove(dstPiece);
			} else if (dstPiece.side == PlaySide.WHITE) {
				blacksCaptures.add(dstPiece);
				whites.remove(dstPiece);
			}
			// Mark capture location
			dstPiece.captured = true;
			dstPiece.x = dstPiece.y = -1;
		}

		srcPiece.x = dstX;
		srcPiece.y = dstY;
		board[dstX][dstY] = srcPiece;
		board[srcX][srcY] = null;
	}

	public Move generateEnPassantMove() {
		Move lastMove = Bot.getLastMove();

		if (lastMove != null && lastMove.isEnablesEnPassant()) {
			int xDestLastMove = lastMove.getDestinationX();
			int yDestLastMove = lastMove.getDestinationY();
			String sourceNewMove = null;
			String destinationNewMove = Piece.getDstString(xDestLastMove + 1, yDestLastMove);

			Piece leftPiece = null;
			Piece rightPiece = null;

			// Checking for pieces to the left and to the right of the pawn.
			if (yDestLastMove == 1) {
				rightPiece = board[xDestLastMove][yDestLastMove + 1];
			} else if (yDestLastMove == 8) {
				leftPiece = board[xDestLastMove][yDestLastMove - 1];
			} else {
				leftPiece = board[xDestLastMove][yDestLastMove - 1];
				rightPiece = board[xDestLastMove][yDestLastMove + 1];
			}

			// Looking for a pawn that can do en passant.
			if (leftPiece != null && leftPiece.getType() == PieceType.PAWN && leftPiece.side == (imBlack ? PlaySide.BLACK : PlaySide.WHITE)) {
				sourceNewMove = leftPiece.getSrcString();
				return ((Pawn)leftPiece).performEnPassant(board, xDestLastMove, yDestLastMove, sourceNewMove, destinationNewMove);
			} else if (rightPiece != null && rightPiece.getType() == PieceType.PAWN && rightPiece.side == (imBlack ? PlaySide.BLACK : PlaySide.WHITE)) {
				sourceNewMove = rightPiece.getSrcString();
				return ((Pawn)rightPiece).performEnPassant(board, xDestLastMove, yDestLastMove, sourceNewMove, destinationNewMove);
			}
		}

		return null;
	}

	/**
	 * Randomly selects a move from an ArrayList
	 * @param moves list of moves
	 * @return a random move if list is not null and has members, null otherwise
	 */
	public Move chooseRandom(ArrayList<Move> moves) {
		if (moves == null || moves.size() == 0) return null;
		Random rand = new Random(System.currentTimeMillis());
		int index = rand.nextInt(moves.size());
		return moves.get(index);
	}

	/**
	 * Chooses a random move, but prioritize captures first
	 * @return a random move
	 */
	public Move aggressiveMode() {
		ArrayList<Move> allPossibleMoves = new ArrayList<>();

		// Check if king is in check
		System.out.println("Checking if the king can be captured at " + whiteKing.getSrcString());
		for (Piece piece : blacks) {
			if (piece.canCapture(this, whiteKing.x, whiteKing.y)) {
				System.out.println("King is in chess because of " + piece.getSrcString() + " " + piece.getType());
				allPossibleMoves.addAll(whiteKing.getPossibleCaptures(this));
				if (allPossibleMoves.size() != 0) return chooseRandom(allPossibleMoves);

				// TODO make other pieces capture the problem piece
				allPossibleMoves.addAll(whiteKing.getPossibleMoves(this));
				if (allPossibleMoves.size() != 0) return chooseRandom(allPossibleMoves);

				return Move.resign();
			}
		}

		// Checking if we can do an en passant attack.
		Move enPassantMove = generateEnPassantMove();
		if (enPassantMove != null) {
			return enPassantMove;
		}

		// First generate all captures and choose one if there are valid captures
		for (Piece piece : whites) allPossibleMoves.addAll(piece.getPossibleCaptures(this));
		if (allPossibleMoves.size() != 0) return chooseRandom(allPossibleMoves);

		// If no captures are valid, generate all moves and choose one
		for (Piece piece : whites) allPossibleMoves.addAll(piece.getPossibleMoves(this));
		if (allPossibleMoves.size() != 0) return chooseRandom(allPossibleMoves);

		// If no moves are valid, resign, for now
		return Move.resign();
	}

	/**
	 * Chooses a random move, purely random
	 * @return a purely random move
	 */
	public Move getRandMove() {
		ArrayList<Move> allPossibleMoves = new ArrayList<>();

		// Check if king is in check
		System.out.println("Checking if the king can be captured at " + whiteKing.getSrcString());
		for (Piece piece : blacks) {
			if (piece.canCapture(this, whiteKing.x, whiteKing.y)) {
				System.out.println("King is in chess because of " + piece.getSrcString() + " " + piece.getType());
				allPossibleMoves.addAll(whiteKing.getAllMoves(this));
				if (allPossibleMoves.size() == 0) return Move.resign();
				Random rand = new Random(System.currentTimeMillis());
				int index = rand.nextInt(allPossibleMoves.size());

				return allPossibleMoves.get(index);
			}
		}

		for (Piece piece : whites) {
			ArrayList<Move> move = piece.getAllMoves(this);
			if (move == null || move.size() == 0) continue;
			allPossibleMoves.addAll(move);
		}

		if (allPossibleMoves.size() == 0) {
			System.out.println("No moves found");
			return Move.resign();
		}

		Random rand = new Random(System.currentTimeMillis());
		int index = rand.nextInt(allPossibleMoves.size());
		return allPossibleMoves.get(index);
	}
}

