import Pieces.*;

import java.util.ArrayList;

public class Board {
	private Piece[][] board;
	private ArrayList<Piece> myPieces = new ArrayList<Piece>();
	private ArrayList<Piece> enemyPieces = new ArrayList<Piece>();

	public void newBoard() {
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
		board[1][2] = new Knight(true, PieceType.KNIGHT, 1, 2);
		board[1][7] = new Knight(true, PieceType.KNIGHT, 1, 7);
		board[8][2] = new Knight(false, PieceType.KNIGHT, 8, 2);
		board[8][7] = new Knight(false, PieceType.KNIGHT, 8, 7);

		// Setting up the bishops
		board[1][3] = new Bishop(true, PieceType.BISHOP, 1, 3);
		board[1][6] = new Bishop(true, PieceType.BISHOP, 1, 6);
		board[8][3] = new Bishop(false, PieceType.BISHOP, 8, 3);
		board[8][6] = new Bishop(false, PieceType.BISHOP, 8, 6);

		// Setting up the queens
		board[1][4] = new Queen(true, PieceType.QUEEN, 1, 4);
		board[8][4] = new Queen(false, PieceType.QUEEN, 8, 4);

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

	public Piece getPiece(int x, int y) {
		return board[x][y];
	}

	public Piece getPiece(String source) {
		int x = source.charAt(0) - 'a' + 1;
		int y = Integer.parseInt(source.substring(1, 2));
		return board[x][y];
	}

	public boolean movePiece(Piece piece, int xDest, int yDest) {
		if (!piece.canMove(board, xDest, yDest)) return false;
		board[piece.x][piece.y] = null;
		board[xDest][yDest] = piece;
		piece.x = xDest;
		piece.y = yDest;
		return true;
	}

	public Move getRandMove() {
		for (Piece piece : myPieces) {
			Move move = piece.suggestRandomMove(board);
			if (move != null) return move;
		}
		System.out.println("No moves found");
		return null;
	}
}
