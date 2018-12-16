package client.network;


import client.ClientBase;
import client.drawableBoard.BoardParser;
import client.drawableBoard.DrawableField;
import com.google.gson.JsonObject;
import com.victorlaerte.asynctask.AsyncTask;
import server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ServerMessageWaiter extends AsyncTask {

  private PrintWriter out;
  private BufferedReader in;


  @Override
  public void onPreExecute() {
    this.out = ServerConnector.getInstance().getOutputStream();
  }

  @Override
  public Object doInBackground(Object[] objects) {
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
      if (response.get("action").getAsString().equals("\"move\"")) {
        // parse the board
        String boardRepr= response.get("board").toString();
        DrawableField[][] board = BoardParser.parseBoard(boardRepr);
        ServerConnector.getInstance().getBoardController().drawBoard(board);
      } else if (response.get("action").getAsString().equals("\"endTurn\"")) {
        int playerId = response.get("playerId").getAsInt();
        // todo set a label for current player or smth
        if(playerId == ClientBase.getInstance().getPlayerId()){
          // decide we cannot break continous communication with the server (error messages  etc)
          //break;
        }
      }
    }
  }

  @Override
  public void onPostExecute(Object o) {

  }

  @Override
  public void progressCallback(Object[] objects) {

  }
}
