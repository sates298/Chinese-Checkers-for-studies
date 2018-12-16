package server;

import server.board.Board;
import server.board.BoardSide;
import server.creator.GameCreator;
import server.exception.*;
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
import java.util.Optional;

public class Server {
    private List<Game> games;
    private static Server instance;

    private List<GameClientHandler> connectedClients;

    private ServerSocket serverSocket;

    public static void  main  (String[] args) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("something", "test");

        System.out.println(jsonObject.get("something").toString());



        Server  server = new Server();

        server.start(1234);
        server.stop();
    }

    public Server() {
        games = new ArrayList<>();
        connectedClients = new ArrayList<>();
    }


    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }

        return instance;
    }

    private void pushToMany(Game game, String message) {
        for (GameClientHandler c: connectedClients) {
            if (c.game == game) {
                c.out.println(message);
            }
        }
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
                GameClientHandler newClient = new GameClientHandler(serverSocket.accept());
                this.connectedClients.add(newClient);
                new Thread(newClient).start();
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
                    //System.out.println(object.get("command").toString());
                    handleClientMessage(object);
                } else {
                    // send error message to client
                    JsonObject returnObj = new JsonObject();
                    returnObj.addProperty("status", "wrong message format");
                    out.println(returnObj.getAsString());
                }
                System.out.println(inputLine); // log requests
            }

            in.close();
            out.close();
            clientSocket.close();
        }

        private void handleClientMessage(JsonObject jsonObject) {
            if (jsonObject.get("command").toString().equals("\"connect\"")) {
                // request looks like "{"command":"connect", "gameId": "gameId"}"
                connectToGame(jsonObject.get("gameId").getAsInt());
                return;
            }

            if (jsonObject.get("command").toString().equals("\"create\"")) {

                createGame(jsonObject.get("boardType").toString(), jsonObject.get("movementType").toString(),
                        jsonObject.get("numberOfPlayers").getAsInt(), jsonObject.get("numberOfPawns").getAsInt());
                return;
            }

            if (jsonObject.get("command").toString().equals("\"join\"")) {
                joinGame(jsonObject.get("startingSide").toString(), jsonObject.get("color").toString());
                return;
            }
            if (jsonObject.get("command").toString().equals("\"move\"")) {
                move(jsonObject.get("playerId").getAsInt(),
                    jsonObject.get("pawnX").getAsInt(), jsonObject.get("pawnY").getAsInt(),
                    jsonObject.get("targetX").getAsInt(), jsonObject.get("targetY").getAsInt());
                return;
            }
            if (jsonObject.get("command").toString().equals("\"endTurn\"")) {
                endTurn(jsonObject.get("playerId").getAsInt());
                return;
            }
            if (jsonObject.get("command").toString().equals("\"start\"")) {
                startGame();
                return;
            }
        }

        private void createGame(String boardType, String movementType, int playersNO, int pawnsNO) {
            try {
                this.game = new GameCreator().createGame(boardType, movementType, playersNO, pawnsNO);
                JsonObject returnObj = new JsonObject();
                returnObj.addProperty("status", "created");
                returnObj.addProperty("gameId", this.game.getGameId());
                returnObj.addProperty("board", this.game.getBoard().fieldsToString());
                out.println(returnObj.toString());
                System.out.println(returnObj.toString());

            } catch (WrongMovementTypeException | WrongBoardTypeException e) {
                JsonObject returnObj = new JsonObject();
                returnObj.addProperty("status", e.toString());
                out.println(returnObj.toString());
                e.printStackTrace();
            }
        }

        private void connectToGame(int gameId){
            // find the game with id equal to gameId
            System.out.println("weszlo do connect");
            for (Game g: Server.getInstance().getGames()) {
                System.out.println(g.getGameId());
            }
            JsonObject returnObj = new JsonObject();
            Optional<Game> optionalGame =  Server.getInstance().getGames().stream().filter(g -> g.getGameId() == gameId).findAny();
            if(optionalGame.isPresent()){
                this.game = optionalGame.get();
                returnObj.addProperty("status", "connected");
                returnObj.addProperty("gameId", gameId);
                returnObj.addProperty("board", this.game.getBoard().fieldsToString());
                out.println(returnObj.toString());
            }else{
                returnObj.addProperty("status", "is not present");
                out.println(returnObj.toString());
            }
        }

        private void joinGame(String side, String color) {
            try {
                Player p = this.game.getController().addPlayer(side, color);
                JsonObject returnObj = new JsonObject();
                returnObj.addProperty("status", "joined");
                returnObj.addProperty("startingSide", p.getStartingSide().toString());
                returnObj.addProperty("color", p.getColor().toString());
                returnObj.addProperty("board", this.game.getBoard().fieldsToString());
                System.out.println(returnObj.toString());
                out.println(returnObj.toString());
            } catch (GameFullException | BoardSideUsedException | ColorUsedException e) {
                JsonObject returnObj = new JsonObject();
                returnObj.addProperty("status", e.toString());
                out.println(returnObj.toString());
                e.printStackTrace();
            }
        }
        private void startGame() {
            this.game.getController().startGame();
            JsonObject returnObj = new JsonObject();
            returnObj.addProperty("status", "joined");
            out.println(returnObj.toString());
        }

        private void move(int playerId, int pawnX, int pawnY, int targetX, int targetY) {
            try {
                this.game.getController().move(playerId, pawnX, pawnY, targetX, targetY);

                JsonObject jsonObject = new JsonObject();

                jsonObject.addProperty("action", "move");
                jsonObject.addProperty("status", "successful");
                jsonObject.addProperty("board", this.game.getBoard().fieldsToString());

                pushToMany(this.game, jsonObject.toString());
            } catch (ForbiddenMoveException | ForbiddenActionException e) {
                e.printStackTrace();
            }
        }

        private void endTurn(int playerId) {
            try {
                this.game.getController().endTurn(playerId);
                // push information about whose turn is it to all players
                JsonObject returnObj = new JsonObject();
                returnObj.addProperty("action", "endTurn");
                returnObj.addProperty("currentPlayer",
                    this.game.getController().getCurrentTurnPlayer().getId());

                Server.this.pushToMany(this.game, returnObj.toString());

            } catch (ForbiddenActionException e) {
                e.printStackTrace();
            }
        }
    }
}
