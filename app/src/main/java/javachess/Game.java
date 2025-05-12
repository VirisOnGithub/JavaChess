package javachess;

import javachess.events.*;
import javachess.pieces.Pawn;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Scanner;

/**
 * Class that represents a chess game.
 * It contains the players, the board, and the game logic.
 */
public class Game extends Observable {
    private final ArrayList<Player> players;
    protected Move move;
    private final Board board;
    private boolean gameDone = false;
    protected int actualPlayer = 0;
    public Piece promoteTo = null;
    private final HashMap<String, Integer> history = new HashMap<>();
    private int fiftyMoveRuleCounter = 0;
    protected final ConfigParser configParser;
    protected final LanguageService languageService;

    /**
     * Constructor for the Game class.
     * Initializes the board, players, and language service.
     */
    public Game(){
        this.configParser = new ConfigParser();
        board = new Board();
        players = new ArrayList<>();
        languageService = new LanguageService();
        languageService.setLanguage(configParser.getLanguage());
        addTwoPlayers();
    }

    /**
     * Constructor for the Game class.
     * @param board The board to be used in the game.
     */
    public Game(Board board){
        this.configParser = new ConfigParser();
        this.board = board;
        players = new ArrayList<>();
        languageService = new LanguageService();
        languageService.setLanguage(configParser.getLanguage());
        addTwoPlayers();
    }

    public static void main(String[] args) {
        System.out.println("Choose display mode: 1 for console, 2 for GUI");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        if (choice == 1) {
            Game game = new Game();
            ConsoleChessDisplay consoleDisplay = new ConsoleChessDisplay(game);
            game.playGame();
        } else if (choice == 2) {
            SwingUtilities.invokeLater(() -> {
                new ChessGameMenu().setVisible(true);
            });
        } else {
            System.out.println("Invalid choice. Exiting.");
            return;
        }
    }

    public Move getMove() {
        return move;
    }

    public Board getBoard() {
        return board;
    }

    /**
     * Shortcut method to add two players to the game.
     */
    private void addTwoPlayers(){
        players.add(new Player(this, PieceColor.WHITE));
        players.add(new Player(this, PieceColor.BLACK));
    }

    /**
     * Get the next player without changing the actual player.
     * @return The next player.
     */
    public Player getNextPlayer() {
        return players.get((actualPlayer + 1) % players.size());
    }

    /**
     * Get the previous player without changing the actual player.
     * @return The previous player.
     */
    public Player getPreviousPlayer() {
        return players.get((actualPlayer - 1) % players.size());
    }

    /**
     * Main method to play the game.
     */
    public void playGame(){
        notifyAll(new SoundEvent("game-start"));

        while(!gameDone){
            Player currentPlayer = getCurrentPlayer();
            PieceColor playerColor = currentPlayer.getColor();
            notifyAll(new ChangePlayerEvent(playerColor));

            // first check if there check or checkmate
            if (board.cannotMoveWithoutMate(playerColor)) {
                notifyAll(new SoundEvent("game-end"));
                notifyAll(board.isCheck(playerColor) ? new CheckMateEvent(getPreviousPlayer().getColor()) : new StalemateEvent());
                gameDone = true;
                break;
            }
            if (board.isCheck(playerColor)) {
                notifyAll(new CheckEvent());
            }
            // handle positionHistory
            int getPositionHistoryNumber = getPositionHistoryNumber(playerColor);
            if (getPositionHistoryNumber > 2) {
                notifyAll(new SoundEvent("game-end"));
                notifyAll(new DrawEvent("The game is a draw due to the threefold repetition rule."));
                gameDone = true;
                break;
            }

            // handle fifty move rule (https://en.wikipedia.org/wiki/Fifty-move_rule)
            if (fiftyMoveRuleCounter >= 50) {
                notifyAll(new SoundEvent("game-end"));
                notifyAll(new DrawEvent("The game is a draw due to the fifty-move rule."));
                gameDone = true;
                break;
            }
            boolean successMove;
            do {
                Move move = currentPlayer.getMove();
                successMove = setMove(move.from(), move.to(), false);
            } while (!successMove);
            actualPlayer++;
        }
    }

    /**
     * Get the number of times the current position has been played.
     * @param color The color of the player.
     * @return The number of times the current position has been played.
     */
    private int getPositionHistoryNumber(PieceColor color) {
        return history.compute(board.getSimplifiedFEN(color), (key, value) -> value == null ? 1 : value + 1);
    }

    private void incrementFiftyMoveRuleCounter() {
        fiftyMoveRuleCounter++;
    }

    private void resetFiftyMoveRuleCounter() {
        fiftyMoveRuleCounter = 0;
    }

    private void notifyAll(Event event){
        setChanged();
        notifyObservers(event);
    }

