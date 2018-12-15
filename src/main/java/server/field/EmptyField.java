package server.field;

public class EmptyField extends Field{

    public EmptyField(int x, int y){
        super(x,y);
    }

    @Override
    public String toString() {
        return "{\"type\":\"EmptyField\"" +
                ",\"x\": " + getX() +
                ",\"y\": " + getY() +
                "}";
    }
}
