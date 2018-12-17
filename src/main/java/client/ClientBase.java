package client;

import client.drawableBoard.Draw;
import client.drawableBoard.DrawableField;
import client.drawableBoard.SixPointedStarDraw;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientBase {

    private int playerId;
    private int gameId;
    private String boardType;
    private String movementType;
    private Draw boardDraw;
    private DrawableField[][] startedBoard;
    private List<Integer> openedGamesIds;
    private Map<Integer, Paint> playersToLabel;

    private DrawableField firstClicked;
    private DrawableField lastClicked;


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
        if(("SixPointedStar").equals(boardType) || ("\"SixPointedStar\"").equals(boardType)){
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

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public Map<Integer, Paint> getPlayersToLabel() {
        return playersToLabel;
    }

    public List<Integer> getOpenedGamesIds() {
        return openedGamesIds;
    }

    public void setOpenedGamesIds(List<Integer> openedGamesIds) {
        this.openedGamesIds = openedGamesIds;
    }

    public DrawableField getFirstClicked() {
        return firstClicked;
    }

    public void setFirstClicked(DrawableField firstClicked) {
        this.firstClicked = firstClicked;
    }

    public DrawableField getLastClicked() {
        return lastClicked;
    }

    public void setLastClicked(DrawableField lastClicked) {
        this.lastClicked = lastClicked;
    }

    public void setPlayersToLabel(Map<Integer, Paint> playersToLabel) {
        this.playersToLabel = playersToLabel;
    }

}
