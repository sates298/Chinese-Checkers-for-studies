package client.drawableBoard;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BoardParser {
    public static DrawableField[][] parseBoard(String boardRepresentation) {
        Gson gson = new Gson();

        System.out.println(boardRepresentation);
        String[][] strArray = gson.fromJson(boardRepresentation, String[][].class);
        DrawableField[][] fieldTypes = new DrawableField[strArray.length][strArray.length];

        JsonParser parser = new JsonParser();
        JsonElement jsonTree;
        JsonObject jsonObject;
        for (int i = 0; i < strArray.length; i++) {
            for (int j = 0; j < strArray.length; j++) {
                jsonTree = parser.parse(strArray[i][j]);
                jsonObject = jsonTree.getAsJsonObject();
                fieldTypes[i][j] = new DrawableField(
                        jsonObject.get("color").toString(),
                        jsonObject.get("type").toString(),
                        jsonObject.get("x").getAsInt(),
                        jsonObject.get("y").getAsInt()
                );
            }
        }

        return fieldTypes;
    }
}
