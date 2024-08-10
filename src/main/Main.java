package main;

import Network.NetworkManager;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        NetworkManager networkManager1 = new NetworkManager(12346);
        NetworkManager networkManager2 = new NetworkManager(12347);

        // Create GamePanel instances
        GamePanel gp1 = new GamePanel(networkManager1);
        JFrame window1 = createChessWindow("Chess - Player 1", gp1);
        window1.pack();
        window1.setVisible(true);
        gp1.LaunchGame();

        GamePanel gp2 = new GamePanel(networkManager1, "black");
        JFrame window2 = createChessWindow("Chess - Player 2", gp2);
        window2.pack();
        window2.setVisible(true);
        gp2.LaunchGame();

        // Start listening for incoming connections
        new Thread(() -> {
            try {
                networkManager1.startListening();
            } catch (RuntimeException e) {
                System.err.println("Failed to start listening on manager1: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                networkManager2.startListening();
            } catch (RuntimeException e) {
                System.err.println("Failed to start listening on manager2: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();

        // Connect to each other (replace with actual IPs if needed)
        new Thread(() -> {
            try {
                networkManager1.connectToPeer("localhost", 12347);
            } catch (RuntimeException e) {
                System.err.println("Failed to connect to manager2 from manager1: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                networkManager2.connectToPeer("localhost", 12346);
            } catch (RuntimeException e) {
                System.err.println("Failed to connect to manager1 from manager2: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    private static JFrame createChessWindow(String title, GamePanel gamePanel) {
        JFrame window = new JFrame(title);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.add(gamePanel);
        return window;
    }
}
