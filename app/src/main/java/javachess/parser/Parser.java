package javachess.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class Parser {
    private static HashMap<String, String> headers;
    private static String moves;

    public static void main(String[] args) {
        String path = args.length == 0 ? "dummy.pgn" : args[0];
        String game =  getGame(path);
        splitParts(game);
        for (String key : headers.keySet()) {
            System.out.println(key + ": " + headers.get(key));
        }
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

    private static void splitParts(String game) {
        String[] parts = game.split("\n\n");
        assert parts.length == 2;
        headers = getHeaders(parts[0]);
        moves = parts[1];
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
}
