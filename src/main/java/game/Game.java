package game;

import board.Board;
import movement.Movement;
import player.Player;

import java.util.List;

public class Game {
  private int gameId;
  private Board board;
  private Movement movement;
  private GameController controller;
  private List<Player> players;

  public Game(int gameId, Board board, Movement movement, GameController controller, List<Player> players) {
    this.gameId = gameId;
    this.board = board;
    this.movement = movement;
    this.controller = controller;
    this.players = players;
  }

  public int getGameId() {
    return gameId;
  }

  public Board getBoard() {
    return board;
  }

  public Movement getMovement() {
    return movement;
  }

  public GameController getController() {
    return controller;
  }

  public List<Player> getPlayers() {
    return players;
  }
}
