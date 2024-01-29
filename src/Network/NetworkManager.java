package Network;

import main.GamePanel;

import java.io.*;
import java.net.*;
import java.util.*;

public class NetworkManager {
    private ServerSocket serverSocket;
    private List<Socket> peerSockets;
    private List<ObjectOutputStream> outputStreams;
    private List<ObjectInputStream> inputStreams;
    private GamePanel gamePanel;

    public NetworkManager() {
        this.peerSockets = new ArrayList<>();
        this.outputStreams = new ArrayList<>();
        this.inputStreams = new ArrayList<>();
    }

    public void startServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                    peerSockets.add(socket);
                    outputStreams.add(outputStream);
                    inputStreams.add(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void connectToPeer(String ip, int port) throws IOException {
        Socket socket = new Socket(ip, port);
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        peerSockets.add(socket);
        outputStreams.add(outputStream);
        inputStreams.add(inputStream);
    }

    public void broadcastMessage(Object message) throws IOException {
        for (ObjectOutputStream outputStream : outputStreams) {
            outputStream.writeObject(message);
        }
    }

    public List<Object> receiveMessages() throws IOException, ClassNotFoundException {
        List<Object> messages = new ArrayList<>();
        for (ObjectInputStream inputStream : inputStreams) {
            if (inputStream.available() > 0) {
                messages.add(inputStream.readObject());
            }
        }
        return messages;
    }
    public void registerGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    public void broadcastGameState(String gameState) throws IOException {
        for (ObjectOutputStream outputStream : outputStreams) {
            outputStream.writeObject(gameState);
        }
    }

    public void broadcast(String message) throws IOException {
        for (ObjectOutputStream outputStream : outputStreams) {
            outputStream.writeObject(message);
        }
    }
    public void startListening() {
        for (ObjectInputStream inputStream : inputStreams) {
            new Thread(() -> {
                while (true) {
                    try {
                        if (inputStream.available() > 0) {
                            Object message = inputStream.readObject();
                            // Process the received message
                            gamePanel.receiveMessage(message.toString());
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}