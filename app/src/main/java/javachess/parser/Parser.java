package javachess.parser;

import javachess.PieceColor;
import javachess.PieceType;
import javachess.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Parser class that parses a PGN file and extracts the headers and moves.
 */
public class Parser {
    private static HashMap<String, String> headers;
    private static ArrayList<Instruction> moves;

    /**
     * Map of piece characters to PieceType.
     */
    private static final HashMap<String, PieceType> pieceMap = new HashMap<>() {{
        put("K", PieceType.KING);
        put("Q", PieceType.QUEEN);
        put("R", PieceType.ROOK);
        put("B", PieceType.BISHOP);
        put("N", PieceType.KNIGHT);
        put("P", PieceType.PAWN);
    }};

    public Parser() {
        headers = new HashMap<>();
        moves = new ArrayList<>();
    }

    public ArrayList<Instruction> getMoves() {
        return moves;
    }

//    public static void main(String[] args) {
//        Parser parser = new Parser();
//        URL url = Parser.class.getResource(args.length == 0 ? "dummy.pgn" : args[0]);
//        assert url != null;
//        String path = url.getPath();
//        String game = parser.getGameFromPath(path);
//        parser.splitParts(game);
//    }

    /**
     * Reads a PGN file from the given path and returns its content as a string.
     *
     * @param path the path to the PGN file
     * @return the content of the PGN file as a string
     */
    public String getGameFromPath(String path) {
        File file = new File(path);
        StringBuilder game = new StringBuilder();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                game.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return game.toString();
    }

    /**
     * Splits the game string into headers and moves.
     *
     * @param game the game string
     */
    public void splitParts(String game) {
        String[] parts = game.split("\n\n");
        assert parts.length == 2;
        headers = getHeaders(parts[0]);
        moves = getMoves(parts[1]);
    }

    /**
     * Parses the headers from the unparsed header string.
     *
     * @param unparsedHeader the unparsed header string
     * @return a map of headers
     */
    public HashMap<String, String> getHeaders(String unparsedHeader) {
        /*
            * The headers are in the format:
            * [Event "Event name"]
            * [Site "Site name"]
            * ...
         */
        String[] headers = unparsedHeader.split("\n"); // Split each line
        HashMap<String, String> headerMap = new HashMap<>();
        for (String header : headers) {
            header = header.substring(1, header.length() - 1); // Remove the brackets
            String[] fields = header.split(" ");
            String key = fields[0];
            // Little optimization : if the header is in the format [Key "Value"] we can just take the value ; if not, we have to select words between quotes
            String value = fields.length == 2 ? fields[1].replaceAll("\"", "") : header.substring(header.indexOf('"') + 1, header.lastIndexOf('"'));
            headerMap.put(key, value);
        }
        return headerMap;
    }

    /**
     * Parses the moves from the unparsed moves string.
     *
     * @param unparsedMoves the unparsed moves string
     * @return a list of instructions
     */
    public ArrayList<Instruction> getMoves(String unparsedMoves) {
        String[] moves = unparsedMoves.replaceAll("\\{.*?}", "") // Remove comments
                                      .replaceAll("\\n", " ") // Remove new lines
                                      .split("[\\s.]+"); // Split by whitespace and dots (trimming it also)
        System.out.println(Arrays.toString(moves));
        ArrayList<Instruction> instructions = new ArrayList<>();
        System.out.println("Length of moves: " + moves.length);
        for (int i = 0; i < moves.length; i+=3) {
            System.out.println("Turn number " + moves[i]);
            String[] bothMoves = i + 2 >= moves.length ? new String[]{moves[i + 1]} : new String[]{moves[i + 1], moves[i + 2]};
            for(int j = 0; j < bothMoves.length; j++) {
                String move = bothMoves[j];
                if (move.equals("1-0") || move.equals("0-1") || move.equals("1/2-1/2")) {
                    System.out.println("End of the game");
                    break; // End of the game
                }
                Instruction instruction = parseMove(move, j == 0 ? PieceColor.WHITE : PieceColor.BLACK);
                instructions.add(instruction);
            }
        }
        return instructions;
    }

    /**
     * Parses a move string into an Instruction object.
     *
     * @param move the move string
     * @param pieceColor the color of the piece
     * @return an Instruction object representing the move
     */
    public Instruction parseMove(String move, PieceColor pieceColor) {
        PieceType pieceType = null;
        Position position;
        boolean isCapture = false;
        boolean isCheck = false;
        boolean isCheckMate = false;
        Character ambiguity = null;
        PieceType promoteTo = null;

        // First check for roque
        if (move.equals("O-O")) {
            return new CastlingInstruction(false, pieceColor);
        } else if (move.equals("O-O-O")) {
            return new CastlingInstruction(true, pieceColor);
        } else {
            // Check for capture
            if (move.contains("x")) {
                isCapture = true;
                move = move.replace("x", "");
            }
            // Check for check and checkmate
            if (move.endsWith("+")) {
                isCheck = true;
                move = move.substring(0, move.length() - 1);
            } else if (move.endsWith("#")) {
                isCheckMate = true;
                move = move.substring(0, move.length() - 1);
            }
            // Check for promotion
            if (move.contains("=")) {
                String[] parts = move.split("=");
                promoteTo = pieceMap.get(parts[1]);
                move = parts[0];
            }
            // Get the piece type
            String pieceChar = String.valueOf(move.charAt(0));
            if (pieceMap.containsKey(pieceChar)) {
                pieceType = pieceMap.get(pieceChar);
                move = move.substring(1);
            } else {
                pieceType = PieceType.PAWN; // Default to pawn
            }
            // Check for ambiguity
            if (move.length() > 2) {
                ambiguity = move.charAt(0);
                move = move.substring(1);
            }
        }
        // Get the position
        int file = move.charAt(0) - 'a';
        int rank = 7 - (move.charAt(1) - '1'); // Invert the rank (our (0, 0) coordinate is the top left corner, while the official coordinate system has (0, 0) in the bottom left corner)
        position = new Position(file, rank);
        // Create the instruction
        RegularInstruction instruction = new RegularInstruction(pieceColor, pieceType, position, isCapture, isCheck, isCheckMate, ambiguity);
        if (promoteTo != null) {
            instruction.setPromoteTo(promoteTo);
        }
        return instruction;
    }
}
