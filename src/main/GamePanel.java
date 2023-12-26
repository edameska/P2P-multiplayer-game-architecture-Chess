package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    final int FPS=60;
    Thread gameThread;
    Board board= new Board();
    public GamePanel() {
        setFocusable(true);
        requestFocus();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
    }
    public void LaunchGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        //game loop
        double drawInterval=1000000000.0/FPS;//1 second divided by FPS
        double delta=0;//time since last update
        long lastTime=System.nanoTime();//time at start of game
        long currentTime;
        while(gameThread!=null){//runs while game is running
            currentTime=System.nanoTime();//time at start of loop
            delta+=(currentTime-lastTime)/drawInterval;//adds time since last update to delta
            lastTime=currentTime;//sets lastTime to currentTime
            if(delta>=1){//if delta is greater than or equal to 1, update game
                update();
                repaint();
                delta--;//game is updated, so delta is decreased
            }
        }
    }
    private void update() {
    }
    public void paintComponent(Graphics g) {//if it doesnt work migrate the type in the function
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        board.draw(g2);
    }

}
