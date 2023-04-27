package main;

import pieces.*;

import java.util.ArrayList;
import java.util.Random;

public class Board {
	private Piece[][] board;
	private boolean imBlack;
	private ArrayList<Piece> whites = new ArrayList<Piece>();
	private ArrayList<Piece> blacks = new ArrayList<Piece>();
	private ArrayList<Piece> whiteCaptures = new ArrayList<Piece>();
	private ArrayList<Piece> blacksCaptures = new ArrayList<Piece>();
	private Piece king;


	public Board() {
		board = new Piece[9][9];
		// Setting up the pawns
		for (int i = 1; i <= 8; i++) {
			board[2][i] = new Pawn(PlaySide.WHITE, PieceType.PAWN, 2, i);
		}

		for (int i = 1; i <= 8; i++) {
			board[7][i] = new Pawn(PlaySide.BLACK, PieceType.PAWN, 7, i);
		}

		// Setting up the rooks
		board[1][1] = new Rook(PlaySide.WHITE, PieceType.ROOK, 1, 1);
		board[1][8] = new Rook(PlaySide.WHITE, PieceType.ROOK, 1, 8);
		board[8][1] = new Rook(PlaySide.BLACK, PieceType.ROOK, 8, 1);
		board[8][8] = new Rook(PlaySide.BLACK, PieceType.ROOK, 8, 8);

		// Setting up the knights
		board[1][2] = new Knight(PlaySide.WHITE, PieceType.KNIGHT, 1, 2);
		board[1][7] = new Knight(PlaySide.WHITE, PieceType.KNIGHT, 1, 7);
		board[8][2] = new Knight(PlaySide.BLACK, PieceType.KNIGHT, 8, 2);
		board[8][7] = new Knight(PlaySide.BLACK, PieceType.KNIGHT, 8, 7);

		// Setting up the bishops
		board[1][3] = new Bishop(PlaySide.WHITE, PieceType.BISHOP, 1, 3);
		board[1][6] = new Bishop(PlaySide.WHITE, PieceType.BISHOP, 1, 6);
		board[8][3] = new Bishop(PlaySide.BLACK, PieceType.BISHOP, 8, 3);
		board[8][6] = new Bishop(PlaySide.BLACK, PieceType.BISHOP, 8, 6);

		// Setting up the queens
		board[1][4] = new Queen(PlaySide.WHITE, PieceType.QUEEN, 1, 4);
		board[8][4] = new Queen(PlaySide.BLACK, PieceType.QUEEN, 8, 4);

		// Setting up the kings
		board[1][5] = new King(PlaySide.WHITE, PieceType.KING, 1, 5);
		board[8][5] = new King(PlaySide.BLACK, PieceType.KING, 8, 5);

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

	public Piece[][] getBoard() {
		return board;
	}

	public Piece getPiece(int x, int y) {
		return board[x][y];
	}

	public Piece getPiece(String source) {
		int x = source.charAt(0) - 'a' + 1;
		int y = Integer.parseInt(source.substring(1, 2));
		return board[x][y];
	}

	public boolean checkIfEnPassantIsEnabled(int srcX, int srcY, int dstX, int dstY) {
		System.out.println("### positions in enpassant check");
		System.out.println(srcX);
		System.out.println(srcY);
		System.out.println(dstX);
		System.out.println(dstY);
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

	public void registerMove(Move move) {
		if (!move.isNormal() && !move.isDropIn() && !move.isPromotion()) return;
//		int srcY = move.getSource().get().charAt(0) - 'a' + 1;
//		int srcX = move.getSource().get().charAt(1) - '0';
//		int dstY = move.getDestination().get().charAt(0) - 'a' + 1;
//		int dstX = move.getDestination().get().charAt(1) - '0';
		int srcX = move.getSourceX();
		int srcY = move.getSourceY();
		int dstX = move.getDestinationX();
		int dstY = move.getDestinationY();

		Piece srcPiece = board[srcX][srcY];
		Piece dstPiece = board[dstX][dstY];
		if (srcPiece == null) return;

		// Handle En Passant
		if (move.isNormal() && srcPiece.getType() == PieceType.PAWN
				&& srcPiece.side == (imBlack ? PlaySide.WHITE : PlaySide.BLACK)
				&& checkIfEnPassantIsEnabled(srcX, srcY, dstX, dstY)) {
			move.setEnablesEnPassant(true);
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
			dstPiece.captured = true;
			dstPiece.x = dstPiece.y = -1;
		}

		srcPiece.x = dstX;
		srcPiece.y = dstY;
		board[dstX][dstY] = srcPiece;
		board[srcX][srcY] = null;
	}

	public Move getRandMove() {
		Move lastMove = Bot.getLastMove();

		if (lastMove != null && lastMove.isEnablesEnPassant()) {
			int xDestLastMove = lastMove.getDestinationX();
			int yDestLastMove = lastMove.getDestinationY();
			String sourceNewMove = null;
			String destinationNewMove = Piece.getDstString(xDestLastMove + 1, yDestLastMove);

			Piece leftPiece = board[xDestLastMove][yDestLastMove - 1];
			Piece rightPiece = board[xDestLastMove][yDestLastMove + 1];

			// Looking for a pawn that can do en passant.
			if (leftPiece != null && leftPiece.getType() == PieceType.PAWN) {
				sourceNewMove = leftPiece.getSrcString();
				return ((Pawn)leftPiece).performEnPassant(board, xDestLastMove, yDestLastMove, sourceNewMove, destinationNewMove);
			} else if (rightPiece != null && rightPiece.getType() == PieceType.PAWN) {
				sourceNewMove = rightPiece.getSrcString();
				return ((Pawn)rightPiece).performEnPassant(board, xDestLastMove, yDestLastMove, sourceNewMove, destinationNewMove);
			}
		}

		ArrayList<Move> allPossibleMoves = new ArrayList<>();

		for (Piece piece : whites) {
			ArrayList<Move> move = piece.getAllMoves(board);
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

