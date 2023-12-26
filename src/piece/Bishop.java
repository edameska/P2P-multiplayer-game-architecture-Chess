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

    @Override
    public boolean canMove(int col, int row) {
        if(isWithin(col,row)&&!SameSquare(col,row)){
            //1:1 ratio of row and col
            if(Math.abs(col-prevCol)==Math.abs(row-prevRow)){
                if(isValidSquare(col,row)&&!pieceInTheWayDiagonal(col,row)){
                    return true;
                }
            }
        }
        return false;
    }
}
