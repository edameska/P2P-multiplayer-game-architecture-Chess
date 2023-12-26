package piece;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;

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
    public void draw(Graphics2D g2){
        g2.drawImage(image,x,y,Board.SQUARE_SIZE,Board.SQUARE_SIZE,null);
    }
}
