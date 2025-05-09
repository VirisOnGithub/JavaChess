package javachess;

import javachess.events.*;
import javachess.pieces.Pawn;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Game extends Observable {
    private final ArrayList<Player> players;
    protected Move move;
    private final Board board;
    private boolean gameDone = false;
    private int actualPlayer = 0;
    public Piece promoteTo = null;
    private final HashMap<String, Integer> history = new HashMap<>();
    private int fiftyMoveRuleCounter = 0;
    protected final ConfigParser configParser;
    protected final LanguageService languageService;

    public Game(){
        this.configParser = new ConfigParser();
        board = new Board();
        players = new ArrayList<>();
        languageService = new LanguageService();
        languageService.setLanguage(configParser.getLanguage());
    }

    public Game(Board board){
        this.configParser = new ConfigParser();
        this.board = board;
        players = new ArrayList<>();
        languageService = new LanguageService();
        languageService.setLanguage(configParser.getLanguage());
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

    public Player getNextPlayer() {
        return players.get((actualPlayer + 1) % players.size());
    }

    public Player getPreviousPlayer() {
        return players.get((actualPlayer - 1) % players.size());
    }

    public void playGame(){
        players.add(new Player(this, PieceColor.WHITE));
        players.add(new Player(this, PieceColor.BLACK));

        notifyAll(new SoundEvent("game-start"));

        while(!gameDone){
            Player currentPlayer = getCurrentPlayer();
            PieceColor playerColor = currentPlayer.getColor();
            notifyAll(new ChangePlayerEvent(playerColor));
            if (board.cannotMoveWithoutMate(playerColor)) {
                notifyAll(new SoundEvent("game-end"));
                notifyAll(board.isCheck(playerColor) ? new CheckMateEvent(getPreviousPlayer().getColor()) : new PatEvent());
                gameDone = true;
                break;
            }
            if (board.isCheck(playerColor)) {
                notifyAll(new CheckEvent());
            }
            // handle positionHistory
            int getPositionHistoryNumber = getPositionHistoryNumber();
            if (getPositionHistoryNumber > 2) {
                notifyAll(new SoundEvent("game-end"));
                notifyAll(new DrawEvent("The game is a draw due to the threefold repetition rule."));
                gameDone = true;
                break;
            }
            if (fiftyMoveRuleCounter >= 50) {
                notifyAll(new SoundEvent("game-end"));
                notifyAll(new DrawEvent("The game is a draw due to the fifty-move rule."));
                gameDone = true;
                break;
            }
            boolean successMove;
            do {
                Move move = currentPlayer.getMove();
                successMove = setMove(move.getFrom(), move.getTo(), false);
            } while (!successMove);
            actualPlayer++;
        }
    }

    private int getPositionHistoryNumber() {
        return history.compute(board.getIdString(), (key, value) -> value == null ? 1 : value + 1);
    }

    private void incrementFiftyMoveRuleCounter() {
        fiftyMoveRuleCounter++;
    }

    private void resetFiftyMoveRuleCounter() {
        fiftyMoveRuleCounter = 0;
    }

    public int getFiftyMoveRuleCounter() {
        return fiftyMoveRuleCounter;
    }

    private void notifyAll(Event event){
        setChanged();
        notifyObservers(event);
    }

    public boolean setMove(Position from, Position to, boolean castling) {
        boolean soundPlayed = false;
        move = new Move(from, to);
        Cell fromCell = board.getCells().get(from);
        Cell toCell = board.getCells().get(to);
        Piece pieceFrom = fromCell.getPiece();
        Piece pieceTo = toCell.getPiece();
        // should never happen with the current move system
        if (pieceFrom == null) {
            System.err.println("Invalid move: No piece at the source position.");
            return false;
        }

        if (pieceFrom.getColor() != getCurrentPlayer().getColor()) {
            System.err.println("Invalid move: The piece does not belong to the current player.");
            notifyAll(new SoundEvent("illegal"));
            soundPlayed = true;
            return false;
        }

        if (!board.getValidCellsForBoard(fromCell.getPiece()).contains(toCell) && !castling) {
            System.out.println(String.valueOf(board.getValidCellsForBoard(fromCell.getPiece())
                    .stream()
                    .map(Object::hashCode)
                    .collect(Collectors.toCollection(ArrayList::new))));
            System.out.println("\n");
            System.out.println(toCell.hashCode());
            System.err.println("Invalid move: The destination cell is not valid for the selected piece.");
            notifyAll(new SoundEvent("illegal"));
            soundPlayed = true;
            return false;
        }

        if (pieceTo != null) {
            if(pieceFrom.getColor() == pieceTo.getColor()){
                System.err.println("Invalid move: The destination cell contains a piece of the same color.");
                notifyAll(new SoundEvent("illegal"));
                return false;
            }
        }
        Move lastMove = board.getLastMove();
        board.applyMove(move, castling);
        if(pieceFrom.getType() == PieceType.PAWN && pieceTo != null){
            incrementFiftyMoveRuleCounter();
        } else {
            resetFiftyMoveRuleCounter();
        }
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

    public Player getCurrentPlayer() {
        return players.get(actualPlayer % players.size());
    }
}
