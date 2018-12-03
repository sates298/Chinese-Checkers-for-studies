package creator.boardCreator;

import board.Board;
import board.SixPointedStar;
import creator.fieldCreator.EmptyFieldCreator;
import creator.fieldCreator.NoFieldCreator;

public class SixPointedStarCreator extends BoardCreator {
    public Board createBoard() {
        SixPointedStar star = new SixPointedStar();
        fieldCreator = new NoFieldCreator();
        for(int i = 0; i < 17; i++){
            for(int j = 0; j < 17 ; j++){
                star.setOneField(fieldCreator.createField(i, j));
            }
        }
        fieldCreator = new EmptyFieldCreator();
        for(int j = 0; j < 13; j ++){
            for(int i=0; i <= j; i++){
                star.setOneField(fieldCreator.createField(i+4, j));
            }
        }

        for(int i=0; i<4; i++){
            for(int j=3; j>=i; j--){
                star.setOneField(fieldCreator.createField(j, 4 + i));
            }
        }

        for(int i=0; i<4; i++){
            for(int j=3; j>=i; j--){
                star.setOneField(fieldCreator.createField(9 + j, 4 + i));
            }
        }

        for(int i=0; i<4; i++){
            for(int j=3; j>=i; j--){
                star.setOneField(fieldCreator.createField(9 + j, 13 + i));
            }
        }


        return star;
    }
}
