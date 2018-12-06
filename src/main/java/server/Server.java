package server;

import server.game.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<Game> games;
    private static Server instance;

    private ServerSocket serverSocket;

    public Server(){
        games = new ArrayList<>();
    }


    public static Server getInstance() {
        if(instance == null){
            instance = new Server();
        }

        return instance;
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace(); // delete after testing phase and replace it with smth more user friendly
        }
        while (true) {
            try {
                new GameClientHandler(serverSocket.accept()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Game> getGames() {
        return games;
    }


    private class GameClientHandler {
        private Socket clientSocket;
        private PrintWriter  out;
        private BufferedReader in;

        private Game game;

        public GameClientHandler(Socket client) {
            this.clientSocket = client;
        }

        public void start() throws IOException {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                handleClientMessage(inputLine.split("\\s"));
                out.println(inputLine);
            }

            in.close();
            out.close();
            clientSocket.close();
        }

        private void handleClientMessage(String[] args) {
            if (args[0].equals("CONNECT")) {
                // request looks like "CONNECT [gameId]"

                connectToGame(Integer.parseInt(args[1]));

                return;
            }

            if (args[0].equals("CREATE")) {
                createGame();
            }
        }

        private void createGame() {
            Game g = new Game(); // probably needs a builder
            Server.this.games.add(g);
            connectToGame(g.getGameId());
        }

        private void connectToGame(int gameId) {
            // find the game with id equal to gameId
            this.game = games.stream().filter(g -> g.getGameId() == gameId).findFirst().get();
        }
    }
}
