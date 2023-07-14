# How to run

        -> Play against it: make play
        -> Play Verik vs Verik: make playbots
<hr>

# Description

        -> This project aims to create a bot that can play Crazyhouse Chess, a variant of chess
           where captured pieces can be placed back on the board as one's own.
<hr>

# Structure

        -> The bot is written in java and is divided into two packages. The main package
           contains the classes necessary for the internal representation of the game as well as
           the classes that deal with the communication with the xboard. In the piece package we
           have the representation of all the entities on the chessboard, including the
           directions in which they can move and if the moves are valid.

            >> Board: The Board class deals with the internal representation of the chessboard
               (captures and moves by both sides) as well as special moves (castling and el passat).
               Also here the moves that the bot sends to the xboard are calculated depending on the
               situation in which it is (chess or not), more details in the comments.
    
            >> Bot: Represents a simple implementation of a bot in a board that receives
               moves and calculates its own next move via the board.
    
            >> Move: Defines a set of methods and variables used to represnt a move in a board game
               It includes information avout the position of the piece being moved, the destiantion
               of the move, the type of piece being promoted, and whether or not the move results in
               a capture.
    
            >> Piece: Piece is the base class for all pieces in which we have the common parts of
               all entities. This is also where all the possible moves for a piece are generated.
    
            >> Pawn: This class is a representation of a pawn chess piece. It extends the Piece class
               and includes specific rules and logic for valid moves and captures for a pawn.
    
            >> Bishop: This class represents a bishop chess piece. It extends the abstract Piece class
               and implements the methods required for a bishop's movement and capture behaviour on the
               chess board.
    
            >> Knight(murgul): The scope of this class to represent a knight chess piece, with it's
               associated movements and rules. The class extends the abstract Piece class and overrides
               its methods to define the specific behaviour of knight on a chess board.

            >> Rook: This class is a representation of a rook chess piece. It represents the rook chess
               piece and contains its specific behaviour, such as the way it moves and captures pieces on
               the chessboard.

            >> Queen: This class represents a queen chess piece. It represents the queen chess piece and
               provides implementations fot he methods inherited from the superclass. The class contains
               two constructors, one with only the position and side parameters, and the other one that 
               adds an extra parameter, isPawn which is helping us for promotion

            >> King(Verik Suprem): This class is a subclass of the Piece class. It represents the king
               chess piece and includes methods for validating moves and getting move directions. The class
               has a moved field which is set to 'false' by default but can be set to 'true' when the king
               is moved for the first time which helps us for the castling special move.

        -> Special moves are handled in Board class:

            >> Castling: In order to perform Castling, first the non-castling checks are made (the king
               must not pass through the checker and he, together with the turn with which he makes the 
               castling, must not have been moved). We use the checkPositionSafe function to check the 
               positions between the king and the rook. To keep track of the king and rook moves we use
               boolean variables in each of the classes that we update when the pieces move. Here there 
               is a bug that may be from xboard. Castling is only done when playing against the bot, if 
               we put bots to play with each other, the match will be blocked when they try to cast.

            >> En Passant: to achieve the en passant movement we used the lastMove variable from the Bot,
               which we query to see if the last move was a pawn moved two positions. Thus, we could tell
               when en passant movement became valid. Next, to represent the movement on the board, I
               turned the diagonal move of the pawn into a normal move, and the back pawn (the captured
               one) is manually deleted and placed in the list of captured pieces.

# Responsabilities
        
        -> From the beginning of this project to the end, we worked on code-with-me, cheers to IntelliJ. 
           Adrian took care a lot of refactoring and many other aspects, Octavian implemented the special 
           moves and also took care of other aspects and Grigorie took care of testing and other aspects. 
           In the rest, the common methods were worked uniformly, each coming with an idea/implementation/solution.


# Github structure

        -> In our repository there are 4 branches, two of them are for the special movements of the parts, 
        one branch is experimental for new "innovations/implementations", and the main one is for the final code.
<hr>
