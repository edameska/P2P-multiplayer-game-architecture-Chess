package main;

import piece.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable{
    public static final int WIDTH = 680;
    public static final int HEIGHT = 480;
    final int FPS=60;
    Thread gameThread;
    Board board= new Board();
    Mouse mouse=new Mouse();
    //pieces
    public static ArrayList<Piece> pieces=new ArrayList<Piece>();//backupList
    public static ArrayList<Piece> simPieces=new ArrayList<Piece>();
    public static ArrayList<Piece> promotionPieces=new ArrayList<Piece>();
    Piece activePiece;//piece that player is currently holding
    public static Piece castlingPiece;//piece that is castling
    Piece checkingPiece;//piece that is checking
    public static final int WHITE=1;
    public static final int BLACK=0;
    int currentColor=WHITE;
    //booleans
    boolean canMove;
    boolean checkSquare;
    boolean promotion;
    boolean gameOver;
    public GamePanel() {
        //adding mouse listeners
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        //setting up panel
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(22, 91, 83));
        //setting up pieces
        //setPieces();
        Testing();
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
    public void Testing(){
        pieces.add(new Pawn(WHITE,7,6));
        pieces.add(new King(WHITE,3,7));
        pieces.add(new King(BLACK,0,3));
        pieces.add(new Bishop(BLACK,1,4));
        pieces.add(new Queen(BLACK,4,5));
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
        if(promotion){
            //promotion stops the game
            promote();
        }
        else {
            if (mouse.pressed) {
                if (activePiece == null) {
                    //if actieve piece is null check if a piiece can be picked up
                    for (Piece piece : simPieces) {
                        //pick up piece
                        if (piece.color == currentColor && piece.col == mouse.x / Board.SQUARE_SIZE && piece.row == mouse.y / Board.SQUARE_SIZE) {
                            activePiece = piece;
                        }
                    }
                } else {
                    simmulate();
                }
            }
            //mouse released
            if (!mouse.pressed) {
                if (activePiece != null) {
                    if (checkSquare) {
                        //update pieces in case of collision/capture
                        CopyPieces(simPieces, pieces);
                        activePiece.updatePosition();
                        if (castlingPiece != null) {
                            castlingPiece.updatePosition();
                        }
                        if(kingIsInCheck()){

                        }
                        //else{
                            if (canPromote()) {
                                promotion = true;
                            } else {
                                changeTurn();//next move
                            }
                        //}
                    } else {
                        //move is not valid, reset it
                        CopyPieces(pieces, simPieces);
                        activePiece.resetPosition();
                        activePiece = null;
                    }
                }
            }
        }
    }
    //thinking phase
    private void simmulate(){
        canMove=false;
        checkSquare=false;
        //restoring removed piece during simulation
        CopyPieces(pieces,simPieces);
        if(castlingPiece!=null){
            castlingPiece.col=castlingPiece.prevCol;
            castlingPiece.x=castlingPiece.getX(castlingPiece.col);
            castlingPiece=null;
        }
        activePiece.x=mouse.x-Board.SQUARE_SIZE/2;//so that mouse pointer is in the center of the piece
        activePiece.y=mouse.y-Board.SQUARE_SIZE/2;
        //setting row and column
        activePiece.col=activePiece.getCol(activePiece.x);
        activePiece.row=activePiece.getRow(activePiece.y);
        if(activePiece.canMove(activePiece.col,activePiece.row)){
            canMove=true;
            if(activePiece.collision!=null){
                simPieces.remove(activePiece.collision.getIndex());
            }
            checkCastle();
            if(!isIllegal(activePiece)&& !canCaptureKing()) {
                checkSquare = true;
            }
        }
    }
    private boolean isIllegal(Piece king){
        if(king.type==PieceType.KING){
            for(Piece piece:simPieces){
                if(piece.color!=king.color&&piece.canMove(king.col,king.row)&&piece!=king){
                        return true;
                }
            }
        }
        return false;
    }
    private boolean canCaptureKing(){
            Piece king= getKing(false);
            for(Piece piece:simPieces){
                if(piece.color!=king.color&&piece.canMove(king.col,king.row)&&piece!=king){
                    return true;
                }
            }
        return false;
    }

    private boolean kingIsInCheck(){
        Piece king= getKing(true);
        if(activePiece.canMove(king.col,king.row)){
            checkingPiece=activePiece;
            return true;
        }
        else{
            checkingPiece=null;
        }
        return false;
    }
    private Piece getKing(boolean opponent){
        Piece king=null;

            for (Piece piece : simPieces) {
                if(opponent){
                    if (piece.type == PieceType.KING && piece.color != currentColor) {
                        king = piece;
                    }
                }
                else{
                    if (piece.type == PieceType.KING && piece.color == currentColor) {
                        king = piece;
                    }
                }
        }
        return king;
    }
    private void checkCastle(){
        if(castlingPiece!=null){
            //small castling
            if(castlingPiece.col==0) {
                castlingPiece.col += 3;
            }
            if(castlingPiece.col==7){
                castlingPiece.col-=2;
            }
            castlingPiece.x= castlingPiece.getX(castlingPiece.col);
            castlingPiece.y= castlingPiece.getY(castlingPiece.row);
        }
    }
    private void changeTurn(){
        if(currentColor==WHITE){
            currentColor=BLACK;
            //reset double step
            for(Piece piece:simPieces){
                if(piece.color==BLACK){
                    piece.twoStep=false;
                }
            }
        }
        else{
            currentColor=WHITE;
            for(Piece piece:simPieces){
                if(piece.color==WHITE){
                    piece.twoStep=false;
                }
            }
        }
        activePiece=null;
    }
    private boolean canPromote(){
        if(activePiece.type==PieceType.PAWN){
            if((activePiece.color==WHITE&&activePiece.row==0)||(activePiece.color==BLACK&&activePiece.row==7)){
                promotionPieces.clear();//clears list
                promotionPieces.add(new Queen(activePiece.color,9,2));//displays it outside of board
                promotionPieces.add(new Knight(activePiece.color,9,3));
                promotionPieces.add(new Bishop(activePiece.color,9,4));
                promotionPieces.add(new Rook(activePiece.color,9,5));
                return true;
            }
        }
        return false;
    }
    private void promote(){
        //selecting what to promote
        if(mouse.pressed){
            for(Piece piece:promotionPieces){
                if(piece.col==mouse.x/Board.SQUARE_SIZE&&piece.row==mouse.y/Board.SQUARE_SIZE){
                   switch (piece.type){
                       case QUEEN:
                           simPieces.add(new Queen(activePiece.color,activePiece.col,activePiece.row));
                           break;
                       case KNIGHT:
                           simPieces.add(new Knight(activePiece.color,activePiece.col,activePiece.row));
                           break;
                       case BISHOP:
                           simPieces.add(new Bishop(activePiece.color,activePiece.col,activePiece.row));
                           break;
                       case ROOK:
                           simPieces.add(new Rook(activePiece.color,activePiece.col,activePiece.row));
                           break;
                       default:
                           break;
                   }
                    simPieces.remove(activePiece.getIndex());
                   CopyPieces(simPieces,pieces);
                   activePiece=null;
                    promotion=false;
                    changeTurn();
                }
            }
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //board
        board.draw(g2);
        //pieces
        for(Piece piece:simPieces){
            piece.draw(g2);
        }
        if(activePiece!=null){
            if(canMove){

               if(isIllegal(activePiece)||canCaptureKing()){
                    g2.setColor(Color.GRAY);
                    g2.setComposite(AlphaComposite.SrcOver.derive(0.8f));//smaller opacity
                    g2.fillRect(activePiece.col * Board.SQUARE_SIZE, activePiece.row * Board.SQUARE_SIZE, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
                    g2.setComposite(AlphaComposite.SrcOver.derive(1f));//resets opacity
                }
               else{
                   g2.setColor(Color.PINK);
                   g2.setComposite(AlphaComposite.SrcOver.derive(0.8f));//smaller opacity
                   g2.fillRect(activePiece.col * Board.SQUARE_SIZE, activePiece.row * Board.SQUARE_SIZE, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
                   g2.setComposite(AlphaComposite.SrcOver.derive(1f));//resets opacity


               }
            }
            activePiece.draw(g2);
        }
        //display messages
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Times New Roman",Font.BOLD,20));
        g2.setColor(new Color(239, 202, 202));

        if(promotion){
            g2.drawString("Promote to:",500,130);
            for(Piece p:promotionPieces){
                g2.drawImage(p.image,p.getX(p.col),p.getY(p.row)+20,Board.SQUARE_SIZE,Board.SQUARE_SIZE,null);
            }
        }

        if(currentColor==WHITE){
            g2.drawString("White's Turn",500,400);
            if(checkingPiece!=null&&checkingPiece.color==BLACK){
                g2.setColor(new Color(203, 45, 92));
                g2.drawString("You're in check!",500,250);
            }
        }
        else{
            g2.drawString("Black's Turn",500,100);
            if(checkingPiece!=null&&checkingPiece.color==WHITE){
                g2.setColor(new Color(203, 45, 92));
                g2.drawString("You're in check!",500,250);
            }
        }

    }

}
