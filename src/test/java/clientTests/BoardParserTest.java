package clientTests;

import client.drawableBoard.BoardParser;
import client.drawableBoard.DrawableField;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.board.Board;
import server.creator.boardCreator.BoardCreator;
import server.creator.boardCreator.SixPointedStarCreator;

import static org.junit.Assert.assertTrue;

public class BoardParserTest {

    private Board board;
    private String boardInString;


    @Before
    public void setUp(){
        BoardCreator creator = new SixPointedStarCreator();
        this.board = creator.createBoard();
        this.boardInString = this.board.fieldsToString();
    }

    @After
    public void tearDown(){
        this.board = null;
        this.boardInString = null;
    }

    @Test
    public void testParser(){
        DrawableField[][] parsed = BoardParser.parseBoard(this.boardInString);
        int[] fieldsQuantity = howManyFields(parsed);
        assertTrue(fieldsQuantity[0] == 168 &&
                fieldsQuantity[1] == 121 &&
                fieldsQuantity[2] == 0
        );
    }

    private int[] howManyFields(DrawableField[][] parsed){
        //indexes : 0 - noFields, 1 - emptyFields, 2 - pawns
        int[] result = new int[3];
        for (DrawableField[] aParsed : parsed) {
            for (DrawableField anAParsed : aParsed) {
                if ("\"NoField\"".equals(anAParsed.getType())) {
                    result[0]++;
                } else if ("\"EmptyField\"".equals(anAParsed.getType())) {
                    result[1]++;
                } else if ("\"Pawn\"".equals(anAParsed.getType())) {
                    result[2]++;
                }
            }
        }

        return result;
    }

}
