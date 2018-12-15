package client.drawableBoard;

import com.google.gson.Gson;

public class BoardParser {
  public static DrawableField[][] parseBoard(String boardRepresentation) {
    Gson gson = new Gson();

    String[][] strArray = gson.fromJson(boardRepresentation, String[][].class);
    DrawableField[][] fieldTypes = new DrawableField[strArray.length][strArray.length];  //

    for (int i=0; i<strArray.length; i++) {
      for (int j=0; j<strArray.length; j++) {
        fieldTypes[i][j] = new DrawableField(
            gson.toJsonTree(strArray[i][j]).getAsJsonObject().get("color").getAsString(),
            gson.toJsonTree(strArray[i][j]).getAsJsonObject().get("type").getAsString()
            );
      }
    }

    return fieldTypes;
  }
}
