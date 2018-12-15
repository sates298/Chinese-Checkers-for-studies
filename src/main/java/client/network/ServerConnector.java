package client.network;

import client.DrawableField;
import client.controller.BoardController;
import client.network.BoardParser;
import client.network.ServerConnectionException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

  private int playerId;


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

  public PrintWriter getOutputStream() {
    return out;
  }


  // wait for server's response and redraw board based on  the response
  public void waitForResponse() throws IOException, ServerConnectionException {
    while (true) {
      String serverResponse = in.readLine();
      JsonObject response = parser.parse(serverResponse).getAsJsonObject();
      if (!response.get("status").getAsString().equals("success")) {
        throw new ServerConnectionException();
      }
      if (response.get("action").getAsString().equals("move")) {
        // parse the board
        String boardRepr= response.get("board").getAsString();
        DrawableField[][] board = BoardParser.parseBoard(boardRepr);
        boardController.drawBoard(board);
      } else if (response.get("action").getAsString().equals("endTurn")) {
        int playerId = response.get("playerId").getAsInt();
        // todo set a label for current player or smth
        if(playerId == this.playerId){
          // decide we cannot break continous communication with the server (error messages  etc)
          //break;
        }
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
      this.playerId = response.get("playerId").getAsInt();
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
      waitForResponse();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }




}
