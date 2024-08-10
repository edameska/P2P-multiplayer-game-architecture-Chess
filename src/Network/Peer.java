package Network;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class Peer implements Runnable {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private NetworkManager networkManager;

    public Peer(Socket socket, NetworkManager networkManager) throws IOException {
        this.socket = socket;
        this.networkManager = networkManager;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object message = in.readObject();
                networkManager.receiveMessage(message);
                // Share known peers when a new peer connects
                if (message instanceof String && ((String) message).equals("REQUEST_PEERS")) {
                    List<String> knownPeers = networkManager.getKnownPeers();
                    sendMessage(knownPeers);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            stop(); // Ensure resources are cleaned up
        }
    }

    public void sendMessage(Object message) throws IOException {
        out.writeObject(message);
        out.flush();
    }

    public void stop() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
