package server.board;

import server.field.Field;
import server.field.Pawn;


public abstract class Board {

    private Field[][] fields;

    public Field[][] getFields() {
        return fields;
    }

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

    public String fieldsToStiring(){
        StringBuilder builder = new StringBuilder();
        builder.append("[ ");
        for(int i=0; i<sizeY(); i++){
            builder.append("[ ");
            for (int j=0; j< sizeX(); j++){
                builder.append(fields[j][i].toString());
            }
            builder.append("] , ");
        }
        builder.append("] ");
        return builder.toString();
    }

    public boolean isPawn(int x, int y) throws ArrayIndexOutOfBoundsException{
        if(x >= sizeX() || y >= sizeY()){
            throw new ArrayIndexOutOfBoundsException();
        }
        return getOneField(x,y) instanceof Pawn;
    }

}
