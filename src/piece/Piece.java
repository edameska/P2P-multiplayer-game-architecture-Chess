package piece;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import main.Board;

import javax.imageio.ImageIO;

public class Piece {
    public int x,y;
    public BufferedImage image;
    public int col,row, prevCol,prevRow;
    public int color;

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
    public void updatePosition(){
        x=getX(col);
        y=getY(row);
        //update previous position since move is complete
        prevCol=getCol(x);
        prevRow=getRow(y);

    }
    public void draw(Graphics2D g2){
        g2.drawImage(image,x,y,Board.SQUARE_SIZE,Board.SQUARE_SIZE,null);
    }
}
