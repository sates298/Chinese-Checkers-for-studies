package client;

public class BaseClient {

    private int playerId;
    private String boardType;
    private String movementType;

    private static BaseClient instance;

    public static BaseClient getInstance() {
        if(instance == null){
            instance = new BaseClient();
        }
        return instance;
    }
}
