package main;

import pieces.Piece;

public class DebugTools {
	public static void printBoardPretty(Piece[][] board, boolean whiteDown) {
		int startX = whiteDown ? 8 : 1;
		int limitX = whiteDown ? 0 : 9;
		int stepX = whiteDown ? -1 : 1;

		int startY = whiteDown ? 1 : 8;
		int limitY = whiteDown ? 9 : 0;
		int stepY = whiteDown ? 1 : -1;

		for (int i = startX; i != limitX; i += stepX) {
			for (int j = startY; j != limitY; j += stepY) {
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
				if (board[i][j].getSide() == PlaySide.WHITE) {
					System.out.print("w");
				} else {
					System.out.print("b");
				}

				System.out.print(" ");
			}
			System.out.println();
		}
	}

	public static void printBoardPretty(Board board, boolean whiteDown) {
		printBoardPretty(board.getBoard(), whiteDown);
	}

	public static void printPawns(Piece[][] board) {
		for (int i = 1; i <= 8; i++) {
			for (int j = 1; j <= 8; j++) {
				if (board[i][j] != null && board[i][j].getType() == PieceType.PAWN) {
					System.out.println(board[i][j]);
				}
			}
		}
	}
}
