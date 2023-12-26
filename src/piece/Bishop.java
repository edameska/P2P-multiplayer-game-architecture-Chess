package piece;

import main.GamePanel;

public class Bishop extends Piece{
    public Bishop(int color,int col, int row){
        super(color,col,row);
        if(color== GamePanel.WHITE){
            image=getImage("/Chess_blt60");
        }
        else{
            image=getImage("/Chess_bdt60");
        }
    }
}