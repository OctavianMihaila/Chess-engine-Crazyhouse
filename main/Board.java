package main;

import pieces.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Board {
	private static final int CASTLING_DISTANCE = 2;
	private static final int SHORT_CASTLE_KING_DESTINATION_Y = 7;
	private static final int LONG_CASTLE_KING_DESTINATION_Y = 3;
	private static final int SHORT_CASTLE_ROOK_DESTINATION_Y = 6;
	private static final int LONG_CASTLE_ROOK_DESTINATION_Y = 4;

	private static final int SHORT_CASTLE_ROOK_ORIGIN_Y = 8;

	private static final int LONG_CASTLE_ROOK_ORIGIN_Y = 1;

	private final Piece[][] board;

	private boolean imBlack;

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
	private final ArrayList<Piece> simulatedWhitePieces = new ArrayList<>();
	private final ArrayList<Piece> simulatedBlackPieces = new ArrayList<>();
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
					if (board[i][j].getSide() == PlaySide.WHITE) {
						whitePieces.add(board[i][j]);
						simulatedWhitePieces.add(board[i][j]);
					} else {
						blackPieces.add(board[i][j]);
						simulatedBlackPieces.add(board[i][j]);
					}
				}
			}
		}
	}

	/**
	 * Getter for the king of the same color as the player
	 *
	 * @param side the side of the table
	 * @return the king
	 */
	public Piece getSameKing(PlaySide side) {
		if (side == PlaySide.WHITE) return whiteKing;
		return blackKing;
	}

	/**
	 * Getter for the king of the opposite color as the player
	 *
	 * @param side the side of the table
	 * @return the king
	 */
	public Piece getOppositeKing(PlaySide side) {
		if (side == PlaySide.WHITE) return blackKing;
		return whiteKing;
	}

	/**
	 * Getter for pieces of the same player
	 *
	 * @param side the side of the player requesting the pieces
	 * @return pieces of the player
	 */
	public ArrayList<Piece> getSame(PlaySide side) {
		if (side == PlaySide.WHITE) return whitePieces;
		return blackPieces;
	}

	/**
	 * Getter for pieces of the opposite player
	 *
	 * @param side the side of the player requesting the opposite pieces
	 * @return pieces of the opposite player
	 */
	public ArrayList<Piece> getOpposites(PlaySide side) {
		if (side == PlaySide.WHITE) return blackPieces;
		return whitePieces;
	}

	/**
	 * Getter for the captures of the same player
	 *
	 * @param side the side of the player requesting the captures
	 * @return captures of the player
	 */
	public ArrayList<Piece> getSameCaptures(PlaySide side) {
		if (side == PlaySide.WHITE) return whiteCaptures;
		return blackCaptures;
	}

	/**
	 * Getter for the captures of the opposite player
	 *
	 * @param side the side of the player requesting the opposite captures
	 * @return captures of the opposite player
	 */
	public ArrayList<Piece> getOppositeCaptures(PlaySide side) {
		if (side == PlaySide.WHITE) return blackCaptures;
		return whiteCaptures;
	}

	/**
	 * Getter for the simulated pieces of the same player
	 *
	 * @param side the side of the player requesting the simulated pieces
	 * @return simulated pieces of the player
	 */
	public ArrayList<Piece> getSimulatedSame(PlaySide side) {
		if (side == PlaySide.WHITE) return simulatedWhitePieces;
		return simulatedBlackPieces;
	}

	/**
	 * Getter for the simulated pieces of the opposite player
	 *
	 * @param side the side of the player requesting the opposite simulated pieces
	 * @return simulated pieces of the opposite player
	 */
	public ArrayList<Piece> getSimulatedOpposites(PlaySide side) {
		if (side == PlaySide.WHITE) return simulatedBlackPieces;
		return simulatedWhitePieces;
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

	public void setPiece(Piece piece, int x, int y) {
		board[x][y] = piece;
		if (piece == null) return;
		piece.updatePosition(x, y);
	}

	public boolean checkIfEnPassantIsEnabled(int srcX, int srcY, int dstX, int dstY) {
		// Check if the pawn has moved before
		if (srcX != 7 && srcX != 2) return false;

		// Check if the pawn moved 2 squares forward
		if (Math.abs(dstX - srcX) == 2 && dstY == srcY) return true;

		return false;
	}

	public boolean checkIfEnPassantCapture(PlaySide side, int dstX, int dstY) {

		// Normal capture
		if (board[dstX][dstY] != null) return false;

		Piece capturedPiece = board[dstX + 1][dstY];

		// TODO: Solve problem with imBlack when the bot can play both sides.

		// There is no pawn that can be captured with en passant (bot's pawn).
		if (capturedPiece != null && capturedPiece.getType() == PieceType.PAWN
				&& capturedPiece.getSide() != side
				&& board[dstX + 1][dstY].getSide() == side) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * Method used to check if a position is safe for a king to move through
	 *
	 * @param enemySide the side of the enemy
	 * @param x         the x coordinate of the position
	 * @param y         the y coordinate of the position
	 * @return true if the position is safe, false otherwise
	 */
	public boolean checkPositionSafe(PlaySide enemySide, int x, int y) {
		for (Piece piece : getSame(enemySide)) {
			if (piece.canMove(this, x, y)) return false;
		}
		return true;
	}


	/**
	 * rook and king must be on the same side.
	 *
	 * @param castleType "short" or "long"
	 */
	public boolean canCastle(Rook rook, King king, String castleType) {
		if (rook.getSide() != king.getSide()) return false;

		// Can't castle if the king or the rook has moved
		if (rook.isMoved() || king.isMoved()) return false;

		switch (castleType) {
			case "short" -> {
				if (king.getSide() == PlaySide.WHITE) {
					// Check if there are pieces between the king and the rook
					if (board[1][6] != null || board[1][7] != null) {
						return false;
					}

					// Checking if positions between king and rook are safe
					if (!checkPositionSafe(PlaySide.BLACK, 1, 6)
							|| !checkPositionSafe(PlaySide.BLACK, 1, 7)) {
						return false;
					}

				} else {
					// Check if there are pieces between the king and the rook
					if (board[8][6] != null || board[8][7] != null) {
						return false;
					}

					// Checking if positions between king and rook are safe
					if (!checkPositionSafe(PlaySide.WHITE, 8, 6)
							|| !checkPositionSafe(PlaySide.WHITE, 8, 7)) {
						return false;
					}
				}
			}

			case "long" -> {
				if (king.getSide() == PlaySide.WHITE) {
					// Check if there are pieces between the king and the rook
					if (board[1][4] != null || board[1][3] != null || board[1][2] != null) return false;

					// Checking if positions between king and rook are safe
					if (!checkPositionSafe(PlaySide.BLACK, 1, 4)
							|| !checkPositionSafe(PlaySide.BLACK, 1, 3)) {
						return false;
					}

				} else {
					// Check if there are pieces between the king and the rook
					if (board[8][4] != null || board[8][3] != null || board[8][2] != null) return false;

					// Checking if positions between king and rook are safe
					if (!checkPositionSafe(PlaySide.WHITE, 8, 4)
							|| !checkPositionSafe(PlaySide.WHITE, 8, 3)) {
						return false;
					}

				}
			}
		}

		return true;
	}

	public void moveRook(int srcX, int dstY) {
		Piece rookToMove;

		// Castle
		if (dstY == SHORT_CASTLE_KING_DESTINATION_Y) {
			// Moving the rook
			rookToMove = board[srcX][SHORT_CASTLE_ROOK_ORIGIN_Y];
			rookToMove.updatePosition(rookToMove.getX(), SHORT_CASTLE_ROOK_DESTINATION_Y);
			board[srcX][SHORT_CASTLE_ROOK_DESTINATION_Y] = board[srcX][SHORT_CASTLE_ROOK_ORIGIN_Y];
			board[srcX][SHORT_CASTLE_ROOK_ORIGIN_Y] = null;
		} else if (dstY == LONG_CASTLE_KING_DESTINATION_Y) {
			// Moving the rook
			rookToMove = board[srcX][LONG_CASTLE_ROOK_ORIGIN_Y];
			rookToMove.updatePosition(rookToMove.getX(), LONG_CASTLE_ROOK_DESTINATION_Y);
			board[srcX][LONG_CASTLE_ROOK_DESTINATION_Y] = board[srcX][LONG_CASTLE_ROOK_ORIGIN_Y];
			board[srcX][LONG_CASTLE_ROOK_ORIGIN_Y] = null;
		}
	}

	public void registerEnPassant(PlaySide side, Move move, Piece srcPiece, int srcX, int srcY, int dstX, int dstY) {
		// En passant
		if (move.isNormal() && srcPiece.getType() == PieceType.PAWN) { // moved piece is a pawn
			// Bot performs en passant.
			if (srcPiece.getSide() == (imBlack ? PlaySide.WHITE : PlaySide.BLACK)
					&& checkIfEnPassantIsEnabled(srcX, srcY, dstX, dstY)) {
				move.setEnablesEnPassant(true);
			}

			// Bot receives en passant and updates its internal structures.
			if (checkIfEnPassantCapture(side, dstX, dstY)) {
				Piece capturedPiece = board[dstX + 1][dstY];
				ArrayList<Piece> oppositeCaptures = getOppositeCaptures(capturedPiece.getSide());
				ArrayList<Piece> myPieces = getSame(capturedPiece.getSide());
				ArrayList<Piece> mySimulatedPieces = getSimulatedSame(capturedPiece.getSide());

				myPieces.remove(srcPiece);
				mySimulatedPieces.remove(srcPiece);
				oppositeCaptures.add(srcPiece);
			}
		}
	}

	/**
	 * Registers a drop in move
	 *
	 * @param side the side tha is dropping int
	 * @param move the move to register
	 * @return true if the move is a drop in, false otherwise
	 */
	private boolean registerDropIn(PlaySide side, Move move) {
		if (move.isDropIn()) {
			ArrayList<Piece> myCaptures = getSameCaptures(side);
			ArrayList<Piece> myPieces = getSame(side);
			ArrayList<Piece> mySimulatedPieces = getSimulatedSame(side);
			PieceType dropPieceType = move.getReplacement().get();
			Piece chosen = null;
			for (Piece p : myCaptures) {
				if (p.getType() != dropPieceType) continue;
				// Save the piece because we cant modify the list that we are iterating over
				chosen = p;
				break;
			}

			if (chosen == null) return true;
			chosen.setSide(side);
			myCaptures.remove(chosen);
			myPieces.add(chosen);
			// Don't forget to always add to simulated so simulations are accurate
			mySimulatedPieces.add(chosen);
			int dstY = move.getDestinationY();
			int dstX = move.getDestinationX();

			board[dstX][dstY] = chosen;
			chosen.updatePosition(dstX, dstY);

			return true;
		}
		return false;
	}

	/**
	 * Registers a move and also update the internals of the board
	 *
	 * @param move the move to register
	 */
	public void registerMove(PlaySide side, Move move) {
		if (!move.isNormal() && !move.isDropIn() && !move.isPromotion()) return;

		if (registerDropIn(side, move)) return;

		int srcY = move.getSourceY();
		int srcX = move.getSourceX();
		int dstY = move.getDestinationY();
		int dstX = move.getDestinationX();

		Piece srcPiece = getPiece(srcX, srcY);
		Piece dstPiece = getPiece(dstX, dstY);
		if (srcPiece == null) return;

		registerEnPassant(side, move, srcPiece, srcX, srcY, dstX, dstY);

		// When destination is not null, it is a capture
		if (dstPiece != null) {
			// Get all lists of pieces
			ArrayList<Piece> myCaptures = getSameCaptures(srcPiece.getSide());
			ArrayList<Piece> oppositePieces = getOpposites(srcPiece.getSide());
			ArrayList<Piece> simulatedOppositePieces = getSimulatedOpposites(srcPiece.getSide());

			// Now begin removing the piece from the lists
			oppositePieces.remove(dstPiece);
			simulatedOppositePieces.remove(dstPiece);
			if (dstPiece.getType() == PieceType.QUEEN && ((Queen) dstPiece).isPawn()) {
				dstPiece = new Pawn(dstPiece.getSide(), -1, -1);
			}

			// Add the captured piece to the list of captures
			myCaptures.add(dstPiece);

			// Mark capture location
			dstPiece.updatePosition(-1, -1);
		}

		// Special moves
		if (move.isPromotion() || (srcPiece.getType() == PieceType.PAWN && (srcPiece.getSide() == PlaySide.WHITE && dstX == 8 || srcPiece.getSide() == PlaySide.BLACK && dstX == 1))) {
			ArrayList<Piece> pieces = getSame(srcPiece.getSide());
			pieces.remove(srcPiece);
			setPiece(new Queen(srcPiece.getSide(), dstX, dstY, true), dstX, dstY);
			pieces.add(board[dstX][dstY]);
		} else {
			setPiece(srcPiece, dstX, dstY);
		}
		setPiece(null, srcX, srcY);
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
			if (leftPiece != null && leftPiece.getType() == PieceType.PAWN && leftPiece.getSide() == (imBlack ? PlaySide.BLACK : PlaySide.WHITE)) {
				sourceNewMove = leftPiece.getSrcString();
				return ((Pawn) leftPiece).performEnPassant(board, xDestLastMove, yDestLastMove, sourceNewMove, destinationNewMove);
			} else if (rightPiece != null && rightPiece.getType() == PieceType.PAWN && rightPiece.getSide() == (imBlack ? PlaySide.BLACK : PlaySide.WHITE)) {
				sourceNewMove = rightPiece.getSrcString();
				return ((Pawn) rightPiece).performEnPassant(board, xDestLastMove, yDestLastMove, sourceNewMove, destinationNewMove);
			}
		}

		return null;
	}

	/**
	 * Method used to register a move with the capability to undo the move
	 *
	 * @param move the move to simulate
	 */
	public void doMove(Move move) {
		if (!move.isNormal() && !move.isDropIn() && !move.isPromotion()) return;

		int srcY = move.getSourceY();
		int srcX = move.getSourceX();
		int dstY = move.getDestinationY();
		int dstX = move.getDestinationX();

		Piece srcPiece = board[srcX][srcY];
		Piece capturedPiece = board[dstX][dstY];
		if (srcPiece == null) return;
		simulatedMoves.push(move);

		// When destination is not null, it is a capture, simulate it and retain all the information
		if (capturedPiece != null) {
			move.markCapture();
			if (capturedPiece.getSide() == PlaySide.BLACK) {
				simulatedWhiteCaptures.push(capturedPiece);
				simulatedBlackPieces.remove(capturedPiece);
			} else {
				simulatedBlackCaptures.push(capturedPiece);
				simulatedWhitePieces.remove(capturedPiece);
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
			if (movedPiece.getSide() == PlaySide.WHITE) {
				capturedPiece = simulatedWhiteCaptures.pop();
				simulatedBlackPieces.add(capturedPiece);
			} else {
				capturedPiece = simulatedBlackCaptures.pop();
				simulatedWhitePieces.add(capturedPiece);
			}
			board[dstX][dstY] = capturedPiece;
		} else {
			board[dstX][dstY] = null;
		}
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

	/**
	 * Returns first capture on target piece
	 *
	 * @param target the piece to be captured
	 * @return the first capture on target piece, null if no capture is possible
	 */
	public Piece getCaptureOnPiece(Piece target) {
		for (Piece piece : getOpposites(target.getSide())) {
			if (piece.canCapture(this, target.getX(), target.getY())) return piece;
		}
		return null;
	}

	/**
	 * Returns first capture on simulated target piece
	 *
	 * @param target the piece to be captured
	 * @return the first capture on simulated target piece, null if no capture is possible
	 */
	public Piece getCaptureOnSimulatedPiece(Piece target) {
		for (Piece piece : getSimulatedOpposites(target.getSide())) {
			if (piece.canCapture(this, target.getX(), target.getY())) return piece;
		}
		return null;
	}

	/**
	 * Returns all possible captures on target piece that do not result in the king being in check
	 *
	 * @param target the piece to be captured
	 * @return all possible captures on target piece, empty list if no capture is possible
	 */
	public ArrayList<Move> getAllCapturesOnPiece(Piece target) {
		ArrayList<Move> moves = new ArrayList<>();

		// My king is the opposite of the target
		Piece myKing = getOppositeKing(target.getSide());
		for (Piece piece : getOpposites(target.getSide())) {
			if (piece.canCapture(this, target.getX(), target.getY())) {
				Move potentialCapture = Move.moveTo(piece.getSrcString(), target.getSrcString());
				// Simulate the capture and check if the king is in check
				doMove(potentialCapture);
				// Can do capture if king isn't in check
				if (getCaptureOnSimulatedPiece(myKing) == null) moves.add(potentialCapture);
				undoMove();
			}
		}
		return moves;
	}

	/**
	 * Returns all possible moves between an attacker and a target that can prevent a check
	 *
	 * @param attacker the piece that is attacking
	 * @param target   the piece that is being attacked (mostly the king)
	 * @return moves to prevent a capture from attacker to target
	 */
	public ArrayList<Move> getAllBlocksBetween(Piece attacker, Piece target) {
		ArrayList<Move> moves = new ArrayList<>();

		int verticalDist = attacker.getX() - target.getX();
		int horizontalDist = attacker.getY() - target.getY();
		int xDir = (int) Math.signum(verticalDist);
		int yDir = (int) Math.signum(horizontalDist);

		// If the Knight or the pawn is the one attacking, there is no way to block it.
		if (target.getType() == PieceType.KNIGHT || target.getType() == PieceType.PAWN) return moves;

		for (int i = 1; i < Math.max(Math.abs(horizontalDist), Math.abs(verticalDist)); i++) {
			int xPositionToBlock = target.getX() + i * xDir;
			int yPositionToBlock = target.getY() + i * yDir;

			// If the position is empty, we can try to block it
			if (getPiece(xPositionToBlock, yPositionToBlock) == null) {
				// Check if after the move of every target pieces, any of the attacker pieces can still attack the target
				// and choose only the one that truly blocks the attack
				for (Piece piece : getSame(target.getSide())) {
					if (piece.getType() != PieceType.KING && piece.canMove(this, xPositionToBlock, yPositionToBlock)) {
						String dstString = Piece.getDstString(xPositionToBlock, yPositionToBlock);
						Move potentialMove = Move.moveTo(piece.getSrcString(), dstString);

						// Simulate the capture and check if the target can be captured
						doMove(potentialMove);
						// Can do capture if king isn't in check
						if (getCaptureOnSimulatedPiece(target) == null) moves.add(potentialMove);
						undoMove();
					}
				}
			}
		}

		return moves;
	}

	public ArrayList<Move> getAllBlocksUsingDropIns(Piece target) {
		ArrayList<Move> moves = new ArrayList<>();
		// My king is the opposite of the target
		Piece myKing = getOppositeKing(target.getSide());

		// If there is a double check we can't escape by dropping a piece.
		if (getAllCapturesOnPiece(myKing).size() > 1) return moves;

		int verticalDist = myKing.getX() - target.getX();
		int horizontalDist = myKing.getY() - target.getY();
		int xDir = (int) Math.signum(verticalDist);
		int yDir = (int) Math.signum(horizontalDist);

		// If the Knight or the pawn is the one attacking, there is no way to block it.
		if (target.getType() == PieceType.KNIGHT || target.getType() == PieceType.PAWN) return moves;

		for (int i = 1; i < Math.max(Math.abs(horizontalDist), Math.abs(verticalDist)); i++) {
			int xPositionToBlock = target.getX() + i * xDir;
			int yPositionToBlock = target.getY() + i * yDir;
			if (getPiece(xPositionToBlock, yPositionToBlock) == null) {
				String dstString = Piece.getDstString(xPositionToBlock, yPositionToBlock);
				for (Piece piece : (myKing.getSide() == PlaySide.WHITE) ? whiteCaptures : blackCaptures) {
					if (piece.getType() == PieceType.KING) continue;
					Move potentialMove = Move.dropIn(dstString, piece.getType());
					moves.add(potentialMove);
				}
			}
		}

		return moves;
	}

	/**
	 * Returns all possible drop ins on a table
	 *
	 * @param side the side which drops in
	 * @return all possible drop ins on a table
	 */
	public ArrayList<Move> getAllDropIns(PlaySide side) {
		ArrayList<Move> moves = new ArrayList<>();

		for (Piece piece : getSameCaptures(side)) {
			for (int i = 1; i <= 8; i++) {
				for (int j = 1; j <= 8; j++) {
					if (getPiece(i, j) != null) continue;
					Move potentialMove = Move.dropIn(Piece.getDstString(i, j), piece.getType());
					moves.add(potentialMove);
				}
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

		// Setting up my pieces and the other player pieces
		ArrayList<Piece> mine = getSame(side);
		Piece myKing = getSameKing(side);

		// If king is in chess, try to capture the problem piece or to block with another piece
		Piece attackingPiece = getCaptureOnPiece(myKing);
		if (attackingPiece != null) {
			// Priority 1 try to capture the problem piece using other pieces
			allPossibleMoves.addAll(getAllCapturesOnPiece(attackingPiece));
			if (allPossibleMoves.size() != 0) return chooseRandom(allPossibleMoves);

			// Priority 2 try to capture the problem piece or other pieces so that there is no more check
			allPossibleMoves.addAll(myKing.getPossibleCaptures(this));
			if (allPossibleMoves.size() != 0) return chooseRandom(allPossibleMoves);

			// Priority 3 try to move the king somewhere else
			allPossibleMoves.addAll(myKing.getPossibleMoves(this));
			if (allPossibleMoves.size() != 0) return chooseRandom(allPossibleMoves);

			// Priority 4 try to block the path of the attacking piece using drop in
			allPossibleMoves.addAll(getAllBlocksUsingDropIns(attackingPiece));
			if (allPossibleMoves.size() != 0) return chooseRandom(allPossibleMoves);

			// Priority 5 try to block the path of the attacking piece using pieces from the board.
			allPossibleMoves.addAll(getAllBlocksBetween(attackingPiece, myKing));
			if (allPossibleMoves.size() != 0) return chooseRandom(allPossibleMoves);

			return Move.resign();
		}

		// Checking if we can do an en passant attack.
		Move enPassantMove = generateEnPassantMove();
		if (enPassantMove != null) return enPassantMove;


		allPossibleMoves.addAll(getAllDropIns(side));
		if (allPossibleMoves.size() != 0) return chooseRandom(allPossibleMoves);

		// First generate all captures and choose one if there are valid captures
		for (Piece piece : mine) allPossibleMoves.addAll(piece.getPossibleCaptures(this));
		if (allPossibleMoves.size() != 0) return chooseRandom(allPossibleMoves);

		// If no captures are valid, generate all moves and choose one
		for (Piece piece : mine) allPossibleMoves.addAll(piece.getPossibleMoves(this));
		if (allPossibleMoves.size() != 0) return chooseRandom(allPossibleMoves);

		// TODO: Move Castling here if you want to test it (works only human play with the bot).
		// TODO further review for illegal moves between 2 bots
		// Castling commented because it is not working properly need revision
		Piece shortCastleRook = board[1][8];
		Piece longCastleRook = board[1][1];

		if (shortCastleRook != null && shortCastleRook.getType() == PieceType.ROOK
				&& canCastle((Rook) shortCastleRook, (King) whiteKing, "short")) { // short castle
			return Move.moveTo(whiteKing.getSrcString(),
					Piece.getDstString(whiteKing.getX(), whiteKing.getY() + CASTLING_DISTANCE));
		} else if (longCastleRook != null && longCastleRook.getType() == PieceType.ROOK
				&& canCastle((Rook) longCastleRook, (King) whiteKing, "long")) { // long castle
			return Move.moveTo(whiteKing.getSrcString(),
					Piece.getDstString(whiteKing.getX(), whiteKing.getY() - CASTLING_DISTANCE));
		}

		// If no moves are valid, resign, for now
		return Move.resign();
	}
}
