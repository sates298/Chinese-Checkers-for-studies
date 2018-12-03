package board;

import field.Field;

import java.util.ArrayList;
import java.util.List;

public enum SixPointedStarSide implements BoardSide{

    TOP{
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
    LEFT_TOP{
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
    RIGHT_TOP{
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
    BOTTOM{
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
    LEFT_BOTTOM{
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
    RIGHT_BOTTOM{
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

    public List<Field> getArea(Board board) {
        return null;
    }

    public abstract List<Field> getOppositeArea(SixPointedStar board);
}
