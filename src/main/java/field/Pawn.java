package field;


import player.Player;

public class Pawn extends Field {
    private Player owner;

    public Pawn(int x, int y){
        super(x,y);
    }
    public void setOwner(Player player){
        this.owner = player;
    }
    public Player getOwner() {
        return owner;
    }

}
