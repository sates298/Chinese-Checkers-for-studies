package server.board;

import server.creator.fieldCreator.FieldCreator;
import server.creator.fieldCreator.PawnCreator;
import server.field.Field;
import server.field.Pawn;
import server.player.Player;


public class SixPointedStar extends Board {


    public SixPointedStar(){
        setType("SixPointedStar");
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

    @Override
    public void setPawns(Player p) {
        FieldCreator creator = new PawnCreator();
        Pawn created;
        if(SixPointedStarSide.TOP == p.getStartingSide()){
            for(int i=0; i<4; i++){
                for(int j=0; j<=i; j++){
                    created = (Pawn)creator.createField(4 + j, i);
                    p.setOnePawn(created);
                    setOneField(created);
                }
            }
        } else if(SixPointedStarSide.LEFT_TOP == p.getStartingSide()){
            for(int i=0; i<4; i++){
                for(int j=3; j>=i; j--){
                    created = (Pawn)creator.createField(j, 4 + i);
                    p.setOnePawn(created);
                    setOneField(created);
                }
            }
        }else if(SixPointedStarSide.RIGHT_TOP == p.getStartingSide()){
            for(int i=0; i<4; i++){
                for(int j=3; j>=i; j--){
                    created = (Pawn)creator.createField(9 + j, 4 + i);
                    p.setOnePawn(created);
                    setOneField(created);
                }
            }
        }else if(SixPointedStarSide.BOTTOM == p.getStartingSide()){
            for(int i=0; i<4; i++){
                for(int j=3; j>=i; j--){
                    created = (Pawn)creator.createField(9 + j, 13 + i);
                    p.setOnePawn(created);
                    setOneField(created);
                }
            }
        }else if(SixPointedStarSide.LEFT_BOTTOM == p.getStartingSide()){
            for(int i=0; i<4; i++){
                for(int j=0; j<=i; j++){
                    created = (Pawn) creator.createField(4 + j, 9 + i);
                    p.setOnePawn(created);
                    setOneField(created);
                }
            }
        }else if(SixPointedStarSide.RIGHT_BOTTOM == p.getStartingSide()){
            for(int i=0; i<4; i++){
                for(int j=0; j<=i; j++){
                    created = (Pawn)creator.createField(13 + j, 9 + i);
                    p.setOnePawn(created);
                    setOneField(created);
                }
            }
        }
    }

}
