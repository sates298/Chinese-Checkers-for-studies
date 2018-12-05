package server;

import server.game.Game;

import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<Game> games;
    private static Server instance;

    public Server(){
        games = new ArrayList<>();
    }


    public static Server getInstance() {
        if(instance == null){
            instance = new Server();
        }

        return instance;
    }

    public List<Game> getGames() {
        return games;
    }
}
