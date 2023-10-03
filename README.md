# How to Run

- Play against the bot: `make play`
- Play Verik vs. Verik: `make playbots`

---

# Description

This project aims to create a bot that can play Crazyhouse Chess, a variant of chess where captured pieces can be placed back on the board as one's own.

---

# Structure

## Main Package

The bot is written in Java and is divided into two packages. The main package contains the classes necessary for the internal representation of the game, as well as the classes that deal with communication with the xboard.

- **Board:** The Board class deals with the internal representation of the chessboard (captures and moves by both sides) as well as special moves (castling and en passant). It calculates moves sent to the xboard based on the game situation.

- **Bot:** Represents a simple implementation of a bot on a chessboard that receives moves and calculates its own next move via the board.

- **Move:** Defines a set of methods and variables used to represent a move in a board game. It includes information about the position of the piece being moved, the destination of the move, the type of piece being promoted, and whether or not the move results in a capture.

- **Piece:** The base class for all chess pieces, containing common attributes and methods to generate possible moves for a piece.

- **Pawn:** Represents a pawn chess piece, including specific rules and logic for valid moves and captures.

- **Bishop:** Represents a bishop chess piece with its movement and capture behavior.

- **Knight (Murgul):** Represents a knight chess piece with associated movements and rules.

- **Rook:** Represents a rook chess piece, including how it moves and captures pieces on the chessboard.

- **Queen:** Represents a queen chess piece and provides implementations for inherited methods. Includes constructors for position and side, and an optional parameter for pawn promotion.

- **King (Verik Suprem):** Represents a king chess piece with methods for validating moves and move directions. Tracks whether the king has moved for castling.

## Special Moves

Special moves like castling and en passant are handled in the Board class:

- **Castling:** Castling checks are made to ensure the king doesn't pass through check, and both king and rook haven't moved. A bug related to xboard is noted. Castling is only possible when playing against the bot.

- **En Passant:** En passant is determined by checking the lastMove variable from the Bot to identify if the last move was a pawn moved two positions. It's then represented on the board by converting the diagonal pawn move into a normal move.

---

# Responsibilities

Throughout the project, we collaborated using code-with-me in IntelliJ. Responsibilities were divided as follows:

- Adrian: Refactoring and various aspects
- Octavian: Special moves implementation and other tasks
- Grigorie: Testing and other aspects

Common methods and ideas were contributed uniformly.

---

# GitHub Structure

In our GitHub repository, there are four branches:

1. Two branches for special piece movements
2. One experimental branch for new innovations and implementations
3. The main branch for the final code

---
