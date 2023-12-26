package piece;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import main.Board;
import main.GamePanel;

import javax.imageio.ImageIO;

public class Piece {
    public int x,y;
    public BufferedImage image;
    public int col,row, prevCol,prevRow;
    public int color;
    public Piece collision;
    public boolean isMoved;

    public Piece(int color,int col, int row){
        this.color=color;
        this.col=col;
        this.row=row;
        prevCol=col;
        prevRow=row;
        x=getX(col);
        y=getY(row);
    }
    public BufferedImage getImage(String path){
        BufferedImage image=null;
        try{
            image= ImageIO.read(getClass().getResourceAsStream(path+".png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return image;
    }
    public int getX(int col){
        return col*Board.SQUARE_SIZE;
    }
    public int getY(int row){
        return row*Board.SQUARE_SIZE;
    }
    //+size/2 to get the center of the square
    public int getCol(int x){
        return (x+(Board.SQUARE_SIZE/2))/Board.SQUARE_SIZE;
    }
    public int getRow(int y){
        return (y+(Board.SQUARE_SIZE/2))/Board.SQUARE_SIZE;
    }
    public int getIndex(){
        for (int i = 0; i < GamePanel.simPieces.size(); i++) {
            if(GamePanel.simPieces.get(i)==this){
                return i;
            }
        }
        return 0;
    }
    public void updatePosition(){
        x=getX(col);
        y=getY(row);
        //update previous position since move is complete
        prevCol=getCol(x);
        prevRow=getRow(y);
        isMoved=true;
    }
    public void resetPosition(){
        col=prevCol;
        row=prevRow;
        x=getX(col);
        y=getY(row);
    }
    public boolean canMove(int col, int row){
        return false;
    }
    public boolean isWithin(int targetRow,int targetCol){
        if(targetRow>=0 && targetRow<8 && targetCol>=0 && targetCol<8){
            return true;
        }
        return false;
    }
    public Piece getCollision(int targetCol, int targetRow){
        System.out.println("targetCol: " + targetCol + ", targetRow: " + targetRow);
        for (Piece piece : GamePanel.simPieces) {
            System.out.println("piece.col: " + piece.col + ", piece.row: " + piece.row);

            if (!piece.equals(this) && piece.col == targetCol && piece.row == targetRow) {
                return piece;
            }

        }
        return null;
    }
    public boolean isValidSquare(int targetCol,int targetRow){
        collision=getCollision(targetCol,targetRow);
        if(collision==null){
            return true;
        }
        else{
            if(this.color==collision.color){
                collision=null;
            }
            else{//move to square to capture it
                return true;
            }
        }
        return false;
    }
    public boolean SameSquare(int targetCol,int targetRow){
        if(targetCol==prevCol&&targetRow==prevRow){
            return true;
        }
        return false;
    }
    public boolean pieceInWayParallel(int col,int row){
        //left
        for (int i = prevCol; i >col; i--) {
            for(Piece p:GamePanel.simPieces){
                if(p.col==i&&p.row==row){
                    collision=p;
                    return true;
                }
            }
        }
        //right
        for (int i = prevCol+1; i <col; i++) {
            for(Piece p:GamePanel.simPieces){
                if(p.col==i&&p.row==row){
                    collision=p;
                    return true;
                }
            }
        }
        //up
        for (int i = prevRow-1; i >row; i--) {
            for(Piece p:GamePanel.simPieces){
                if(p.row==i&&p.col==col){
                    collision=p;
                    return true;
                }
            }
        }
        //down
        for (int i = prevRow+1; i <row; i++) {
            for(Piece p:GamePanel.simPieces){
                if(p.row==i&&p.col==col){
                    collision=p;
                    return true;
                }
            }
        }
        return false;
    }
    public boolean pieceInTheWayDiagonal(int col, int row){
        if(row<prevRow) {
            //up left
            for (int i = prevCol-1; i >col; i--) {
                int diff = Math.abs(i-prevCol);
                for(Piece p:GamePanel.simPieces){
                    if(p.col==i&&p.row==prevRow-diff){
                        collision=p;
                        return true;
                    }
                }
            }
            //up right
            for (int i = prevCol+1; i <col; i++) {
                int diff = Math.abs(i-prevCol);
                for(Piece p:GamePanel.simPieces){
                    if(p.col==i&&p.row==prevRow-diff){
                        collision=p;
                        return true;
                    }
                }
            }
            }
        if(row>prevRow) {
            //down left
            for (int i = prevCol-1; i >col; i--) {
                int diff = Math.abs(i-prevCol);
                for(Piece p:GamePanel.simPieces){
                    if(p.col==i&&p.row==prevRow+diff){
                        collision=p;
                        return true;
                    }
                }
            }
            //down right
            for (int i = prevCol+1; i <col; i++) {
                int diff = Math.abs(i-prevCol);
                for(Piece p:GamePanel.simPieces){
                    if(p.col==i&&p.row==prevRow+diff){
                        collision=p;
                        return true;
                    }
                }
            }
        }

        return false;
    }
    public void draw(Graphics2D g2){
        g2.drawImage(image,x,y,Board.SQUARE_SIZE,Board.SQUARE_SIZE,null);
    }
}
