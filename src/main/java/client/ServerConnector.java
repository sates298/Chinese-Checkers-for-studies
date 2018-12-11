package client;

import client.controller.BoardController;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.shape.Circle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnector {
  private Socket clientSocket;

  private BoardController boardController;

  private PrintWriter out;
  private BufferedReader in;
  private JsonParser parser;

  public ServerConnector(BoardController boardController, String host, int port) {
    this.boardController = boardController;
    parser = new JsonParser();
    try {
      clientSocket = new Socket(host, port);
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // todo check how threads in gui work and if we can invoke methods from these object even though this loop is running

  // wait for server's response and redraw board based on  the response
  public void play() throws IOException, ServerConnectionException {
    while (true) {
      String serverResponse = in.readLine();
      JsonObject response = parser.parse(serverResponse).getAsJsonObject();
      if (!response.get("status").getAsString().equals("success")) {
        throw new ServerConnectionException();
      }
      if (response.get("action").getAsString().equals("move")) {
        // parse the board
        String boardRepr= response.get("board").getAsString();
        String[][] board = BoardParser.parseBoard(boardRepr);
        boardController.drawBoard(board);
      } else if (response.get("action").getAsString().equals("endTurn")) {
        // todo set a label for current player or smth
      }
    }
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


  public void requestCreateGame(String boardType, String movementType) throws ServerConnectionException {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("command", "create");
    jsonObj.addProperty("boardType", boardType);
    jsonObj.addProperty("movementType", movementType);
    out.println(jsonObj.getAsString());
    // read the response
    try {
      JsonObject response = parser.parse(in.readLine()).getAsJsonObject();
      if (!response.get("status").getAsString().equals("created")) {
        throw new ServerConnectionException();
      }

      int gameId = response.get("gameId").getAsInt();
      String boardRepr =  response.get("board").getAsString();
      boardController.drawBoard(BoardParser.parseBoard(boardRepr));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void requestConnectToGame(int gameId) throws ServerConnectionException {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("command", "connect");
    jsonObj.addProperty("gameId", gameId);
    out.println(jsonObj.getAsString());
    // read the response
    try {
      JsonObject response = parser.parse(in.readLine()).getAsJsonObject();
      if (!response.get("status").getAsString().equals("connected")) {
        throw new ServerConnectionException();
      }
      String boardRepr =  response.get("board").getAsString();
      boardController.drawBoard(BoardParser.parseBoard(boardRepr));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void requestJoinGame(String startingSide, String color) throws ServerConnectionException {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("command", "join");
    jsonObj.addProperty("startingSide", startingSide);
    jsonObj.addProperty("color", color);
    out.println(jsonObj.getAsString());
    // read the response
    try {
      JsonObject response = parser.parse(in.readLine()).getAsJsonObject();
      if (!response.get("status").getAsString().equals("joined")) {
        throw new ServerConnectionException();
      }
      String boardRepr =  response.get("board").getAsString();
      boardController.drawBoard(BoardParser.parseBoard(boardRepr));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void requestStartGame() throws ServerConnectionException {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("command", "start");

    out.println(jsonObj.getAsString());
    // read the response
    try {
      JsonObject response = parser.parse(in.readLine()).getAsJsonObject();
      if (!response.get("status").getAsString().equals("joined")) {
        throw new ServerConnectionException();
      }
      // wait for server information
      play();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void requestMove(int playerId, int pawnX, int pawnY, int targetX, int targetY) throws ServerConnectionException {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("command", "move");
    jsonObj.addProperty("playerId", playerId);
    jsonObj.addProperty("pawnX", pawnX);
    jsonObj.addProperty("pawnY", pawnY);
    jsonObj.addProperty("targetX", targetX);
    jsonObj.addProperty("targetY", targetY);
    out.println(jsonObj.getAsString());
  }

  public void requestEndTurn(int playerId) throws ServerConnectionException {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("command", "move");
    jsonObj.addProperty("playerId", playerId);
    out.println(jsonObj.getAsString());
  }

}
