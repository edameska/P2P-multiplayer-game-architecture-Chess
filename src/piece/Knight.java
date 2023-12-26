package piece;

import main.GamePanel;

public class Knight extends Piece{
    public Knight(int color,int col, int row){
        super(color,col,row);
        if(color== GamePanel.WHITE){
            image=getImage("/Chess_nlt60");
        }
        else{
            image=getImage("/Chess_ndt60");
        }
    }
}
