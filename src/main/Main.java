package main;

import Network.NetworkManager;

import javax.swing.JFrame;
import java.io.IOException;
import java.util.List;
/*The project is almost finished. The message handling and sending messages methods already exist.
* The only thing that's missing is getting the Network Manager to work between 2 projects, since each game creates its own network manager
* I have some ideas on solving the issue, but I require a bit more time.*/
public class Main {
    public static void main(String[] args) {
        NetworkManager networkManager = new NetworkManager();

        GamePanel gp1 = new GamePanel(networkManager);
        JFrame window1 = createChessWindow(networkManager, "Chess - Window 1", gp1);
        window1.pack();
        window1.setVisible(true);
        gp1.LaunchGame();

        GamePanel gp2 = new GamePanel(networkManager,"black");
        JFrame window2 = createChessWindow(networkManager, "Chess - Window 2", gp2);
        window2.pack();
        window2.setLocationRelativeTo(null);
        window2.setVisible(true);
        gp2.LaunchGame();

        new Thread(() -> {
            while (true) {
                try {
                    //
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