package board;


import field.Field;

public class SixPointedStar implements Board {
    private Field[][] fields;

    public SixPointedStar(){
        this.fields = new Field[17][17];
    }

    public Field[][] getFields() {
        return fields;
    }

    public void setOneField(Field field){
        this.fields[field.getX()][field.getY()] = field;
    }

    public Field getOneField(int x, int y){
        return this.fields[x][y];
    }

}
