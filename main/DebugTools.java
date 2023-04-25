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
}
