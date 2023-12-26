package piece;

import main.GamePanel;
import main.PieceType;

public class Queen extends Piece{
    public Queen(int color,int col, int row){
        super(color,col,row);
        type= PieceType.QUEEN;
        if(color== GamePanel.WHITE){
            image=getImage("/Chess_qlt60");
        }
        else{
            image=getImage("/Chess_qdt60");
        }
    }

    @Override
    public boolean canMove(int col, int row) {
        //rook and bishop combined
        if(isWithin(col,row)&&!SameSquare(col,row)){
            if(col==prevCol||row==prevRow){
                if(isValidSquare(col,row)&&!pieceInWayParallel(col,row)){
                    return true;
                }
            }
            if(Math.abs(col-prevCol)==Math.abs(row-prevRow)){
                if(isValidSquare(col,row)&&!pieceInTheWayDiagonal(col,row)){
                    return true;
                }
            }
        }
        return false;
    }
}
