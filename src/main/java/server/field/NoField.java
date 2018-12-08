package server.field;

public class NoField extends Field {
    public NoField(int x, int y){
        super(x,y);
    }

    @Override
    public String toString() {
        return "{type : NoField" +
                ", x : " + getX() +
                ", y : " + getY() +
                "}";
    }
}
