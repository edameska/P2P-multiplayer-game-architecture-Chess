package main;

import piece.Piece;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable{
    public static final int WIDTH = 680;
    public static final int HEIGHT = 480;
    final int FPS=60;
    Thread gameThread;
    Board board= new Board();
    //pieces
    public static ArrayList<Piece> pieces=new ArrayList<Piece>();//backupList
    public static ArrayList<Piece> simPieces=new ArrayList<Piece>();
    public static final int WHITE=1;
    public static final int BLACK=0;
    int currentColor=WHITE;
    public GamePanel() {
        //setFocusable(true);
        //requestFocus();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setPieces();
        CopyPieces(pieces,simPieces);
    }
    public void LaunchGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void setPieces(){
        //white pieces
        pieces.add(new piece.Pawn(WHITE,0,6));
        pieces.add(new piece.Pawn(WHITE,1,6));
        pieces.add(new piece.Pawn(WHITE,2,6));
        pieces.add(new piece.Pawn(WHITE,3,6));
        pieces.add(new piece.Pawn(WHITE,4,6));
        pieces.add(new piece.Pawn(WHITE,5,6));
        pieces.add(new piece.Pawn(WHITE,6,6));
        pieces.add(new piece.Pawn(WHITE,7,6));
        pieces.add(new piece.Rook(WHITE,0,7));
        pieces.add(new piece.Knight(WHITE,1,7));
        pieces.add(new piece.Bishop(WHITE,2,7));
        pieces.add(new piece.Queen(WHITE,3,7));
        pieces.add(new piece.King(WHITE,4,7));
        pieces.add(new piece.Bishop(WHITE,5,7));
        pieces.add(new piece.Knight(WHITE,6,7));
        pieces.add(new piece.Rook(WHITE,7,7));
        //black pieces
        pieces.add(new piece.Pawn(BLACK,0,1));
        pieces.add(new piece.Pawn(BLACK,1,1));
        pieces.add(new piece.Pawn(BLACK,2,1));
        pieces.add(new piece.Pawn(BLACK,3,1));
        pieces.add(new piece.Pawn(BLACK,4,1));
        pieces.add(new piece.Pawn(BLACK,5,1));
        pieces.add(new piece.Pawn(BLACK,6,1));
        pieces.add(new piece.Pawn(BLACK,7,1));
        pieces.add(new piece.Rook(BLACK,0,0));
        pieces.add(new piece.Knight(BLACK,1,0));
        pieces.add(new piece.Bishop(BLACK,2,0));
        pieces.add(new piece.Queen(BLACK,3,0));
        pieces.add(new piece.King(BLACK,4,0));
        pieces.add(new piece.Bishop(BLACK,5,0));
        pieces.add(new piece.Knight(BLACK,6,0));
        pieces.add(new piece.Rook(BLACK,7,0));
    }
    private void CopyPieces(ArrayList<Piece> source, ArrayList<Piece> destination) {
        destination.clear();
        for(int i=0; i<source.size() ;i++){
            destination.add(source.get(i));
        }
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
        //board
        board.draw(g2);
        //pieces
        for(Piece piece:simPieces){
            piece.draw(g2);
        }
    }

}
