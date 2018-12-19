package server.game;


import server.board.BoardSide;
import server.board.SixPointedStar;
import server.board.SixPointedStarSide;
import server.exception.*;
import server.field.Field;
import server.field.Pawn;
import server.movement.MainMovement;
import server.player.Bot;
import server.player.Color;
import server.player.MoveToken;
import server.player.Player;
import server.player.botAlgorithm.BotAlgorithmTemplate;
import server.player.botAlgorithm.BotMainMovementOnSPStarAlgorithm;

import java.util.*;
import java.util.stream.Collectors;

public class GameController {
    private Game actual;
    private Player currentTurnPlayer;
    private ListIterator<Player> playerListIterator;
    private boolean started;

    public GameController(Game actual) {
        this.actual = actual;
    }

    public Player getCurrentTurnPlayer() {
        return currentTurnPlayer;
    }

    public void startGame() {
        //add bots to game
        int diff = this.actual.getNumberOfPlayers() - this.actual.getPlayers().size();
        while (diff > 0) {
            addBot();
            diff--;
        }

        // sort players by starting sides (add values to  starting sides)
        this.actual.getPlayers().sort(Comparator.comparingInt((Player p) -> p.getStartingSide().getNum()));
        this.playerListIterator = this.actual.getPlayers().listIterator();
        // set player's id to his index in list (done in addPlayer() )
        //this.actual.getPlayers().forEach(p -> p.setId(this.actual.getPlayers().indexOf(p)));

        currentTurnPlayer = playerListIterator.next();
        //allow current player to move
        currentTurnPlayer.setMoveToken(MoveToken.ALLOW);
        started = true;
    }

    public void endGame() {

    }

    public void move(int playerId, int pawnX, int pawnY, int targetX, int targetY) throws ForbiddenMoveException, ForbiddenActionException {
        if (playerId != this.currentTurnPlayer.getId() || this.currentTurnPlayer.getMoveToken().getNum() == 0) {
            throw new ForbiddenActionException();
        }
        // find pawn and target  by coordinates
        Optional<Pawn> optionalPawn = this.currentTurnPlayer.getPawns().stream()
                .filter(p -> p.getX() == pawnX && p.getY() == pawnY).findFirst();
        Field target = this.actual.getBoard().getOneField(targetX, targetY);

        if (optionalPawn.isPresent()) {
            this.actual.getMovement().move(optionalPawn.get(), target);
        } else {
            throw new ForbiddenActionException();
        }
    }

    public void endTurn(int playerId) throws ForbiddenActionException {
        if (this.currentTurnPlayer.getId() != playerId) {
            throw new ForbiddenActionException();
        }
        //reset chosen pawn
        this.currentTurnPlayer.setLastMoved(null);
        //forbid current player to move if is not a winner yet
        if (checkWinCondition()) {
            this.currentTurnPlayer.setMoveToken(MoveToken.WINNER);
        } else {
            this.currentTurnPlayer.setMoveToken(MoveToken.FORBID);
        }
        Player prev = this.currentTurnPlayer;

        // set current player to next player who has moveToken not equal 3 in player list
        do {
            if (playerListIterator.hasNext()) {
                currentTurnPlayer = playerListIterator.next();

            } else {
                playerListIterator = this.actual.getPlayers().listIterator();
                currentTurnPlayer = playerListIterator.next();
            }
            //if only one player (or no one) has move token not equal 3, end game
            if (prev.getId() == this.currentTurnPlayer.getId()) {
                endGame();
                break;
            }

        } while (currentTurnPlayer.getMoveToken().getNum() == 3);
        //if only one player has move token not equal 3, end game
        if (prev.getId() == this.currentTurnPlayer.getId()) {
            endGame();
        }
        //allow next player to move
        this.currentTurnPlayer.setMoveToken(MoveToken.ALLOW);
    }

