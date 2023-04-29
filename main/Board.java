package main;

import pieces.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

public class Board {
	private final Piece[][] board;

	// Field for pieces on the table
	private final ArrayList<Piece> whitePieces = new ArrayList<>();
	private final ArrayList<Piece> blackPieces = new ArrayList<>();
	private final ArrayList<Piece> whiteCaptures = new ArrayList<>();
	private final ArrayList<Piece> blackCaptures = new ArrayList<>();

	// Fields for ease of acces to kings
	private final Piece whiteKing;
	private final Piece blackKing;

	// Fields for simulating and undoing moves
	private final Stack<Move> simulatedMoves = new Stack<>();
	private final Stack<Piece> simulatedWhiteCaptures = new Stack<>();
	private final Stack<Piece> simulatedBlackCaptures = new Stack<>();

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
		blackKing = board[8][5];

		// Setting up the pieces
		for (int i = 1; i <= 8; i++) {
			for (int j = 1; j <= 8; j++) {
				if (board[i][j] != null) {
					if (board[i][j].side == PlaySide.WHITE) {
						whitePieces.add(board[i][j]);
					} else {
						blackPieces.add(board[i][j]);
					}
				}
			}
		}
	}

	/**
	 * Method used to register a move with the capability to undo the move
	 * @param move the move to simulate
	 */
	public void doMove(Move move) {
		if (!move.isNormal() && !move.isDropIn() && !move.isPromotion()) return;


		int srcY = move.getSourceY();
		int srcX = move.getSourceX();
		int dstY = move.getDestinationY();
		int dstX = move.getDestinationX();

		Piece srcPiece = board[srcX][srcY];
		Piece dstPiece = board[dstX][dstY];
		if (srcPiece == null) return;
		simulatedMoves.push(move);

		// When destination is not null, it is a capture, simulate it and retain all the information
		if (dstPiece != null) {
			move.markCapture();
			if (dstPiece.side == PlaySide.BLACK) {
				simulatedWhiteCaptures.push(dstPiece);
				blackPieces.remove(dstPiece);
			} else if (dstPiece.side == PlaySide.WHITE) {
				simulatedBlackCaptures.push(dstPiece);
				whitePieces.remove(dstPiece);
			}
		}

		srcPiece.updatePosition(dstX, dstY);
		board[dstX][dstY] = srcPiece;
		board[srcX][srcY] = null;
	}

	/**
	 * Method used to undo last move
	 */
	public void undoMove() {
		if (simulatedMoves.size() == 0) return;
		Move lastMove = simulatedMoves.pop();
		if (lastMove == null) return;

		int srcY = lastMove.getSourceY();
		int srcX = lastMove.getSourceX();
		int dstY = lastMove.getDestinationY();
		int dstX = lastMove.getDestinationX();

		Piece movedPiece = board[dstX][dstY];
		board[srcX][srcY] = movedPiece;
		movedPiece.updatePosition(srcX, srcY);

		// If the move was a capture we need to restore the captured piece
		if (lastMove.isCapture()) {
			Piece capturedPiece;
			if (movedPiece.side == PlaySide.WHITE) {
				capturedPiece = simulatedWhiteCaptures.pop();
				blackPieces.add(capturedPiece);
			} else {
				capturedPiece = simulatedBlackCaptures.pop();
				whitePieces.add(capturedPiece);
			}
			board[dstX][dstY] = capturedPiece;
		} else {
			board[dstX][dstY] = null;
		}
	}

	/**
	 * Getter for the king of the same color as the player
	 * @param side the side of the table
	 * @return the king
	 */
	public Piece getSameKing(PlaySide side) {
		if (side == PlaySide.WHITE) return whiteKing;
		return blackKing;
	}

	/**
	 * Getter for the king of the opposite color as the player
	 * @param side the side of the table
	 * @return the king
	 */
	public Piece getOppositeKing(PlaySide side) {
		if (side == PlaySide.WHITE) return blackKing;
		return whiteKing;
	}

	/**
	 * Getter for pieces of the same player
	 * @param side the side of the player requesting the pieces
	 * @return pieces of the player
	 */
	public ArrayList<Piece> getSame(PlaySide side) {
		if (side == PlaySide.WHITE) return whitePieces;
		return blackPieces;
	}

	/**
	 * Getter for pieces of the opposite player
	 * @param side the side of the player requesting the opposite pieces
	 * @return pieces of the opposite player
	 */
	public ArrayList<Piece> getOpposites(PlaySide side) {
		if (side == PlaySide.WHITE) return blackPieces;
		return whitePieces;
	}

	/**
	 * Getter for the board
	 *
	 * @return the board
	 */
	public Piece[][] getBoard() {
		return board;
	}

	/**
	 * Getter for a piece on the table
	 *
	 * @param x vertical coordinate on table
	 * @param y horizontal coordinate on table
	 * @return piece at the position
	 */
	public Piece getPiece(int x, int y) {
		return board[x][y];
	}

	/**
	 * Registers a move and also update the internals of the board
	 *
	 * @param move the move to register
	 */
	public void registerMove(Move move) {
		if (!move.isNormal() && !move.isDropIn() && !move.isPromotion()) return;

		int srcY = move.getSourceY();
		int srcX = move.getSourceX();
		int dstY = move.getDestinationY();
		int dstX = move.getDestinationX();

		Piece srcPiece = board[srcX][srcY];
		Piece dstPiece = board[dstX][dstY];
		if (srcPiece == null) return;

		// When destination is not null, it is a capture
		if (dstPiece != null) {
			if (dstPiece.side == PlaySide.BLACK) {
				whiteCaptures.add(dstPiece);
				blackPieces.remove(dstPiece);
			} else if (dstPiece.side == PlaySide.WHITE) {
				blackCaptures.add(dstPiece);
				whitePieces.remove(dstPiece);
			}

			// Mark capture location
			dstPiece.updatePosition(-1, -1);
		}

		srcPiece.updatePosition(dstX, dstY);
		if (move.isPromotion()) {
			System.out.println("Promoted pawn at " + srcPiece.getSrcString() + " to queen");
			board[dstX][dstY] = new Queen(srcPiece.side, dstX, dstY, true);
		} else {
			board[dstX][dstY] = srcPiece;
		}
		board[srcX][srcY] = null;
	}

	/**
	 * Randomly selects a move from an ArrayList
	 *
	 * @param moves list of moves
	 * @return a random move if list is not null and has members, null otherwise
	 */
	public Move chooseRandom(ArrayList<Move> moves) {
		if (moves == null || moves.size() == 0) return null;
		Random rand = new Random(System.currentTimeMillis());
		int index = rand.nextInt(moves.size());
		return moves.get(index);
	}

	public Piece getAttackingPiece(Piece target) {
		for (Piece piece : getOpposites(target.side)) {
			if (piece.canCapture(this, target.x, target.y)) return piece;
		}
		return null;
	}

	public ArrayList<Move> getAllCapturesOnPiece(Piece target) {
		ArrayList<Move> moves = new ArrayList<>();
		for (Piece piece : getOpposites(target.side)) {
			if (piece.canCapture(this, target.x, target.y)) {
				moves.add(Move.moveTo(piece.getSrcString(), target.getSrcString()));
			}
		}
		return moves;
	}

	/**
	 * Chooses a random move, but prioritize captures first
	 *
	 * @return a random move
	 */
	public Move aggressiveMode(PlaySide side) {
		ArrayList<Move> allPossibleMoves = new ArrayList<>();

		if (whitePieces.size() == 1 && blackPieces.size() == 1) return Move.resign();

		// Setting up my pieces and the other player pieces
		ArrayList<Piece> mine = getSame(side);
		Piece myKing = getSameKing(side);

		// If king is in chess, try to capture the problem piece
		Piece attackingPiece = getAttackingPiece(myKing);
		if (attackingPiece != null) {
			System.out.println("King is in chess because of " + attackingPiece.getSrcString() + " " + attackingPiece.getType());

			allPossibleMoves.addAll(getAllCapturesOnPiece(attackingPiece));
			if (allPossibleMoves.size() != 0) return chooseRandom(allPossibleMoves);

			allPossibleMoves.addAll(myKing.getPossibleCaptures(this));
			if (allPossibleMoves.size() != 0) return chooseRandom(allPossibleMoves);

			// TODO make other pieces capture the problem piece
			allPossibleMoves.addAll(myKing.getPossibleMoves(this));
			if (allPossibleMoves.size() != 0) return chooseRandom(allPossibleMoves);

			return Move.resign();
		}

		// First generate all captures and choose one if there are valid captures
		for (Piece piece : mine) allPossibleMoves.addAll(piece.getPossibleCaptures(this));
		if (allPossibleMoves.size() != 0) return chooseRandom(allPossibleMoves);

		// If no captures are valid, generate all moves and choose one
		for (Piece piece : mine) allPossibleMoves.addAll(piece.getPossibleMoves(this));
		if (allPossibleMoves.size() != 0) return chooseRandom(allPossibleMoves);

		// If no moves are valid, resign, for now
		return Move.resign();
	}
}
