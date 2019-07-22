package client.network;

import client.ClientBase;
import com.google.gson.JsonObject;

public class JsonRequestCreator {
    public static final String COMMAND = "command";
    public static final String BOARD_TYPE = "boardType";
    public static final String MOVEMENT_TYPE = "movementType";
    public static final String NUMBER_OF_PLAYERS = "numberOfPlayers";
    public static final String NUMBER_OF_PAWNS = "numberOfPawns";
    public static final String BOARD = "board";
    public static final String GAME_ID = "gameId";
    public static final String STATUS = "status";
    public static final String UNUSED_COLORS = "unused colors";
    public static final String UNUSED_SIDES = "unused sides";
    public static final String STARTING_SIDE = "startingSide";
    public static final String COLOR = "color";
    public static final String PLAYER_ID = "playerId";

    public JsonObject createRequestCreateGameJsonObject(int numberOfPlayers, int numberOfPawns, String boardType, String movementType) {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty(COMMAND, "create");
        jsonObj.addProperty(BOARD_TYPE, boardType);
        jsonObj.addProperty(MOVEMENT_TYPE, movementType);
        jsonObj.addProperty(NUMBER_OF_PLAYERS, numberOfPlayers);
        jsonObj.addProperty(NUMBER_OF_PAWNS, numberOfPawns);
        return jsonObj;
    }

    public JsonObject createRequestBeforeConnectToGame() {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty(JsonRequestCreator.COMMAND, "before connect");
        return jsonObj;
    }

    public JsonObject createRequestConnectToGame(int gameId) {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty(JsonRequestCreator.COMMAND, "connect");
        jsonObj.addProperty(JsonRequestCreator.GAME_ID, gameId);
        System.out.println(jsonObj.toString());
        return jsonObj;
    }

    public JsonObject createRequestBeforeJoinGame() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JsonRequestCreator.COMMAND, "before join");
        return jsonObject;
    }

    public JsonObject createRequestJoinGameJsonObject(String startingSide, String color) {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty(JsonRequestCreator.COMMAND, "join");
        jsonObj.addProperty(JsonRequestCreator.STARTING_SIDE, startingSide);
        jsonObj.addProperty(JsonRequestCreator.COLOR, color);
        return jsonObj;
    }

    public JsonObject createRequestStartGame() {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty(JsonRequestCreator.COMMAND, "start");
        return jsonObj;
    }
}
