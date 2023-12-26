package piece;

import main.GamePanel;

public class King extends Piece{
    public King(int color,int col, int row){
        super(color,col,row);
        if(color== GamePanel.WHITE){
            image=getImage("/Chess_klt60");
        }
        else{
            image=getImage("/Chess_kdt60");
        }
    }

    @Override
    public boolean canMove(int col, int row) {
        if(isWithin(col,row)){
            if(Math.abs(col-prevCol)<=1 && Math.abs(row-prevRow)<=1){
                if (isValidSquare(col,row)){
                    return true;
                }
            }
        }
        return false;
    }
}
