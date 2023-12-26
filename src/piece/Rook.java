package piece;

import main.GamePanel;
import main.PieceType;

public class Rook extends Piece{
    public Rook(int color,int col, int row){
        super(color,col,row);
        type= PieceType.ROOK;
        if(color== GamePanel.WHITE){
            image=getImage("/Chess_rlt60");
        }
        else{
            image=getImage("/Chess_rdt60");
        }
    }

    @Override
    public boolean canMove(int col, int row) {
        if(isWithin(row,col)&&!SameSquare(col,row)){
            if(col==prevCol||row==prevRow){
                if(isValidSquare(col,row)&&!pieceInWayParallel(col,row)){
                    return true;
                }
            }
    }
        return false;
    }
}
