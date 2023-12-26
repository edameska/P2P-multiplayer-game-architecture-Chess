package main;

import java.awt.*;

public class Board {
    final int COL = 8;
    final int ROW = 8;
    public static final int SQUARE_SIZE = 60;//square will be 100x100 pixels
    public static final int HALF_SQUARE_SIZE = SQUARE_SIZE/2;
    public void draw(Graphics2D g2){
        int color=0;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if(color==0){
                    g2.setColor(new Color(210, 246, 241));
                    color=1;
                }
                else{
                    g2.setColor(new Color(38, 151, 142));
                    color=0;
                }
                g2.fillRect(j*SQUARE_SIZE,i*SQUARE_SIZE,SQUARE_SIZE,SQUARE_SIZE);//(x,y,wifth,height)
            }
            //handling color row change
            if(color==0){
                color=1;
            }
            else{
                color=0;
            }
        }
    }
}
