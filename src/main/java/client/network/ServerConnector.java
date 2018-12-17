package client.network;

import client.ClientBase;
import client.drawableBoard.DrawableField;
import client.controller.BoardController;

import client.drawableBoard.BoardParser;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ServerConnector {
  private static ServerConnector instance;

  private Socket clientSocket;


  private BoardController boardController;

  private PrintWriter out;
  private BufferedReader in;
  private JsonParser parser;


  private ServerConnector() {
    parser = new JsonParser();
  }


  public static ServerConnector getInstance() {
    if (instance == null) {
      instance = new ServerConnector();
    }
    return instance;
  }

  public void setBoardController(BoardController boardController){
    this.boardController = boardController;
  }

  public void makeConnection(String host, int port) throws IOException {
    if (this.clientSocket == null) {
      this.clientSocket = new Socket(host, port);
      this.out = new PrintWriter(clientSocket.getOutputStream(), true);
      this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
  }


  public PrintWriter getOutputStream() {
    return out;
  }

  public BoardController getBoardController() {
    return boardController;
  }

  public BufferedReader getInputStream() {
    return in;
  }

  public JsonParser getParser() {
    return parser;
  }

  public void endConnection() {
    try {
      in.close();
      out.close();
      clientSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void requestCreateGame(int numberOfPlayers, int numberOfPawns) throws ServerConnectionException {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("command", "create");
    jsonObj.addProperty("boardType", ClientBase.getInstance().getBoardType());
    jsonObj.addProperty("movementType", ClientBase.getInstance().getMovementType());
    jsonObj.addProperty("numberOfPlayers", numberOfPlayers);
    jsonObj.addProperty("numberOfPawns", numberOfPawns);
    //System.out.println(jsonObj.toString());
    out.println(jsonObj.toString());
    //System.out.println(jsonObj.toString());
    // read the response
    try {
      JsonObject response = parser.parse(in.readLine()).getAsJsonObject();
      //System.out.println(response.toString());
      if (!response.get("status").getAsString().equals("created")) {
        throw new ServerConnectionException();
      }

      ClientBase.getInstance().setGameId(response.get("gameId").getAsInt());
      String boardRepr =  response.get("board").getAsString();
      DrawableField[][] board = BoardParser.parseBoard(boardRepr);
      ClientBase.getInstance().setStartedBoard(board);
      //boardController.drawBoard(board);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void requestBeforeConnectToGame(){
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("command", "before connect");
    out.println(jsonObj.toString());


    try {

      String response = in.readLine();
      if(("null").equals(response)){
        ClientBase.getInstance().setOpenedGamesIds(null);
        return;
      }

      Gson gson = new Gson();
      Integer[] games = gson.fromJson(response, Integer[].class);
      ClientBase.getInstance().setOpenedGamesIds(Arrays.asList(games));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void requestConnectToGame() throws ServerConnectionException {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("command", "connect");
    jsonObj.addProperty("gameId", ClientBase.getInstance().getGameId());
    System.out.println(jsonObj.toString());
    out.println(jsonObj.toString());
    // read the response
    try {
      JsonObject response = parser.parse(in.readLine()).getAsJsonObject();
      if (!response.get("status").toString().equals("\"connected\"")) {
        throw new ServerConnectionException();
      }
      //todo save board, not draw
      //String boardRepr =  response.get("board").toString();
      //boardController.drawBoard(BoardParser.parseBoard(boardRepr));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void requestBeforeJoinGame(){
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("command", "before join");
    out.println(jsonObject.toString());

    try{
      JsonObject response = parser.parse(in.readLine()).getAsJsonObject();
      if(response.get("unused colors").toString().equals("\"null\"")){
        ClientBase.getInstance().setUnusedColors(null);
        return;
      }
      if(response.get("unused sides").toString().equals("\"null\"")){
        ClientBase.getInstance().setUnusedSides(null);
        return;
      }

      Gson gson = new Gson();
      String[] colors = gson.fromJson(response.get("unused colors"), String[].class);
      String[] sides = gson.fromJson(response.get("unused sides"), String[].class);

      ClientBase.getInstance().setUnusedColors(Arrays.asList(colors));
      ClientBase.getInstance().setUnusedSides(Arrays.asList(sides));

    } catch (IOException e) {
      e.printStackTrace();
    }


  }

  public void requestJoinGame(String startingSide, String color) throws ServerConnectionException {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("command", "join");
    jsonObj.addProperty("startingSide", startingSide);
    jsonObj.addProperty("color", color);
    System.out.println(jsonObj.toString());
    out.println(jsonObj.toString());
    // read the response
    try {
      JsonObject response = parser.parse(in.readLine()).getAsJsonObject();
      if (!response.get("status").toString().equals("\"joined\"")) {
        System.out.println(response);
        throw new ServerConnectionException();
      }
      String boardRepr =  response.get("board").getAsString();
      String boardType = response.get("boardType").toString();
      ClientBase.getInstance().setPlayerId(response.get("playerId").getAsInt());
      ClientBase.getInstance().setBoardType(boardType);
      ClientBase.getInstance().setStartedBoard(BoardParser.parseBoard(boardRepr));
      //ClientBase.getInstance().setPlayersToLabel(parseJsonMap(response.get("playerIdMap").getAsString()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void requestStartGame() throws ServerConnectionException {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("command", "start");

    out.println(jsonObj.toString());
    // read the response
    try {
      JsonObject response = parser.parse(in.readLine()).getAsJsonObject();
      if (!response.get("status").toString().equals("\"joined\"")) {
        throw new ServerConnectionException();
      }
      //todo set labels for players

      // wait for server information
      Platform.runLater(new Waiter());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  class Waiter implements Runnable {
    public void run() {
      while (true) {
        String serverResponse = "";
        try {
          serverResponse = in.readLine();
        } catch (IOException e) {
          e.printStackTrace();
        }
        JsonObject response = ServerConnector.getInstance().getParser().parse(serverResponse).getAsJsonObject();
        if (!response.get("status").toString().equals("\"success\"")) {
          // cannot throw exception :(
        }
        if (response.get("action").toString().equals("\"move\"")) {
          // parse the board
          String boardRepr = response.get("board").getAsString();
          DrawableField[][] board = BoardParser.parseBoard(boardRepr);
          ServerConnector.getInstance().getBoardController().drawBoard(board);
        } else if (response.get("action").toString().equals("\"endTurn\"")) {
          int currentPlayerId = response.get("playerId").getAsInt();
          // todo set a label for current player or smth

        }
      }
    }
  }

  private Map<Integer, Paint> parseJsonMap(String jsonMap) {
    Map<Integer, Paint> result = new HashMap<>();

    Map<String, String> map = new Gson().fromJson(jsonMap, new TypeToken<Map<String, String>>(){}.getType());

    for (Map.Entry<String, String> entry : map.entrySet()) {
      switch(entry.getValue()) {
        case "\"GREEN\"":
          result.put(Integer.parseInt(entry.getKey()), Color.GREEN);
          break;
        case "\"BLACK\"":
          result.put(Integer.parseInt(entry.getKey()), Color.BLACK);
          break;
        case "\"BLUE\"":
          result.put(Integer.parseInt(entry.getKey()), Color.BLUE);
          break;
        case "\"YELLOW\"":
          result.put(Integer.parseInt(entry.getKey()), Color.YELLOW);
          break;
        case "\"PURPLE\"":
          result.put(Integer.parseInt(entry.getKey()), Color.PURPLE);
          break;
      }

    }
    return result;
  }

}
