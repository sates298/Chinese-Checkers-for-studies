package serverTests;

import org.junit.Test;
import server.board.SixPointedStar;
import server.board.SixPointedStarSide;
import server.field.EmptyField;
import server.field.Field;
import server.field.NoField;
import server.field.Pawn;
import server.player.Color;
import server.player.Player;

import static org.junit.Assert.assertEquals;

public class FieldTest {
  @Test
  public void testEmptyFieldToString() {
    Field f = new EmptyField(0, 0);

    assertEquals("{\"type\":\"EmptyField\",\"x\":0,\"y\":0,\"color\":\"null\"}",
        f.toString());
  }
  @Test
  public void testNoFieldToString() {
    Field f = new NoField(0, 0);

    assertEquals("{\"type\":\"NoField\",\"x\":0,\"y\":0,\"color\":\"null\"}",
        f.toString());
  }
  @Test
  public void testPawnToString() {
    Pawn p = new Pawn(0, 0);

    p.setOwner(new Player(SixPointedStarSide.TOP, Color.BLACK));

    assertEquals("{\"type\":\"Pawn\",\"x\":0,\"y\":0,\"owner\":0,\"color\":\"BLACK\"}",
        p.toString());
  }
}
