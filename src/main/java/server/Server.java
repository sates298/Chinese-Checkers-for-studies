package server;

import server.board.Board;
import server.board.BoardSide;
import server.creator.GameCreator;
import server.creator.exception.WrongBoardTypeException;
import server.creator.exception.WrongMovementTypeException;
import server.exception.BoardSideUsedException;
import server.exception.ColorUsedException;
import server.exception.GameFullException;
import server.game.Game;

import com.google.gson.*;
import server.player.Color;
import server.player.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<Game> games;
    private static Server instance;

    private ServerSocket serverSocket;

    public Server() {
        games = new ArrayList<>();
    }


    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }

        return instance;
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace(); // delete after testing phase and replace it with smth more user friendly
        }
        while (true) {
            // start  new thread when new client connects
            try {
                new Thread(new GameClientHandler(serverSocket.accept())).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Game> getGames() {
        return games;
    }


    private class GameClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        private Game game;

        public GameClientHandler(Socket client) {
            this.clientSocket = client;
        }

        public void run() {
            try {
                handleClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private void handleClient() throws IOException {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            JsonParser parser = new JsonParser();

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                JsonElement jsonTree = parser.parse(inputLine);
                if (jsonTree.isJsonObject()) {
                    JsonObject object = jsonTree.getAsJsonObject();
                    handleClientMessage(object);
                } else {
                    // send error message to client
                    out.println("{\"error\": \"wrong message format\"");
                }
                System.out.println(inputLine); // log requests
            }

            in.close();
            out.close();
            clientSocket.close();
        }

        private void handleClientMessage(JsonObject jsonObject) {
            if (jsonObject.get("command").getAsString().equals("connect")) {
                // request looks like "{"command":"connect", "gameId": "gameId"}"
                connectToGame(jsonObject.get("gameId").getAsInt());
                return;
            }

            if (jsonObject.get("command").toString().equals("create")) {
                createGame(jsonObject.get("gameType").getAsString(), jsonObject.get("movementType").getAsString());
                return;
            }

            if (jsonObject.get("command").getAsString().equals("join")) {
                joinGame(jsonObject.get("side").getAsString(), jsonObject.get("color").getAsString());
                return;
            }
        }

        private void createGame(String boardType, String movementType) {
            try {
                this.game = new GameCreator().createGame(boardType, movementType);
                out.println("{\"status\": \"created\", \"gameId\": \"" + this.game.getGameId() + "\"}");

            } catch (WrongMovementTypeException | WrongBoardTypeException e) {
                out.println("{\"error\": \"" + e + "\"}");
                e.printStackTrace();
            }
        }

        private void connectToGame(int gameId) {
            // find the game with id equal to gameId
            this.game = games.stream().filter(g -> g.getGameId() == gameId).findFirst().get();
            out.println("{\"status\": \"connected\", \"gameId\": \"" + gameId + "\"}");
        }

        private void joinGame(String side, String color) {
            try {
                Player p = this.game.getController().addPlayer(side, color);
                out.println("\"status\": \"joined\", \"startingSide\": "
                        + p.getStartingSide() + "\", \"color\"" + p.getColor() + "\"");
            } catch (GameFullException | BoardSideUsedException | ColorUsedException e) {
                e.printStackTrace();
                out.println("{\"error\": \"" + e + "\"}");
            }
        }
    }
}
