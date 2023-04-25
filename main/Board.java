package main;

import pieces.*;

import java.util.ArrayList;

public class Board {
	private Piece[][] board;
	private ArrayList<Piece> myPieces = new ArrayList<Piece>();
	private ArrayList<Piece> enemyPieces = new ArrayList<Piece>();

	public Board() {
		board = new Piece[9][9];

		// Setting up the pawns
		for (int i = 1; i <= 8; i++) {
			board[2][i] = new Pawn(true, PieceType.PAWN, 2, i);
		}

		for (int i = 1; i <= 8; i++) {
			board[7][i] = new Pawn(false, PieceType.PAWN, 7, i);
		}

		// Setting up the rooks
		board[1][1] = new Rook(true, PieceType.ROOK, 1, 1);
		board[1][8] = new Rook(true, PieceType.ROOK, 1, 8);
		board[8][1] = new Rook(false, PieceType.ROOK, 8, 1);
		board[8][8] = new Rook(false, PieceType.ROOK, 8, 8);

		// Setting up the knights
		board[1][2] = new Knight(imBlack, PieceType.KNIGHT, 1, 2);
		board[1][7] = new Knight(imBlack, PieceType.KNIGHT, 1, 7);
		board[8][2] = new Knight(!imBlack, PieceType.KNIGHT, 8, 2);
		board[8][7] = new Knight(!imBlack, PieceType.KNIGHT, 8, 7);

		// Setting up the bishops
		board[1][3] = new Bishop(imBlack, PieceType.BISHOP, 1, 3);
		board[1][6] = new Bishop(imBlack, PieceType.BISHOP, 1, 6);
		board[8][3] = new Bishop(!imBlack, PieceType.BISHOP, 8, 3);
		board[8][6] = new Bishop(!imBlack, PieceType.BISHOP, 8, 6);

		// Setting up the queens
//		board[1][4] = new Queen(true, PieceType.QUEEN, 1, 4);
//		board[8][4] = new Queen(false, PieceType.QUEEN, 8, 4);

		// Setting up the kings
		board[1][5] = new King(true, PieceType.KING, 1, 5);
		board[8][5] = new King(false, PieceType.KING, 8, 5);

		// Setting up the pieces
		for (int i = 1; i <= 8; i++) {
			for (int j = 1; j <= 8; j++) {
				if (board[i][j] != null) {
					if (board[i][j].isMine) {
						myPieces.add(board[i][j]);
					} else {
						enemyPieces.add(board[i][j]);
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

	public boolean movePiece(Move move) {
		int sourceY = move.getSource().get().charAt(0) - 'a' + 1;
		int sourceX = move.getSource().get().charAt(1) - '0';
		System.out.println("Source: " + move.getSource());
		int destY = move.getDestination().get().charAt(0) - 'a' + 1;
		int destX = move.getDestination().get().charAt(1) - '0';
		System.out.println("Dest: " + move.getDestination());
		Piece piece = board[sourceX][sourceY];

		if (piece == null) {
			System.out.println("No piece found at " + sourceX + ", " + sourceY);
			return false;
		}

		board[piece.x][piece.y] = null;
		board[destX][destY] = piece;
		piece.x = destX;
		piece.y = destY;

		return true;
	}

	public Move getRandMove() {
		System.out.println(">>>>>>> Random move <<<<<<<");
		DebugTools.printBoard(board);
		for (Piece piece : myPieces) {
			Move move = null;
			if (piece.isMine && piece.getType() == PieceType.PAWN) {
				System.out.println(">>>>>>>> SOURCE: " + piece.getSrcString());
				 move = piece.suggestRandomMove(board);
			}

			if (move != null) {
				return move;
			}
		}

		System.out.println("No moves found");
		return null;
	}
}