    public Player addPlayer(String sideStr, String colorStr) throws GameFullException, ColorUsedException, BoardSideUsedException {
        // find used color and starting side

        List<Color> usedColors = this.actual.getPlayers().stream().map(Player::getColor).collect(Collectors.toList());
        List<BoardSide> usedSides = this.actual.getPlayers().stream().map(Player::getStartingSide).collect(Collectors.toList());


        if (this.actual.getBoard() instanceof SixPointedStar) {
            BoardSide side = SixPointedStarSide.valueOfJson(sideStr);
            Color color = Color.valueOfJson(colorStr);
            if (this.actual.getPlayers().size() >= this.actual.getNumberOfPlayers()) {
                throw new GameFullException();
            }

            // check if side and color are  in  used sides
            if (usedSides.contains(side)) {
                throw new BoardSideUsedException();
            }
            if (usedColors.contains(color)) {
                throw new ColorUsedException();
            }

            Player p = new Player(side, color);
            this.actual.getBoard().setPawns(p);
            this.actual.getPlayers().add(p);
            p.setId(this.actual.getPlayers().indexOf(p));
            return p;

        }

        return null;
    }

    private void addBot() {
        List<BoardSide> sides = getEnabledSides();
        List<Color> colors = getEnabledColors();
        if(sides == null || colors == null){
            return;
        }
        BotAlgorithmTemplate botAlgorithm;
        if("SixPointedStar".equals(this.actual.getBoard().getType())){
            if(this.actual.getMovement() instanceof MainMovement){
                botAlgorithm = new BotMainMovementOnSPStarAlgorithm();
            }else{
                return;
            }
        }else{
            return;
        }

        Bot bot = new Bot(sides.get(0), colors.get(0), this.actual, botAlgorithm);
        this.actual.getBoard().setPawns(bot);
        this.actual.getPlayers().add(bot);
        bot.setId(this.actual.getPlayers().indexOf(bot));
    }


    private boolean checkWinCondition() {
        //Player should has minimum 1 pawn, otherwise this method shouldn't be called
        if (this.currentTurnPlayer.getStartingSide() instanceof SixPointedStarSide) {
            List<Field> winning =
                    ((SixPointedStarSide) this.currentTurnPlayer.getStartingSide()).getOppositeArea((SixPointedStar) this.actual.getBoard());
            int fieldsMatches = 0;

            for (Field f : winning) {
                for (Pawn p : this.currentTurnPlayer.getPawns()) {
                    if (p.equals(f)) {
                        fieldsMatches++;
                    }
                }
            }

            return fieldsMatches == this.currentTurnPlayer.getPawns().size();

        }
        return false;
    }

    public Map<Integer, String> getIdColorMap() {
        Map<Integer, String> result = new HashMap<>();
        for (Player p : actual.getPlayers()) {
            result.put(p.getId(), p.getColor().toString());
        }

        return result;
    }

    public boolean isStarted() {
        return started;
    }

    public List<Color> getEnabledColors() {
        List<Color> enabled = new ArrayList<>();
        Collections.addAll(enabled, Color.RED, Color.GREEN, Color.PURPLE, Color.BLACK, Color.YELLOW, Color.BLUE);
        for (Player p : this.actual.getPlayers()) {
            enabled.remove(p.getColor());
        }
        return enabled;
    }

