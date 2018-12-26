package ClientServerCommunicationTests;

import client.ClientBase;
import client.network.ServerConnectionException;
import client.network.ServerConnector;
import org.junit.*;
import server.Server;
import server.creator.GameCreator;
import server.exception.WrongBoardTypeException;
import server.exception.WrongMovementTypeException;

import java.io.IOException;
import java.lang.reflect.Field;

public class CommunicationTest {
  private static Thread serverThread;
  @BeforeClass
  public static void initializeServer() throws IOException {
    serverThread = new Thread(() -> {
      Server.getInstance().start(1111);
    });
    serverThread.start();
  }

  @Test
  public void testHostGameScenario() throws ServerConnectionException, IOException, IllegalAccessException, InstantiationException, NoSuchFieldException {
    {
      Field field = ServerConnector.class.getDeclaredFields()[0];
      field.setAccessible(true);
      field.set(null, null);
    }
    {
      Field field = ClientBase.class.getDeclaredFields()[0];
      field.setAccessible(true);
      field.set(null, null);
    }


    ServerConnector.getInstance().makeConnection("localhost", 1111);
    ClientBase.getInstance().setBoardType("SixPointedStar");
    ClientBase.getInstance().setMovementType("main");
    ServerConnector.getInstance().requestCreateGame(6, 10);
    ServerConnector.getInstance().requestJoinGame("TOP", "RED");
    ServerConnector.getInstance().requestStartGame();

  }

  @Test
  public void testJoinAlreadyCreatedGameScenario() throws WrongMovementTypeException, WrongBoardTypeException, ServerConnectionException, IOException, IllegalAccessException, InstantiationException, NoSuchFieldException {
    {
      Field field = ServerConnector.class.getDeclaredFields()[0];
      field.setAccessible(true);
      field.set(null, null);
    }
    {
      Field field = ClientBase.class.getDeclaredFields()[0];
      field.setAccessible(true);
      field.set(null, null);
    }

    ServerConnector.getInstance().makeConnection("localhost", 1111);
    GameCreator gc = new GameCreator();
    int gameId = gc.createGame("\"SixPointedStar\"", "\"main\"",
        6, 10).getGameId();
    ClientBase.getInstance().setGameId(gameId);
    ServerConnector.getInstance().requestConnectToGame();
    ServerConnector.getInstance().requestJoinGame("TOP", "RED");
    ServerConnector.getInstance().requestStartGame();
  }




  @AfterClass
  public static void killServer() {
    serverThread.interrupt();
  }
}
