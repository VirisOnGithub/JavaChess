package javachess.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;

public class Parser {
    private static String headers;
    private static String moves;

    public static void main(String[] args) {
        String path = args.length == 0 ? "dummy.pgn" : args[0];
        String game =  getGame(path);
        splitMoves(game);
        System.out.println(moves);
    }

    private static String getGame(String path) {
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

    private static void splitMoves(String game) {
        String[] parts = game.split("\n\n");
        assert parts.length == 2;
        headers = parts[0];
        moves = parts[1];
    }
}