    public List<BoardSide> getEnabledSides() {
        List<BoardSide> result = new ArrayList<>();

        if (actual.getBoard().getType().equals("SixPointedStar")) {
            if (this.actual.getPlayers().size() == 0) {
                Collections.addAll(result,
                        SixPointedStarSide.TOP, SixPointedStarSide.LEFT_TOP,
                        SixPointedStarSide.BOTTOM, SixPointedStarSide.LEFT_BOTTOM,
                        SixPointedStarSide.RIGHT_TOP, SixPointedStarSide.RIGHT_BOTTOM);
            }

            switch (this.actual.getNumberOfPlayers()) {
                case 2:
                    if (this.actual.getPlayers().size() == 1) {
                        result.clear();
                        result.add(
                                ((SixPointedStarSide) this.actual.getPlayers()
                                        .get(0).getStartingSide())
                                        .getOppositeSide()
                        );
                    }
                    break;
                case 3:
                    //  /\   something like this it looks
                    // /__\

                    if (this.actual.getPlayers().size() > 0) {
                        result.clear();
                        Player p = this.actual.getPlayers().get(0);
                        switch (p.getStartingSide().getNum()) {
                            case 0:
                                result.add(SixPointedStarSide.LEFT_BOTTOM);
                                result.add(SixPointedStarSide.RIGHT_BOTTOM);
                                break;
                            case 1:
                                result.add(SixPointedStarSide.LEFT_TOP);
                                result.add(SixPointedStarSide.BOTTOM);
                                break;
                            case 2:
                                result.add(SixPointedStarSide.LEFT_BOTTOM);
                                result.add(SixPointedStarSide.TOP);
                                break;
                            case 3:
                                result.add(SixPointedStarSide.LEFT_TOP);
                                result.add(SixPointedStarSide.RIGHT_TOP);
                                break;
                            case 4:
                                result.add(SixPointedStarSide.TOP);
                                result.add(SixPointedStarSide.RIGHT_BOTTOM);
                                break;
                            case 5:
                                result.add(SixPointedStarSide.BOTTOM);
                                result.add(SixPointedStarSide.RIGHT_TOP);
                                break;
                        }
                        if (this.actual.getPlayers().size() == 2) {
                            result.remove(
                                    this.actual.getPlayers()
                                            .get(1).getStartingSide()
                            );
                        }
                    }
                    break;
                case 4:
                    if (this.actual.getPlayers().size() == 1) {
                        result.clear();
                        Collections.addAll(result,
                                SixPointedStarSide.TOP, SixPointedStarSide.LEFT_TOP,
                                SixPointedStarSide.BOTTOM, SixPointedStarSide.LEFT_BOTTOM,
                                SixPointedStarSide.RIGHT_TOP, SixPointedStarSide.RIGHT_BOTTOM);
                        result.remove(
                                this.actual.getPlayers()
                                        .get(0).getStartingSide()
                        );
                    } else if (this.actual.getPlayers().size() == 2) {
                        result.clear();
                        Player p0 = this.actual.getPlayers().get(0);
                        Player p1 = this.actual.getPlayers().get(1);
                        if (p0.getStartingSide() != ((SixPointedStarSide) p1.getStartingSide()).getOppositeSide()) {
                            Collections.addAll(
                                    result,
                                    ((SixPointedStarSide) p0.getStartingSide())
                                            .getOppositeSide(),
                                    ((SixPointedStarSide) p1.getStartingSide())
                                            .getOppositeSide()
                            );
                        }
                    } else if (this.actual.getPlayers().size() == 3) {
                        result.clear();
                        Player p0 = this.actual.getPlayers().get(0);
                        Player p1 = this.actual.getPlayers().get(1);
                        Player p2 = this.actual.getPlayers().get(2);
                        if (p0.getStartingSide() == ((SixPointedStarSide) p1.getStartingSide()).getOppositeSide()) {
                            result.add(
                                    ((SixPointedStarSide) p2.getStartingSide()).getOppositeSide()
                            );
                        } else if (p2.getStartingSide() == ((SixPointedStarSide) p1.getStartingSide()).getOppositeSide()) {
                            result.add(
                                    ((SixPointedStarSide) p0.getStartingSide()).getOppositeSide()
                            );
                        } else {
                            result.add(
                                    ((SixPointedStarSide) p1.getStartingSide()).getOppositeSide()
                            );
                        }
                    }
                    break;
                case 6:
                    result.clear();
                    Collections.addAll(result,
                            SixPointedStarSide.TOP, SixPointedStarSide.LEFT_TOP,
                            SixPointedStarSide.BOTTOM, SixPointedStarSide.LEFT_BOTTOM,
                            SixPointedStarSide.RIGHT_TOP, SixPointedStarSide.RIGHT_BOTTOM);
                    for (Player p : this.actual.getPlayers()) {
                        result.remove(p.getStartingSide());
                    }
                default:
                    break;
            }
        }


        return result;
    }


}
