package main;

import Network.NetworkManager;

import javax.swing.JFrame;
import java.io.IOException;
import java.util.List;
/* The only thing I need to implement is changing the text in both games and changing the moves, which I can do with a bit more time*/
public class Main {
    public static void main(String[] args) {
        NetworkManager networkManager = new NetworkManager();

        GamePanel gp1 = new GamePanel(networkManager);
        JFrame window1 = createChessWindow(networkManager, "Chess - Player 1", gp1);
        window1.pack();
        window1.setVisible(true);
        gp1.LaunchGame();

        GamePanel gp2 = new GamePanel(networkManager,"black");
        JFrame window2 = createChessWindow(networkManager, "Chess - Player 2", gp2);
        window2.pack();
        window2.setLocationRelativeTo(null);
        window2.setVisible(true);
        gp2.LaunchGame();

        new Thread(() -> {
            while (true) {
                try {
                    //Recieves messages and applies them to the game
                    List<Object> messages = networkManager.receiveMessages();
                    for (Object message : messages) {
                        Move move = (Move) message;
                        gp1.applyMove(move);
                        gp2.applyMove(move);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static JFrame createChessWindow(NetworkManager networkManager, String title, GamePanel gamePanel) {
        JFrame window = new JFrame(title);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.add(gamePanel);
        return window;
    }
}