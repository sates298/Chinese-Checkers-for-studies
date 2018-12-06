package server.board;

import server.field.Field;


public class SixPointedStar extends Board {

    public SixPointedStar(){
        setFields(new Field[17][17]);
    }

    @Override
    public int sizeY() {
        return 17;
    }

    @Override
    public int sizeX() {
        return 17;
    }
}
