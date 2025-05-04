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

public class Parser {
    private static HashMap<String, String> headers;
    private static ArrayList<Instruction> moves;

    private static final HashMap<String, PieceType> pieceMap = new HashMap<>() {{
        put("K", PieceType.KING);
        put("Q", PieceType.QUEEN);
        put("R", PieceType.ROOK);
        put("B", PieceType.BISHOP);
        put("N", PieceType.KNIGHT);
        put("P", PieceType.PAWN);
    }};

    public static void main(String[] args) {
        String path = args.length == 0 ? "dummy.pgn" : args[0];
        String game = getGameFromPath(path);
        splitParts(game);
    }

    private static String getGameFromPath(String path) {
        URL url = Parser.class.getResource('/' + path);
        assert url != null;
        File file = new File(url.getPath());
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

    private static void splitParts(String game) {
        String[] parts = game.split("\n\n");
        assert parts.length == 2;
        headers = getHeaders(parts[0]);
        moves = getMoves(parts[1]);
    }

    private static HashMap<String, String> getHeaders(String unparsedHeader) {
        String[] headers = unparsedHeader.split("\n");
        HashMap<String, String> headerMap = new HashMap<>();
        for (String header : headers) {
            header = header.substring(1, header.length() - 1); // Remove the brackets
            String[] fields = header.split(" ");
            String key = fields[0];
            String value;
            if (fields.length == 2) {
                value = fields[1].replaceAll("\"", "");
            } else {
                value = header.substring(header.indexOf('"') + 1, header.lastIndexOf('"')); // Get what's inside the quotes
            }
            headerMap.put(key, value);
        }
        return headerMap;
    }

    private static ArrayList<Instruction> getMoves(String unparsedMoves) {
        String[] moves = unparsedMoves.replaceAll("\\{.*?\\}", "") // Remove comments
                                      .replaceAll("\\n", " ") // Remove new lines
                                      .split("[\\s.]+"); // Split by whitespace and dots (trimming it also)
        System.out.println(Arrays.toString(moves));
        ArrayList<Instruction> instructions = new ArrayList<>();
        for (int i = 0; i < moves.length / 3; i+=3) {
            String moveWhite = moves[i + 1];
            String moveBlack = moves[i + 2];
            String[] bothMoves = new String[]{moveWhite, moveBlack};
            for(int j = 0; j < bothMoves.length; j++) {
                String move = bothMoves[j];
                if (move.equals("1-0") || move.equals("0-1") || move.equals("1/2-1/2")) {
                    break; // End of the game
                }
                Instruction instruction = parseMove(move, j == 0 ? PieceColor.WHITE : PieceColor.BLACK);
                instructions.add(instruction);
            }
        }
        return instructions;
    }

    private static Instruction parseMove(String move, PieceColor pieceColor) {
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
            System.out.println("Move: " + move);
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
//            System.out.println("Move after check checkmate check: " + move);
            // Check for promotion
            if (move.contains("=")) {
                String[] parts = move.split("=");
                promoteTo = pieceMap.get(parts[1]);
                move = parts[0];
            }
//            System.out.println("Move after promotion check: " + move);
            // Get the piece type
            String pieceChar = String.valueOf(move.charAt(0));
            if (pieceMap.containsKey(pieceChar)) {
                pieceType = pieceMap.get(pieceChar);
                move = move.substring(1);
            } else {
                pieceType = PieceType.PAWN; // Default to pawn
            }
//            System.out.println("Move after piece type check: " + move);
            // Check for ambiguity
            if (move.length() > 2) {
                ambiguity = move.charAt(2);
                move = move.substring(0, 2);
            }
//            System.out.println("Move after ambiguity check: " + move);
        }
        // Get the position
        int file = move.charAt(0) - 'a';
        int rank = move.charAt(1) - '1';
        position = new Position(file, rank);
        // Create the instruction
        RegularInstruction instruction = new RegularInstruction(pieceColor, pieceType, position, isCapture, isCheck, isCheckMate, ambiguity);
        if (promoteTo != null) {
            instruction.setPromoteTo(promoteTo);
        }
        return instruction;
    }
}
