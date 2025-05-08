package javachess;

import javachess.events.*;
import javachess.pieces.*;

import java.util.*;

public class ConsoleChessDisplay implements Observer, EventVisitor {

    final Game game;
    private boolean gameDone = false;

    public ConsoleChessDisplay(Game game) {
        this.game = game;
        game.addObserver(this);
        displayBoard();
        mainLoop();
    }

    public static void main(String[] args) {
        Game game = new Game();
        ConsoleChessDisplay consoleDisplay = new ConsoleChessDisplay(game);
        game.playGame();
    }

    public void displayBoard() {
        clearConsole();
        BiMap<Position, Cell> cells = this.game.getBoard().getCells();
        StringBuilder sb = new StringBuilder();
        sb.append("   a  b  c  d  e  f  g  h \n");
        for(int i = 0; i < 8; i++) {
            sb.append(8 - i).append(" ");
            for (int j = 0; j < 8; j++) {
                sb.append((i+j) % 2 == 0 ? Colors.LIGHT_GRAY_BACKGROUND : Colors.DARK_GRAY_BACKGROUND);
                Position position = new Position(j, i);
                Cell cell = cells.get(position);
                Piece piece = cell.getPiece();
                if (piece != null) {
                    sb.append(" ")
                            .append(piece.getColor() == PieceColor.WHITE ? Colors.WHITE : Colors.BLACK)
                            .append(getPieceSymbol(piece))
                            .append(" ")
                            .append(Colors.RESET);
                } else {
                    sb.append("   ");
                }
                sb.append(Colors.RESET);
            }
            sb.append(Colors.RESET).append("\n");
        }
        System.out.println(sb);
    }

    private void mainLoop() {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (!gameDone) {
                System.out.println("Enter your move (e.g., e2 e4): ");
                String input = scanner.nextLine();
                String[] parts = input.split(" ");
                if (parts.length != 2) {
                    System.out.println("Invalid input. Please enter a valid move.");
                    continue;
                }
                Position from = Position.fromString(parts[0]);
                Position to = Position.fromString(parts[1]);
                synchronized (game) {
                    game.getCurrentPlayer().setMove(from, to);
                }
            }
        }).start();
    }

    private char getPieceSymbol(Piece piece) {
        PieceType type = piece.getType();
        return switch (type) {
            case PAWN -> '♟';
            case ROOK -> '♜';
            case KNIGHT -> '♞';
            case BISHOP -> '♝';
            case QUEEN -> '♛';
            case KING -> '♚';
        };
    }

    private void clearConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof Event event) {
            visit(event);
        }
    }

    @Override
    public void visit(CheckEvent event) {
        System.out.println("Check!");
    }

    @Override
    public void visit(CheckMateEvent event) {
        System.out.println("Checkmate! Winner: " + event.getWinnerColor());
        gameDone = true;
    }

    @Override
    public void visit(UpdateBoardEvent event) {
        displayBoard();
    }

    @Override
    public void visit(PromotionEvent event) {
        PieceColor color = event.getFrom().getX() == 0 ? PieceColor.WHITE : PieceColor.BLACK;
        System.out.println("Promotion! Choose a piece for " + color + ":");
        System.out.println("1. Queen");
        System.out.println("2. Rook");
        System.out.println("3. Bishop");
        System.out.println("4. Knight");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        Position piecePosition = event.getFrom();
        Cell pawnCell = game.getBoard().getCells().get(piecePosition);
        Piece pawn = pawnCell.getPiece();

        game.promoteTo = switch (choice) {
            case 2 -> new Rook(pawn.getColor(), pawnCell);
            case 3 -> new Bishop(pawn.getColor(), pawnCell);
            case 4 -> new Knight(pawn.getColor(), pawnCell);
            default -> new Queen(pawn.getColor(), pawnCell);
        };

        synchronized (game) {
            game.notify();
        }
    }

    @Override
    public void visit(DrawEvent event) {
        System.out.println("Draw! Reason: " + event.getReason());
        gameDone = true;
    }

    @Override
    public void visit(PatEvent event) {
        System.out.println("Stalemate!");
        gameDone = true;
    }

    @Override
    public void visit(ChangePlayerEvent event) {
        System.out.println("Current Player: " + event.getColor());
    }

    @Override
    public void visit(SoundEvent event) {
        // Sound events are not applicable in console display.
    }
}