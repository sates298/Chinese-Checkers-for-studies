package server.board;

import com.google.gson.JsonArray;
import server.field.Field;
import server.field.Pawn;
import server.player.Player;


public abstract class Board {

    private Field[][] fields;

    public void setFields(Field[][] fields) {
        this.fields = fields;
    }

    public void setOneField(Field field){
        this.fields[field.getX()][field.getY()] = field;
    }

    public Field getOneField(int x, int y) throws ArrayIndexOutOfBoundsException{
        if(x > sizeX() || y > sizeY()){
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.fields[x][y];
    }

    public abstract int sizeY();
    public abstract int sizeX();
    public abstract void setPawns(Player player);

    public String fieldsToString(){
        JsonArray parentJsonArray = new JsonArray();

        for (int i=0; i<sizeY(); i++){
            JsonArray childJsonArray = new JsonArray();
            for (int j =0; j<sizeX(); j++){
                childJsonArray.add(fields[i][j].toString());
            }
            parentJsonArray.add(childJsonArray);
        }

        return parentJsonArray.toString();
    }

    public boolean isPawn(int x, int y) throws ArrayIndexOutOfBoundsException{
       /* if(x >= sizeX() || y >= sizeY()){
            throw new ArrayIndexOutOfBoundsException();
        }*/
        return getOneField(x,y) instanceof Pawn;
    }

}
