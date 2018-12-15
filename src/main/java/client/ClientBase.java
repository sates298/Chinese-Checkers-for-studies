package client;

import client.drawableBoard.Draw;
import client.drawableBoard.DrawableField;
import client.drawableBoard.SixPointedStarDraw;

public class ClientBase {

    private int playerId;
    private String boardType;
    private String movementType;
    private Draw boardDraw;
    private DrawableField[][] startedBoard;

    private static ClientBase instance;

    public static ClientBase getInstance() {
        if(instance == null){
            instance = new ClientBase();
        }
        return instance;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getBoardType() {
        return boardType;
    }

    public void setBoardType(String boardType) {
        this.boardType = boardType;
        if(("SixPointedStar").equals(boardType)){
            this.boardDraw = new SixPointedStarDraw();
        }
    }

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public Draw getBoardDraw() {
        return boardDraw;
    }

    public DrawableField[][] getStartedBoard() {
        return startedBoard;
    }

    public void setStartedBoard(DrawableField[][] startedBoard) {
        this.startedBoard = startedBoard;
    }
}
