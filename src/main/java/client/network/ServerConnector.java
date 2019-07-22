package client.network;

import client.ClientBase;
import client.drawableBoard.BoardParser;
import client.drawableBoard.DrawableField;
import client.controller.BoardController;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class ServerConnector {

    private static ServerConnector instance;

    private Socket clientSocket;


    private BoardController boardController;

    private PrintWriter writer;
    private BufferedReader reader;
    private JsonParser parser;
    private JsonRequestCreator requestCreator;

    private boolean alive = true;


    private ServerConnector() {
        parser = new JsonParser();
        requestCreator = new JsonRequestCreator();
    }


    public static ServerConnector getInstance() {
        if (instance == null) {
            instance = new ServerConnector();
        }
        return instance;
    }

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public void makeConnection(String host, int port) throws IOException {
        if (this.clientSocket == null) {
            this.clientSocket = new Socket(host, port);
            this.writer = new PrintWriter(clientSocket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }
    }

    public PrintWriter getOutputStream() {
        return writer;
    }

    public BoardController getBoardController() {
        return boardController;
    }

    public JsonParser getParser() {
        return parser;
    }

    public void endConnection() throws IOException {
        writer.close();
        reader.close();
        clientSocket.close();
    }


    public void requestCreateGame(int numberOfPlayers, int numberOfPawns) throws ServerConnectionException, IOException {
        JsonObject jsonObj =
                requestCreator.createRequestCreateGameJsonObject(numberOfPlayers, numberOfPawns,
                        ClientBase.getInstance().getBoardType(), ClientBase.getInstance().getMovementType());

        sendRequestToServer(jsonObj.toString());
        JsonObject response = readResponse("created");

        ClientBase.getInstance().setGameId(response.get(JsonRequestCreator.GAME_ID).getAsInt());
        parseAndSetStartedBoard(response.get(JsonRequestCreator.BOARD).getAsString());
    }


    public void requestBeforeConnectToGame() throws IOException {
        JsonObject jsonObj = requestCreator.createRequestBeforeConnectToGame();
        sendRequestToServer(jsonObj.toString());
        setOpenedGamesIds(reader.readLine());
    }


    public void requestConnectToGame() throws ServerConnectionException, IOException {
        JsonObject jsonObj = requestCreator.createRequestConnectToGame(ClientBase.getInstance().getGameId());
        sendRequestToServer(jsonObj.toString());
        readResponse("\"connected\"");
    }

    public void requestBeforeJoinGame() throws IOException {
        JsonObject jsonObject = requestCreator.createRequestBeforeJoinGame();
        sendRequestToServer(jsonObject.toString());

        JsonObject response = parser.parse(reader.readLine()).getAsJsonObject();

        parseAndSetUnusedColors(response.get(JsonRequestCreator.UNUSED_COLORS));
        parseAndSetUnusedSides(response.get(JsonRequestCreator.UNUSED_SIDES));

    }

    public void requestJoinGame(String startingSide, String color) throws ServerConnectionException, IOException {
        JsonObject jsonObj = requestCreator.createRequestJoinGameJsonObject(startingSide, color);

        sendRequestToServer(jsonObj.toString());
        // read the response
        JsonObject response = readResponse("\"successful\"");

        setBoardAfterJoining(response, ClientBase.getInstance());

        // wait for server information
        startWaiter();
    }

    private void startWaiter() {
        Waiter waiter = new Waiter(reader);
        new Thread(waiter).start();
    }

    public void requestStartGame() throws ServerConnectionException {
        JsonObject jsonObj = requestCreator.createRequestStartGame();
        sendRequestToServer(jsonObj.toString());
    }

    private void sendRequestToServer(String request) {
        writer.println(request);
    }

    public JsonObject readResponse(String expectedStatus) throws IOException, ServerConnectionException {
        JsonObject response = parser.parse(reader.readLine()).getAsJsonObject();
        if (!response.get(JsonRequestCreator.STATUS).toString().equals(expectedStatus)) {
            throw new ServerConnectionException();
        }
        return response;
    }

    private void setOpenedGamesIds(String response) {
        if (("null").equals(response)) {
            ClientBase.getInstance().setOpenedGamesIds(null);
        } else {
            Gson gson = new Gson();
            Integer[] games = gson.fromJson(response, Integer[].class);
            ClientBase.getInstance().setOpenedGamesIds(Arrays.asList(games));
        }
    }

    private void parseAndSetStartedBoard(String boardRepr) {
        DrawableField[][] board = BoardParser.parseBoard(boardRepr);
        ClientBase.getInstance().setStartedBoard(board);
    }

    private void parseAndSetUnusedSides(JsonElement unusedSides) {
        if (unusedSides.toString().equals("\"null\"")) {
            ClientBase.getInstance().setUnusedSides(null);
            return;
        }

        Gson gson = new Gson();
        String[] sides = gson.fromJson(unusedSides, String[].class);
        ClientBase.getInstance().setUnusedSides(Arrays.asList(sides));
    }

    private void parseAndSetUnusedColors(JsonElement unusedColors) {
        if (unusedColors.toString().equals("\"null\"")) {
            ClientBase.getInstance().setUnusedColors(null);
            return;
        }

        Gson gson = new Gson();
        String[] colors = gson.fromJson(unusedColors, String[].class);
        ClientBase.getInstance().setUnusedColors(Arrays.asList(colors));
    }

    private void setBoardAfterJoining(JsonObject response, ClientBase client) {
        String boardRepr = response.get(JsonRequestCreator.BOARD).getAsString();
        String boardType = response.get(JsonRequestCreator.BOARD_TYPE).toString();

        client.setPlayerId(response.get(JsonRequestCreator.PLAYER_ID).getAsInt());
        client.setBoardType(boardType);
        client.setStartedBoard(BoardParser.parseBoard(boardRepr));
    }
}
