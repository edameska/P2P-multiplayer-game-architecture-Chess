package piece;

import main.GamePanel;
import main.PieceType;

public class King extends Piece{
    public King(int color,int col, int row){
        super(color,col,row);
        type= PieceType.KING;
        if(color== GamePanel.WHITE){
            image=getImage("/Chess_klt60");
        }
        else{
            image=getImage("/Chess_kdt60");
        }
    }

    @Override
    public boolean canMove(int col, int row) {
        if(isWithin(col,row)&&!SameSquare(col,row)){
            if(Math.abs(col-prevCol)<=1 && Math.abs(row-prevRow)<=1){
                if (isValidSquare(col,row)){
                    return true;
                }
            }
            if(!isMoved){
                //small castling
                if(col==prevCol+2 && row==prevRow&&!pieceInWayParallel(col,row)){
                    for(Piece p: GamePanel.simPieces){
                        if(p.col==prevCol+3 && p.row==prevRow && !p.isMoved){
                            GamePanel.castlingPiece = p;
                            return true;
                        }
                    }
                }
                //big castling
                if(col==prevCol-2 && row==prevRow&&!pieceInWayParallel(col,row)){
                    Piece p2[]=new Piece[2];//2 spaces must be empty
                    for(Piece p: GamePanel.simPieces) {
                        if(p.col == prevCol - 4 && p.row == prevRow && !p.isMoved) {
                            p2[0] = p;
                        }
                        if(p.col == prevCol - 3 && p.row == prevRow && !p.isMoved) {
                            p2[1] = p;
                        }
                        if (p2[0] != null && p2[1] == null && !p2[0].isMoved) {
                            GamePanel.castlingPiece = p2[0];
                            return true;
                        }

                    }
                }
            }
        }
        return false;
    }
}
