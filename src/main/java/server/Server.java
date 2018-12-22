package server;

import server.board.BoardSide;
import server.board.SixPointedStarSide;
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
import java.net.SocketException;
import java.util.*;

public class Server {
    private volatile List<Game> games;
    private volatile static Server instance;

    private volatile List<GameClientHandler> connectedClients;

    private ServerSocket serverSocket;

    private static volatile boolean keepAlive = true;

    public static void main(String[] args) {
        int PORT = 1235;
        System.out.println(" Server is running on port " + PORT
            + " to terminate the server, type 'exit' to console and press Enter");
        Server server = new Server();
        server.start(PORT);
    }

    public Server() {
        games = new ArrayList<>();
        connectedClients = new ArrayList<>();
    }


    public static Server getInstance() {
        if (instance == null) {
            synchronized (Server.class) {
                if (instance == null) {
                    instance = new Server();
                }
            }
        }

        return instance;
    }

    public void pushToMany(Game game, String message) {
        System.out.println(Server.getInstance().connectedClients);
        for (GameClientHandler c : Server.getInstance().connectedClients) {
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
        while (keepAlive) {
            // this thread waits for 'exit' and if it has any kills  the server
            new Thread(() -> {
                Scanner s = new Scanner(System.in);
                while (true) {
                    if(s.hasNext()  && s.nextLine().equals("exit")) {
                        keepAlive = false;
                        Server.this.stop();
                        break;
                    }
                }
            }).start();

            // start  new thread when new client connects
            try {
                GameClientHandler newClient = new GameClientHandler(serverSocket.accept());
                Server.getInstance().connectedClients.add(newClient);
                new Thread(newClient).start();
            } catch (SocketException se) {
                System.out.println("server shutdown");
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

        GameClientHandler(Socket client) {
            this.clientSocket = client;
        }

        public void run() {
            try {
                handleClient();
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }

        private void handleClient() throws IOException {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            JsonParser parser = new JsonParser();

            String inputLine;
            while (Server.keepAlive && (inputLine = in.readLine()) != null) {
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

            out.close();
            in.close();
            clientSocket.close();
        }

        private void handleClientMessage(JsonObject jsonObject) {
            switch (jsonObject.get("command").toString()) {
                case "\"connect\"":
                    // request looks like "{"command":"connect", "gameId": "gameId"}"
                    connectToGame(jsonObject.get("gameId").getAsInt());
                    break;
                case "\"before connect\"":
                    beforeConnectToGame();
                    break;
                case "\"create\"":
                    createGame(jsonObject.get("boardType").toString(), jsonObject.get("movementType").toString(),
                            jsonObject.get("numberOfPlayers").getAsInt(), jsonObject.get("numberOfPawns").getAsInt());
                    break;
                case "\"join\"":
                    joinGame(jsonObject.get("startingSide").toString(), jsonObject.get("color").toString());
                    break;
                case "\"before join\"":
                    beforeJoinGame();
                    break;
                case "\"move\"":
                    move(jsonObject.get("playerId").getAsInt(),
                            jsonObject.get("pawnX").getAsInt(), jsonObject.get("pawnY").getAsInt(),
                            jsonObject.get("targetX").getAsInt(), jsonObject.get("targetY").getAsInt());
                    break;
                case "\"endTurn\"":
                    endTurn(jsonObject.get("playerId").getAsInt());
                    break;
                case "\"start\"":
                    startGame();
                    break;
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

            } catch (WrongMovementTypeException | WrongBoardTypeException e) {
                JsonObject returnObj = new JsonObject();
                returnObj.addProperty("status", e.toString());
                out.println(returnObj.toString());
                e.printStackTrace();
            }
        }

        private void connectToGame(int gameId) {
            // find the game with id equal to gameId
            /*for (Game g : Server.getInstance().getGames()) {
                System.out.println(g.getGameId());
            }*/
            JsonObject returnObj = new JsonObject();
            Optional<Game> optionalGame = Server.getInstance().getGames().stream().filter(g -> g.getGameId() == gameId).findAny();
            if (optionalGame.isPresent()) {
                this.game = optionalGame.get();
                returnObj.addProperty("status", "connected");
                returnObj.addProperty("gameId", gameId);
                returnObj.addProperty("board", this.game.getBoard().fieldsToString());
                out.println(returnObj.toString());
            } else {
                returnObj.addProperty("status", "is not present");
                out.println(returnObj.toString());
            }
        }

        private void beforeConnectToGame() {
            Integer[] ids = new Integer[Server.getInstance().getGames().size()];
            for (int i = 0; i < Server.getInstance().getGames().size(); i++) {
                ids[i] = Server.getInstance().getGames().get(i).getGameId();
            }
            JsonArray jArray = new JsonArray();
            for (Integer i : ids) {
                jArray.add(i);
            }
            if (ids.length == 0) {
                out.println("null");
                return;
            }
            out.println(jArray.toString());


        }

        private void beforeJoinGame() {

            if (this.game.getBoard().getType().equals("SixPointedStar")) {
                JsonArray colorsArray = new JsonArray();
                JsonArray sidesArray = new JsonArray();

                List<BoardSide> enabledSides = this.game.getController().getEnabledSides();
                for(BoardSide side : enabledSides){
                    sidesArray.add(side.toString());
                }

                List<Color> enabledColors = this.game.getController().getEnabledColors();
                for(Color color : enabledColors){
                    colorsArray.add(color.toString());
                }

                JsonObject returnObj = new JsonObject();

                if (colorsArray.size() == 0) {
                    returnObj.addProperty("unused colors", "null");
                } else {
                    returnObj.add("unused colors", colorsArray);
                }

                if (sidesArray.size() == 0) {
                    returnObj.addProperty("unused sides", "null");
                } else {
                    returnObj.add("unused sides", sidesArray);
                }
                out.println(returnObj.toString());
            }
        }

        private void joinGame(String side, String color) {
            try {
                Player p = this.game.getController().addPlayer(side, color);
                JsonObject returnObj = new JsonObject();
                returnObj.addProperty("status", "successful");
                returnObj.addProperty("startingSide", p.getStartingSide().toString());
                returnObj.addProperty("color", p.getColor().toString());
                returnObj.addProperty("board", this.game.getBoard().fieldsToString());
                returnObj.addProperty("boardType", this.game.getBoard().getType());
                returnObj.addProperty("playerId", p.getId());
                returnObj.addProperty("playerColorMap",
                        new Gson().toJson(game.getController().getIdColorMap()));

                out.println(returnObj.toString());

                JsonObject allPlayersResponse = new JsonObject();
                allPlayersResponse.addProperty("status", "successful");
                allPlayersResponse.addProperty("action", "join");
                allPlayersResponse.addProperty("board", this.game.getBoard().fieldsToString());
                allPlayersResponse.addProperty("playerColorMap",
                    new Gson().toJson(game.getController().getIdColorMap()));

                pushToMany(this.game, allPlayersResponse.toString());

            } catch (GameFullException | BoardSideUsedException | ColorUsedException e) {
                JsonObject returnObj = new JsonObject();
                returnObj.addProperty("status", e.toString());
                out.println(returnObj.toString());
            }
        }

        private void startGame() {
            JsonObject returnObj = new JsonObject();
            this.game.getController().startGame();
            returnObj.addProperty("action", "game started");
            returnObj.addProperty("status", "successful");
            returnObj.addProperty("board", this.game.getBoard().fieldsToString());
            returnObj.addProperty("playerColorMap",
                    new Gson().toJson(game.getController().getIdColorMap()));
            returnObj.addProperty("currentPlayer",
                    this.game.getController().getCurrentTurnPlayer().getId());
            pushToMany(this.game, returnObj.toString());
        }

        private void move(int playerId, int pawnX, int pawnY, int targetX, int targetY) {
            // if game hasn't started don't send any message so client's buffer is not full
            if (!this.game.getController().isStarted()) return;
            try {

                this.game.getController().move(playerId, pawnX, pawnY, targetX, targetY);

                JsonObject jsonObject = new JsonObject();

                jsonObject.addProperty("action", "move");
                jsonObject.addProperty("status", "successful");
                jsonObject.addProperty("board", this.game.getBoard().fieldsToString());

                pushToMany(this.game, jsonObject.toString());
            } catch (ForbiddenMoveException | ForbiddenActionException e) {
                JsonObject response = new JsonObject();
                response.addProperty("status", e.toString());
                out.println(response.toString());
                //pushToMany(this.game, response.toString());
            }
        }

        private void endTurn(int playerId) {
            // if game hasnt started dont send any message so client's buffer is not full
            if (!this.game.getController().isStarted()) return;
            try {
                this.game.getController().endTurn(playerId);
                // push information about whose turn is it to all players
                JsonObject returnObj = new JsonObject();
                returnObj.addProperty("status", "successful");
                returnObj.addProperty("action", "endTurn");
                returnObj.addProperty("currentPlayer",
                        this.game.getController().getCurrentTurnPlayer().getId());

                Server.this.pushToMany(this.game, returnObj.toString());

            } catch (ForbiddenActionException e) {
                JsonObject response = new JsonObject();
                response.addProperty("status", e.toString());
                out.println(response.toString());
            }
        }
    }
}
