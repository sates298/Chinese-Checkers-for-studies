package client.drawableBoard;

import client.network.JsonRequestCreator;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BoardParser {

    public static final String TYPE = "type";
    public static final String X = "x";
    public static final String Y = "y";

    public static DrawableField[][] parseBoard(String boardRepresentation) {
        Gson gson = new Gson();
        String[][] strArray = gson.fromJson(boardRepresentation, String[][].class);
        return parseAllFields(strArray);
    }

    private static DrawableField[][] parseAllFields(String[][] strArray) {
        DrawableField[][] fieldTypes = new DrawableField[strArray.length][strArray.length];

        JsonParser parser = new JsonParser();
        JsonElement jsonTree;
        JsonObject jsonObject;
        for (int i = 0; i < strArray.length; i++) {
            for (int j = 0; j < strArray.length; j++) {
                jsonTree = parser.parse(strArray[i][j]);
                jsonObject = jsonTree.getAsJsonObject();
                fieldTypes[i][j] = parseOneField(jsonObject);
            }
        }
        return fieldTypes;
    }

    private static DrawableField parseOneField(JsonObject jsonObject) {
         return new DrawableField(
                jsonObject.get(JsonRequestCreator.COLOR).toString(),
                jsonObject.get(TYPE).toString(),
                jsonObject.get(X).getAsInt(),
                jsonObject.get(Y).getAsInt()
        );
    }
}
