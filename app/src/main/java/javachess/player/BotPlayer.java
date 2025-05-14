package javachess.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import javachess.*;
import javachess.pieces.Bishop;
import javachess.pieces.Knight;
import javachess.pieces.Queen;
import javachess.pieces.Rook;
import org.json.JSONObject;

public class BotPlayer implements Player {
    private final Game game;
    private final PieceColor color;
    private int depth;
    private String promoteTo = null;

    /**
     * Base URL for the Stockfish API.event.getColor())
     */
    private static final String BASE_URL = "https://stockfish.online/api/s/v2.php";

    /**
     * Constructor for BotPlayer.
     * @param game The game instance.
     * @param color The color of the player.
     */
    public BotPlayer(Game game, PieceColor color, int depth) {
        this.game = game;
        this.color = color;
        this.depth = depth < 16 ? depth : 15; // Ensure depth is less than 16
    }

    /**
     * Fetches the best move from Stockfish API.
     * @param fen The FEN string to analyze.
     * @param depth The depth for engine to go to (must be less than 16).
     * @return The best move as a String, or null if not found.
     * @throws Exception in case of an HTTP or parsing error.
     */
    public String getBestMove(String fen, int depth) throws Exception {
        if (depth >= 16) {
            throw new IllegalArgumentException("Depth must be less than 16.");
        }
        String urlString = BASE_URL + "?fen=" + java.net.URLEncoder.encode(fen, StandardCharsets.UTF_8)
                + "&depth=" + depth;
        System.out.println(BASE_URL + urlString);
        URL url = new URL(urlString);

        JSONObject jsonObject = requestAPI(url);
        String bestMove = jsonObject.optString("bestmove", null);
        System.out.println("Best Move: " + bestMove);
        return bestMove.split(" ")[1];
    }

    public static boolean testConnection() {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            return responseCode == 200;
        } catch (IOException e) {
            return false;
        }
    }

    private static JSONObject requestAPI(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int status = conn.getResponseCode();
        if (status != 200) {
            throw new RuntimeException("HTTP GET Request Failed with Error code: " + status);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder responseContent = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            responseContent.append(inputLine);
        }
        in.close();
        conn.disconnect();

        JSONObject jsonObject = new JSONObject(responseContent.toString());
        return jsonObject;
    }

    // Example usage:
    public static void main(String[] args) {
        try {
            BotPlayer client = new BotPlayer(new Game(), PieceColor.WHITE, 10);
            String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
            int depth = 10;
            String bestMove = client.getBestMove(fen, depth);
            System.out.println("Best Move: " + bestMove);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Move parseMove(String moveString) {
        // Assuming moveString is in the format "e2e4"
        Position from = new Position(moveString.substring(0, 2));
        Position to = new Position(moveString.substring(2, 4));
        if (moveString.length() == 5){
            promoteTo = moveString.substring(4, 5);
        }
        return new Move(from, to);
    }

    @Override
    public PieceColor getColor() {
        return color;
    }

    @Override
    public Move getMove() {
        String bestMoveString;
        try {
            bestMoveString = getBestMove(game.getFEN(), depth);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (bestMoveString == null) {
            throw new RuntimeException("No best move found.");
        }
        System.out.println("Best Move: " + bestMoveString);
        System.out.println("ParseMove: " + parseMove(bestMoveString));
        game.move = parseMove(bestMoveString);
        return game.getMove();
    }

    public Piece getPromoteTo(Cell pawnCell) {
        if (promoteTo != null) {
            return switch (promoteTo) {
                case "q" -> new Queen(this.getColor(), pawnCell);
                case "r" -> new Rook(this.getColor(), pawnCell);
                case "b" -> new Bishop(this.getColor(), pawnCell);
                case "n" -> new Knight(this.getColor(), pawnCell);
                default -> throw new IllegalArgumentException("Invalid promotion piece: " + promoteTo);
            };
        }
        return null;
    }
}