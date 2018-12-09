package server.board;

import server.field.Field;

import java.util.ArrayList;
import java.util.List;

public enum SixPointedStarSide implements BoardSide{

    TOP(0){
        @Override
        public List<Field> getArea(Board board) {
            List<Field> area = new ArrayList<Field>();
            for(int i=0; i<4; i++){
                for(int j=0; j<=i; j++){
                    area.add(((SixPointedStar)board).getOneField(4 + j, i));
                }
            }

            return area;
        }

        public List<Field> getOppositeArea(SixPointedStar board) {
            return SixPointedStarSide.BOTTOM.getArea(board);
        }
    },
    LEFT_TOP(5){
        @Override
        public List<Field> getArea(Board board) {
            List<Field> area = new ArrayList<Field>();
            for(int i=0; i<4; i++){
                for(int j=3; j>=i; j--){
                    area.add(((SixPointedStar)board).getOneField(j, 4 + i));
                }
            }

            return area;
        }

        public List<Field> getOppositeArea(SixPointedStar board) {
            return SixPointedStarSide.RIGHT_BOTTOM.getArea(board);
        }
    },
    RIGHT_TOP(1){
        @Override
        public List<Field> getArea(Board board) {
            List<Field> area = new ArrayList<Field>();
            for(int i=0; i<4; i++){
                for(int j=3; j>=i; j--){
                    area.add(((SixPointedStar)board).getOneField(9 + j, 4 + i));
                }
            }

            return area;
        }

        public List<Field> getOppositeArea(SixPointedStar board) {
            return SixPointedStarSide.LEFT_BOTTOM.getArea(board);
        }
    },
    BOTTOM(3){
        @Override
        public List<Field> getArea(Board board) {
            List<Field> area = new ArrayList<Field>();
            for(int i=0; i<4; i++){
                for(int j=3; j>=i; j--){
                    area.add(((SixPointedStar)board).getOneField(9 + j, 13 + i));
                }
            }

            return area;
        }

        public List<Field> getOppositeArea(SixPointedStar board) {
            return SixPointedStarSide.TOP.getArea(board);
        }
    },
    LEFT_BOTTOM(4){
        @Override
        public List<Field> getArea(Board board) {
            List<Field> area = new ArrayList<Field>();
            for(int i=0; i<4; i++){
                for(int j=0; j<=i; j++){
                    area.add(((SixPointedStar)board).getOneField(4 + j, 9 + i));
                }
            }

            return area;
        }

        public List<Field> getOppositeArea(SixPointedStar board) {
            return SixPointedStarSide.RIGHT_TOP.getArea(board);
        }
    },
    RIGHT_BOTTOM(2){
        @Override
        public List<Field> getArea(Board board) {
            List<Field> area = new ArrayList<Field>();
            for(int i=0; i<4; i++){
                for(int j=0; j<=i; j++){
                    area.add(((SixPointedStar)board).getOneField(13 + j,9 + i));
                }
            }

            return area;
        }

        public List<Field> getOppositeArea(SixPointedStar board) {
            return SixPointedStarSide.LEFT_TOP.getArea(board);
        }
    };

    private int num;

    SixPointedStarSide(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public List<Field> getArea(Board board) {
        return null;
    }

    public abstract List<Field> getOppositeArea(SixPointedStar board);

}
