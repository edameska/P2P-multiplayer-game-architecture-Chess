package piece;

import main.GamePanel;


public class Pawn  extends Piece{
    public Pawn(int color,int col, int row){
       super(color,col,row);
            if(color==GamePanel.WHITE){
                image=getImage("/Chess_plt60");
            }
            else{
                image=getImage("/Chess_pdt60");
            }
    }
    @Override
    public boolean canMove(int col, int row) {
        if(isWithin(row,col)&&!SameSquare(row,col)){
            //check color to define direction
            int MoveVal;
            if(color==GamePanel.WHITE) MoveVal=-1;
            else MoveVal=1;
            collision=getCollision(col,row);
            if(col==prevCol&&row==prevRow+MoveVal&&collision==null){
                return true;
            }
            //first move, can go by 2
            if(col==prevCol&& row==prevRow+MoveVal*2&&collision==null&&!isMoved&&!pieceInWayParallel(col,row)){
                return true;
            }
            //diagonal capture
            if (Math.abs(col - prevCol) == 1 && row == prevRow + MoveVal && collision != null && collision.color != color) {
                return true;
            }

        }
        return false;
    }
}
