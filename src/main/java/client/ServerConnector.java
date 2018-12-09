package client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnector {
  private Socket clientSocket;

  private PrintWriter out;
  private BufferedReader in;
  private JsonParser parser;

  public ServerConnector(String host, int port) {
    parser = new JsonParser();
    try {
      clientSocket = new Socket(host, port);
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void waitForMessage(int playerId) throws IOException {
    String nextLine;
    while ((nextLine = in.readLine()) != null) {
      JsonObject jsonObject = parser.parse(nextLine).getAsJsonObject();
      if (jsonObject.get("action").getAsString().equals("move")) {
        // todo receive information about made move
      }
      else if (jsonObject.get("action").getAsString().equals("endTurn")) {
        // if currentPlayerId == current player's id - break
        if (jsonObject.get("currentPlayer").getAsInt() == playerId){
          break;
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

  // todo return needed values
  // todo review which values are needed in client
  // we need a way to return multiple values
  // board representation and playerId is the least we need
  // can we return just JsonObject?
  // todo board array parsing

  public void requestCreateGame(String boardType, String movementType) {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("command", "create");
    jsonObj.addProperty("boardType", boardType);
    jsonObj.addProperty("movementType", movementType);
    out.println(jsonObj.getAsString());
    // read the response
    try {
      JsonObject response = parser.parse(in.readLine()).getAsJsonObject();
      if (!response.get("status").getAsString().equals("created")) {
        // todo throw exception or smth
      }

      int gameId = response.get("gameId").getAsInt();
      String boardRepr =  response.get("board").getAsString();
      // return values?
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void requestConnectToGame(int gameId) {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("command", "connect");
    jsonObj.addProperty("gameId", gameId);
    out.println(jsonObj.getAsString());
    // read the response
    try {
      JsonObject response = parser.parse(in.readLine()).getAsJsonObject();
      if (!response.get("status").getAsString().equals("connected")) {
        // todo throw exception or smth
      }
      String boardRepr =  response.get("board").getAsString();
      // return values?
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void requestJoinGame(String startingSide, String color) {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("command", "join");
    jsonObj.addProperty("startingSide", startingSide);
    jsonObj.addProperty("color", color);
    out.println(jsonObj.getAsString());
    // read the response
    try {
      JsonObject response = parser.parse(in.readLine()).getAsJsonObject();
      if (!response.get("status").getAsString().equals("joined")) {
        // todo throw exception or smth
      }
      String boardRepr =  response.get("board").getAsString();
      // return values?
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void requestStartGame() {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("command", "start");

    out.println(jsonObj.getAsString());
    // read the response
    try {
      JsonObject response = parser.parse(in.readLine()).getAsJsonObject();
      if (!response.get("status").getAsString().equals("joined")) {
        // todo throw exception or smth
      }
      // return values?
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void requestMove(int playerId, int pawnX, int pawnY, int targetX, int targetY) {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("command", "move");
    jsonObj.addProperty("playerId", playerId);
    jsonObj.addProperty("pawnX", pawnX);
    jsonObj.addProperty("pawnY", pawnY);
    jsonObj.addProperty("targetX", targetX);
    jsonObj.addProperty("targetY", targetY);
    out.println(jsonObj.getAsString());
    // read the response
    try {
      JsonObject response = parser.parse(in.readLine()).getAsJsonObject();
      if (!response.get("status").getAsString().equals("connected")) {
        // todo throw exception or smth
      }
      //String boardRepr =  response.get("board").getAsString();
      // return values?
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void requestEndTurn(int playerId) {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("command", "move");
    jsonObj.addProperty("playerId", playerId);
    out.println(jsonObj.getAsString());
    // read the response
    try {
      JsonObject response = parser.parse(in.readLine()).getAsJsonObject();
      if (!response.get("status").getAsString().equals("connected")) {
        // todo throw exception or smth
      }
      //String boardRepr =  response.get("board").getAsString();
      // return values?

      // after finished turn we wait for messages about other players' actions
      waitForMessage(playerId);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