    /**
     * Set the move for the game.
     * @param from The position from which the piece is moved.
     * @param to The position to which the piece is moved.
     * @param castling True if the move is a castling move, false otherwise. It is false by default.
     * @return True if the move is valid, false otherwise.
     */
    public boolean setMove(Position from, Position to, boolean castling) {
        boolean soundPlayed = false;
        move = new Move(from, to);
        Cell fromCell = board.getCells().get(from);
        Cell toCell = board.getCells().get(to);
        Piece pieceFrom = fromCell.getPiece();
        Piece pieceTo = toCell.getPiece();

        // should never happen with the current move system
        if (pieceFrom == null) {
            System.err.println("Invalid move: No piece at the source position. Position1: " + from + " Position2: " + to);
            return false;
        }

        if (pieceFrom.getColor() != getCurrentPlayer().getColor()) {
            System.err.println("Invalid move: The piece does not belong to the current player.");
            notifyAll(new SoundEvent("illegal"));
            soundPlayed = true;
            return false;
        }

        // check if the move is valid for the piece (check if the decorator is valid)
        if (!board.getValidCellsForBoard(fromCell.getPiece()).contains(toCell) && !castling) {
//            System.out.println(String.valueOf(board.getValidCellsForBoard(fromCell.getPiece())
//                    .stream()
//                    .map((e) -> board.getCells().getReverse(e))
//                    .collect(Collectors.toCollection(ArrayList::new))));
//            System.out.println("\n");
//            System.out.println(board.getCells().getReverse(toCell));
//            System.out.println("from: " + board.getCells().getReverse(fromCell));
            System.err.println("Invalid move: The destination cell is not valid for the selected piece.");
            notifyAll(new SoundEvent("illegal"));
            return false;
        }

        // check if the colors of the pieces are the same (a piece cannot take pieces of the same color)
        if (pieceTo != null && pieceFrom.getColor() == pieceTo.getColor()){
            System.err.println("Invalid move: The destination cell contains a piece of the same color.");
            notifyAll(new SoundEvent("illegal"));
            return false;
        }

        Move lastMove = board.getLastMove();
        board.applyMove(move, castling);

        // check if there has been a pawn move or a capture
        if (pieceFrom.getType() != PieceType.PAWN && pieceTo == null) {
            incrementFiftyMoveRuleCounter();
        } else {
            resetFiftyMoveRuleCounter();
        }

        System.out.println("FEN :" + getFEN());

        // handle castling
        if (pieceFrom.getType() == PieceType.KING && Math.abs(from.getX() - to.getX()) > 1) {
            System.out.println("Castling");
            notifyAll(new SoundEvent("castle"));

            Position rookFrom = new Position(to.getX() > from.getX() ? 7 : 0, from.getY());
            Position rookTo = new Position(to.getX() + (to.getX() > from.getX() ? -1 : 1), from.getY());
            Piece rook = board.getCells().get(rookFrom).getPiece();
            if (rook != null && rook.getType() == PieceType.ROOK && rook.getColor() == pieceFrom.getColor()) {
                setMove(rookFrom, rookTo, true);
            }
        }

        boolean enPassant = false;

        // handle en passant

        if (pieceFrom.getType() == PieceType.PAWN && Math.abs(from.getX() - to.getX()) == 1 && Math.abs(from.getY() - to.getY()) == 1) {
            enPassant = true;

            Position enPassantPosition = new Position(to.getX(), from.getY());
            Cell enPassantCell = board.getCells().get(enPassantPosition);

            if (enPassantCell != null && enPassantCell.getPiece() instanceof Pawn && lastMove.getMiddlePosition().equals(to)) {
                Piece enPassantPiece = enPassantCell.getPiece();
                if (enPassantPiece.getColor() != pieceFrom.getColor()) {
                    enPassantCell.setPiece(null);
                }
            }
        }

        // handle promotion
        if (pieceFrom.getType() == PieceType.PAWN && (to.getY() == 0 || to.getY() == 7)) {
            notifyAll(new SoundEvent("promote"));
            soundPlayed = true;
            notifyAll(new UpdateBoardEvent()); // show the pawn reaching the end
            notifyAll(new PromotionEvent(to));
            if (promoteTo == null) {
                synchronized (this) {
                    try {
                        // waiting for the player to choose the piece
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Cell pawnCell = board.getCells().get(to);
            pawnCell.setPiece(promoteTo);
            promoteTo.setCell(pawnCell);
        }

        /*
            * now we check which sound to play
            * this order is based on the chess.com order
         */

        if(!soundPlayed && board.isCheck(getNextPlayer().getColor())){
            notifyAll(new SoundEvent("move-check"));
            soundPlayed = true;
        }

        if(!soundPlayed && (pieceTo != null || enPassant)){
            notifyAll(new SoundEvent("capture"));
            soundPlayed = true;
        }

        if(!soundPlayed){
            notifyAll(new SoundEvent("move-self"));
        }

        notifyAll(new UpdateBoardEvent());
        return true;
    }

    /**
     * Get the current player.
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return players.get(actualPlayer % players.size());
    }

    /**
     * Get the FEN string of the current board state.
     */
    public String getFEN() {
        return board.getFEN(fiftyMoveRuleCounter, getCurrentPlayer().getColor());
    }
}
