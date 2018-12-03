package field;

public abstract class Field {
    int x;
    int y;

    public Field(){
        this.x = 0;
        this.y = 0;
    }
    public Field(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
