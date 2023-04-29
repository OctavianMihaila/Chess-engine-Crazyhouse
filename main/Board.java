package main;

import pieces.*;

import java.util.ArrayList;
import java.util.Random;

public class Board {
	private final Piece[][] board;

	private final ArrayList<Piece> whites = new ArrayList<>();
	private final ArrayList<Piece> blacks = new ArrayList<>();
	private final ArrayList<Piece> whiteCaptures = new ArrayList<>();
	private final ArrayList<Piece> blacksCaptures = new ArrayList<>();
	private final Piece whiteKing;
	private final Piece blackKing;
	private static final int CASTLING_DISTANCE = 2;
	private static final int SHORT_CASTLE_KING_DESTINATION_Y = 7;
	private static final int LONG_CASTLE_KING_DESTINATION_Y = 3;
	private static final int SHORT_CASTLE_ROOK_DESTINATION_Y = 6;
	private static final int LONG_CASTLE_ROOK_DESTINATION_Y = 4;

	private static final int SHORT_CASTLE_ROOK_ORIGIN_Y = 8;

	private static final int LONG_CASTLE_ROOK_ORIGIN_Y = 1;


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
						whites.add(board[i][j]);
					} else {
						blacks.add(board[i][j]);
					}
				}
			}
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

	/**
	 * Getter for the board
	 * @return the board
	 */
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

	/**
	 * Checks if a position is attack from one of the enemy pieces.
	 */
	public boolean CheckIfPositionIsSafe(PlaySide enemySide, int x, int y) {
		switch (enemySide) {
			case WHITE -> {
				for (Piece piece : whites) {
					if (piece.canMove(this, x, y)) {
						return false;
					}
				}
			}
			case BLACK -> {
				System.out.println("*** Searching blacks to see if any piece can capture at position: " + x + " " + y + " ***");
				for (Piece piece : blacks) {
					if (piece.canMove(this, x, y)) {
						System.out.println("*** Found piece that can capture at " + x + " " + y + " ***");
						return false;
					}
				}
			}
			default -> System.out.println("Unexpected enemy side received");
		}

		return true;
	}

	/**
	 * rook and king must be on the same side.
	 * @param castleType "short" or "long"
	 */
	public boolean canCastle(Rook rook, King king, String castleType) {
		if (rook.getSide() != king.getSide()) return false;

		// Can't castle if the king or the rook has moved
		if (rook.isMoved() || king.isMoved()) return false;

		System.out.println("TRYNG TO CASTLE: " +  castleType);
		System.out.println("SIDE: " + king.getSide());
		switch (castleType) {
			case "short" -> {
				if (king.getSide() == PlaySide.WHITE) {
					// Check if there are pieces between the king and the rook
					if (board[1][6] != null || board[1][7] != null) {
						System.out.println("@@@ There are pieces between rook and king (short) @@@");
						return false;
					}

					// Checking if positions between king and rook are safe
					if (!CheckIfPositionIsSafe(PlaySide.BLACK, 1, 6)
							|| !CheckIfPositionIsSafe(PlaySide.BLACK, 1, 7)) {
						System.out.println("@@@ Short castle is not possible(short) @@@");
						return false;
					}
					System.out.println("@@@ Short castle is possible @@@");

				} else {
					// Check if there are pieces between the king and the rook
					if (board[8][6] != null || board[8][7] != null) {
						System.out.println("@@@ There are pieces between rook and king (short) @@@");
						return false;
					}

					// Checking if positions between king and rook are safe
					if (!CheckIfPositionIsSafe(PlaySide.WHITE, 8, 6)
							|| !CheckIfPositionIsSafe(PlaySide.WHITE, 8, 7)) {
						System.out.println("@@@ Short castle is not possible (long)@@@");
						return false;
					}
				}
			}

			case "long" -> {
				if (king.getSide() == PlaySide.WHITE) {
					// Check if there are pieces between the king and the rook
					if (board[1][4] != null || board[1][3] != null || board[1][2] != null) return false;

					// Checking if positions between king and rook are safe
					if (!CheckIfPositionIsSafe(PlaySide.BLACK, 1, 4)
							|| !CheckIfPositionIsSafe(PlaySide.BLACK, 1, 3)
							|| !CheckIfPositionIsSafe(PlaySide.BLACK, 1, 2)) {
						return false;
					}

					System.out.println("@@@ Long castle is possible @@@");

				} else {
					// Check if there are pieces between the king and the rook
					if (board[8][4] != null || board[8][3] != null || board[8][2] != null) return false;

					// Checking if positions between king and rook are safe
					if (!CheckIfPositionIsSafe(PlaySide.WHITE, 8, 4)
							|| !CheckIfPositionIsSafe(PlaySide.WHITE, 8, 3)
							|| !CheckIfPositionIsSafe(PlaySide.WHITE, 8, 2)) {
						return false;
					}

				}
			}
			default -> System.out.println("Unexpected castle type received");
		}

		System.out.println("@@@ CASTLE IS POSSIBLE @@@");

		return true;
	}

	public void registerCastle(int srcX, int dstY) {
		Piece rookToMove = null;

		// Castle
		if (dstY == SHORT_CASTLE_KING_DESTINATION_Y) {
			System.out.println("### Before SHORT CASTLING ###");
			DebugTools.printBoardPretty(board, false);

			// Moving the rook
			rookToMove = board[srcX][SHORT_CASTLE_ROOK_ORIGIN_Y];
			rookToMove.y = SHORT_CASTLE_ROOK_DESTINATION_Y;
			board[srcX][SHORT_CASTLE_ROOK_DESTINATION_Y] = board[srcX][SHORT_CASTLE_ROOK_ORIGIN_Y];
			board[srcX][SHORT_CASTLE_ROOK_ORIGIN_Y] = null;
		} else if (dstY == LONG_CASTLE_KING_DESTINATION_Y) {
			System.out.println("### Before LONG CASTLING ###");
			DebugTools.printBoardPretty(board, false);

			// Moving the rook
			rookToMove = board[srcX][LONG_CASTLE_ROOK_ORIGIN_Y];
			rookToMove.y = LONG_CASTLE_ROOK_DESTINATION_Y;
			board[srcX][LONG_CASTLE_ROOK_DESTINATION_Y] = board[srcX][LONG_CASTLE_ROOK_ORIGIN_Y];
			board[srcX][LONG_CASTLE_ROOK_ORIGIN_Y] = null;
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

		if (srcPiece.getType() == PieceType.KING) {
			((King)srcPiece).setMoved(true);

			if (move.isCastle()) {
				registerCastle(srcX, dstY);
			}
		}

		if (srcPiece.getType() == PieceType.ROOK) {
			((Rook)srcPiece).setMoved(true);
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

		System.out.println("### AFTER LAST MOVE ###");
		DebugTools.printBoardPretty(board, false);
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
	public Move aggressiveMode(PlaySide side) {
		ArrayList<Move> allPossibleMoves = new ArrayList<>();

		// Setting up my pieces and the other player pieces
		ArrayList<Piece> mine = getSame(side);
		ArrayList<Piece> theirs = getOpposites(side);
		Piece myKing = getSameKing(side);

		// Check if king is in check
		System.out.println("Checking if the king can be captured at " + myKing.getSrcString());
		for (Piece piece : theirs) {
			if (piece.canCapture(this, myKing.x, myKing.y)) {
				System.out.println("King is in chess because of " + piece.getSrcString() + " " + piece.getType());
				allPossibleMoves.addAll(myKing.getPossibleCaptures(this));
				if (allPossibleMoves.size() != 0) return chooseRandom(allPossibleMoves);

				// TODO make other pieces capture the problem piece
				allPossibleMoves.addAll(myKing.getPossibleMoves(this));
				if (allPossibleMoves.size() != 0) return chooseRandom(allPossibleMoves);

				return Move.resign();
			}
		}

		// Castling
		Piece shortCastleRook = board[1][8];
		Piece longCastleRook = board[1][1];

		if (shortCastleRook != null && shortCastleRook.getType() == PieceType.ROOK
				&& canCastle((Rook)shortCastleRook, (King)whiteKing, "short")) { // short castle
			System.out.println(">>> CAN CASTLE SHORT");
			return Move.moveTo(whiteKing.getSrcString(),
					Piece.getDstString(whiteKing.x, whiteKing.y + CASTLING_DISTANCE));
		} else if (longCastleRook != null && longCastleRook.getType() == PieceType.ROOK
				&& canCastle((Rook)longCastleRook, (King)whiteKing, "long")) { // long castle
			System.out.println(">>> CAN CASTLE LONG");
			return Move.moveTo(whiteKing.getSrcString(),
					Piece.getDstString(whiteKing.x, whiteKing.y - CASTLING_DISTANCE));
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

//		Piece shortCastleRook = board[1][8];
//		Piece longCastleRook = board[1][1];
//
//
//		if (shortCastleRook != null && shortCastleRook.getType() == PieceType.ROOK
//				&& canCastle((Rook)shortCastleRook, (King)whiteKing, "short")) { // short castle
//
//			return Move.moveTo(whiteKing.getSrcString(),
//					Piece.getDstString(whiteKing.x, whiteKing.y + CASTLING_DISTANCE));
//		} else if (longCastleRook != null && longCastleRook.getType() == PieceType.ROOK
//				&& canCastle((Rook)longCastleRook, (King)whiteKing, "long")) { // long castle
//
//			return Move.moveTo(whiteKing.getSrcString(),
//					Piece.getDstString(whiteKing.x, whiteKing.y - CASTLING_DISTANCE));
//		}
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

