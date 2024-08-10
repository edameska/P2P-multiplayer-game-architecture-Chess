package Network;

import main.GamePanel;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class NetworkManager {
    private List<Peer> peers;
    private List<String> knownPeers;
    private GamePanel gamePanel;
    private ServerSocket serverSocket;
    private int port;

    public NetworkManager(int port) {
        this.peers = new ArrayList<>();
        this.knownPeers = new ArrayList<>();
        this.port = port;
    }

    public void startListening() {
        while (!isPortAvailable(port)) {
            port++;
        }

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port " + port);
            new Thread(() -> {
                while (true) {
                    try {
                        Socket socket = serverSocket.accept();
                        System.out.println("Accepted connection from " + socket.getRemoteSocketAddress());
                        Peer peer = new Peer(socket, this);
                        peers.add(peer);
                        new Thread(peer).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException("Failed to bind to port: " + port, e);
        }
    }

    private boolean isPortAvailable(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void connectToPeer(String ip, int port) {
        try {
            Socket socket = new Socket(ip, port);
            Peer peer = new Peer(socket, this);
            peers.add(peer);
            new Thread(peer).start();
            System.out.println("Connected to peer at " + ip + ":" + port);
        } catch (IOException e) {
            throw new RuntimeException("Failed to connect to peer at " + ip + ":" + port, e);
        }
    }

    public void broadcastMessage(Object message) throws IOException {
        for (Peer peer : peers) {
            peer.sendMessage(message);
        }
    }

    public void registerGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void receiveMessage(Object message) {
        if (gamePanel != null) {
            gamePanel.receiveMessage(message.toString());
        } else {
            System.out.println("GamePanel not registered to receive messages.");
        }
    }

    public void addKnownPeer(String peerAddress) {
        if (!knownPeers.contains(peerAddress)) {
            knownPeers.add(peerAddress);
        }
    }

    public List<String> getKnownPeers() {
        return new ArrayList<>(knownPeers);
    }

    public void stop() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
        for (Peer peer : peers) {
            peer.stop();
        }
    }
}
