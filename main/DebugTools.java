package main;

import pieces.Piece;

public class DebugTools {

	public static void printBoard(Board board) {
		for (int i = 1; i <= 8; i++) {
			for (int j = 1; j <= 8; j++) {
				Piece piece = board.getPiece(i, j);
				if (piece != null) {
					System.out.println(piece.getType() + " " + i + " " + j + " Mine ? : " + piece.isMine);
				} else {
					System.out.println("Empty position: " + i + " " + j);
				}
			}
		}
	}

	public static void printBoard(Piece[][] board) {
		System.out.println("Printing board");
		System.out.println("  a b c d e f g h");
		for (int i = 1; i <= 8; i++) {
			System.out.print(i + " ");
			for (int j = 1; j <= 8; j++) {
				Piece piece = board[i][j];
				if (piece != null) {
					System.out.print(piece.getType() + " ");
				} else {
					System.out.print("  ");
				}
			}
			System.out.println();
		}
	}

	public static void printBoardPretty(Piece[][] board) {
		for (int i = 1; i <=8 ; i++) {
			for (int j = 1; j <= 8; j++) {
				if (board[i][j] == null) {
					System.out.print("*  ");
					continue;
				}

				switch (board[i][j].getType()) {
					case PAWN:
						System.out.print("P");
						break;
					case ROOK:
						System.out.print("R");
						break;
					case KNIGHT:
						System.out.print("N");
						break;
					case BISHOP:
						System.out.print("B");
						break;
					case QUEEN:
						System.out.print("Q");
						break;
					case KING:
						System.out.print("K");
						break;
				}
				if (board[i][j].isMine) {
					System.out.print("m");
				} else {
					System.out.print("o");
				}

				System.out.print(" ");
			}
			System.out.println();
		}
	}
}
