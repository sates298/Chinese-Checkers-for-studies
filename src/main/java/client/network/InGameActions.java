package client.network;

import com.google.gson.JsonObject;

public class InGameActions {
  private ServerConnector connector;

  public InGameActions(ServerConnector connector) {
    this.connector = connector;
  }

  public void requestMove(int playerId, int pawnX, int pawnY, int targetX, int targetY) throws ServerConnectionException {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("command", "move");
    jsonObj.addProperty("playerId", playerId);
    jsonObj.addProperty("pawnX", pawnX);
    jsonObj.addProperty("pawnY", pawnY);
    jsonObj.addProperty("targetX", targetX);
    jsonObj.addProperty("targetY", targetY);
    this.connector.getOutputStream().println(jsonObj.getAsString());
  }

  public void requestEndTurn(int playerId) throws ServerConnectionException {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("command", "move");
    jsonObj.addProperty("playerId", playerId);
    this.connector.getOutputStream().println(jsonObj.getAsString());
  }
}
