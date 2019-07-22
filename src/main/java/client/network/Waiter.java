package client.network;

import client.ClientBase;
import client.drawableBoard.BoardParser;
import client.drawableBoard.DrawableField;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

class Waiter implements Runnable {

    private BufferedReader reader;

    public Waiter(BufferedReader reader) {
        this.reader = reader;
    }

    public void run() {
        try {
            waitForResponseAndExecuteAction();
        } catch(SocketException se) {
            // this exception is thrown when sockets are closed
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void waitForResponseAndExecuteAction() throws IOException {
        String serverResponse;
        while ((serverResponse = reader.readLine()) != null) {
//        System.out.println(serverResponse);
            JsonObject response = ServerConnector.getInstance().getParser().parse(serverResponse).getAsJsonObject();

            if (!response.get("status").toString().equals("\"successful\"")) {
                System.out.println("continuing");
                continue;
            }

            executeAction(response);

        }
    }

    private void executeAction(JsonObject response) {
        switch (response.get("action").toString()) {
            case "\"move\"": {
                parseAndSetBoardFromString(response.get("board").getAsString());
                break;
            }
            case "\"game started\"": {
                gameStartedAction(response.get("playerColorMap").getAsString(),
                        response.get("board").getAsString(),response.get("currentPlayer").getAsInt());
                break;
            }
            case "\"endTurn\"":
                endTurnAction(response.get("currentPlayer").getAsInt());
                break;
            case "\"join\"": {
                joinAction(response.get("playerColorMap").getAsString(), response.get("board").getAsString());
                break;
            }
        }
    }

    private void gameStartedAction(String playersMapRepr, String boardRepr, int currentPlayerId) {
        // parse the board
        parseAndSetBoardFromString(boardRepr);
        //set labels
        setPlayersLabelFromString(playersMapRepr);
        //set current player's label bold
        setStyleOnAllPlayersLabels(currentPlayerId);
    }


    private void endTurnAction(int currentPlayerId) {
        setStyleOnAllPlayersLabels(currentPlayerId);
    }

    private void setStyleOnAllPlayersLabels(int currentPlayerId) {
        for (int i = 0; i < ClientBase.getInstance().getPlayersToLabel().size(); i++) {
            setStyleOnPlayerLabel(i, "-fx-font-weight: normal");
        }
        setStyleOnPlayerLabel(currentPlayerId, "-fx-font-weight: bold");
    }

    private void setStyleOnPlayerLabel(int currentPlayerId, String style) {
        Platform.runLater(() -> ServerConnector.getInstance()
                .getBoardController()
                .getCorrectLabel(currentPlayerId)
                .setStyle(style));
    }

    private void joinAction(String boardString, String playerColorMapString){
        DrawableField[][] board = parseAndSetBoardFromString(boardString);
        setPlayersLabelFromString(playerColorMapString);
    }

    private DrawableField[][] parseAndSetBoardFromString(String boardString) {
        DrawableField[][] board = BoardParser.parseBoard(boardString);
        Platform.runLater(() -> ServerConnector.getInstance().getBoardController().drawBoard(board));
        return board;
    }

    private void setPlayersLabelFromString(String playerColorMapString) {
        ClientBase.getInstance().setPlayersToLabel(parseJsonMap(playerColorMapString));
        Platform.runLater(() -> ServerConnector.getInstance().getBoardController().fillLabels());
    }

    private Map<Integer, Paint> parseJsonMap(String jsonMap) {
        Map<Integer, Paint> result = new HashMap<>();

        Map<String, String> map = new Gson().fromJson(jsonMap, new TypeToken<Map<String, String>>(){}.getType());

        for (Map.Entry<String, String> entry : map.entrySet()) {
            result.put(Integer.parseInt(entry.getKey()), Color.valueOf(entry.getValue()));
        }

        return result;
    }
}
