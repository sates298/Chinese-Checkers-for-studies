package server.player.botAlgorithm;

import server.exception.ForbiddenActionException;
import server.field.Pawn;
import server.player.Bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BotAlgorithmTemplate {
    public void play(Bot b) {
        prepareMove(b);
        chooseAndExecuteMove(b);
        endBotTurn(b);
    }

    private void prepareMove(Bot b){
        List<Pawn>  list = new ArrayList<>();
        while(!b.isMovable()){
            if(list.size() == b.getPawns().size()){
                b.setChosenPawn(null);
                break;
            }
            chooseRandomPawn(b, list);
            checkMoves(b);
        }
    }

    private void chooseRandomPawn(Bot b, List<Pawn> list) {
        List<Pawn> pawns = b.getPawns();
        Random rand = new Random();
        int index = rand.nextInt(b.getPawns().size());
        b.setChosenPawn(pawns.get(index));
        //save pawn if it hasn't chosen before
        if(!list.contains(b.getChosenPawn())){
            list.add(b.getChosenPawn());
        }
    }

    public abstract void checkMoves(Bot b);
    public abstract void chooseAndExecuteMove(Bot b);

    private void endBotTurn(Bot b){
        try {
            b.getGame().getController().endTurn(b.getId());
            b.getSender().sendToPlayers();
            b.setMovable(false);
        } catch (ForbiddenActionException e) {
            System.out.println("bot:" + b.getId() + " " + e.toString());
        }
    }
}
