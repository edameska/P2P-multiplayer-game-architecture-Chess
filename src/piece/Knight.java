package piece;

import main.GamePanel;
import main.PieceType;

public class Knight extends Piece{
    public Knight(int color,int col, int row){
        super(color,col,row);
        type= PieceType.KNIGHT;
        if(color== GamePanel.WHITE){
            image=getImage("/Chess_nlt60");
        }
        else{
            image=getImage("/Chess_ndt60");
        }
    }

    @Override
    public boolean canMove(int col, int row) {
        if(isWithin(col,row)&&!SameSquare(col,row)){
            //movement ratio of knight is 1:2 or 2:1(2 rows 1 col or 2 cols 1 row)
            if(Math.abs(col-prevCol)*(Math.abs(row-prevRow))==2){
                if (isValidSquare(col,row)){
                    return true;
                }
            }
        }
        return false;
    }
}
