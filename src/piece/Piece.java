package piece;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import main.Board;
import main.GamePanel;
import main.PieceType;

import javax.imageio.ImageIO;

public class Piece {
    public PieceType type;
    public int x,y;
    public BufferedImage image;
    public int col,row, prevCol,prevRow;
    public int color;
    public Piece collision;
    public boolean isMoved;
    public boolean twoStep;//has it moved 2 steps

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
        //check bieno pole
        if(type== PieceType.PAWN){
            if(Math.abs(row-prevRow)==2){
                twoStep=true;
            }
        }
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
        for (Piece piece : GamePanel.simPieces) {
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
    public boolean pieceInWayParallel(int col, int row) {
        int deltaX = col - prevCol;
        int deltaY = row - prevRow;

        // Check if the movement is horizontal
        if (deltaY == 0) {
            int step = Integer.compare(col, prevCol);
            for (int i = prevCol + step; i != col; i += step) {
                for (Piece p : GamePanel.simPieces) {
                    if (p.col == i && p.row == row) {
                        collision = p;
                        return true;
                    }
                }
            }
        }

        // Check if the movement is vertical
        if (deltaX == 0) {
            int step = Integer.compare(row, prevRow);
            for (int i = prevRow + step; i != row; i += step) {
                for (Piece p : GamePanel.simPieces) {
                    if (p.row == i && p.col == col) {
                        collision = p;
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean pieceInTheWayDiagonal(int col, int row) {
        int deltaCol = col - prevCol;
        int deltaRow = row - prevRow;

        // Check if the movement is along a diagonal
        if (Math.abs(deltaCol) == Math.abs(deltaRow)) {
            int stepCol = Integer.compare(col, prevCol);
            int stepRow = Integer.compare(row, prevRow);

            for (int i = 1; i < Math.abs(deltaCol); i++) {
                int checkCol = prevCol + i * stepCol;
                int checkRow = prevRow + i * stepRow;

                for (Piece p : GamePanel.simPieces) {
                    if (p.col == checkCol && p.row == checkRow) {
                        collision = p;
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
