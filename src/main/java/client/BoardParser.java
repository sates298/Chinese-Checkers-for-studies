package client;

import com.google.gson.Gson;

public class BoardParser {
  public static String[][] parseBoard(String boardRepresentation) {
    Gson gson = new Gson();

    String[][] strArray = gson.fromJson(boardRepresentation, String[][].class);
    String[][] fieldTypes = new String[strArray.length][strArray.length];  //

    for (int i=0; i<strArray.length; i++) {
      for (int j=0; j<strArray.length; j++) {
        fieldTypes[i][j] = gson.toJsonTree(strArray[i][j]).getAsJsonObject().get("type").getAsString();
      }
    }

    //todo find best form to pass board to draw

    return fieldTypes;
  }
}
