package Network;
import java.io.*;
import java.net.Socket;
public class Peer implements Runnable{
    Socket connection;
    BufferedReader in;
    BufferedWriter out;
    NetworkManager networkManager;

    public Peer(Socket connection, NetworkManager networkManager) {
        this.connection = connection;
        this.networkManager=networkManager;
        //we can talk with this
        try{
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        String msg;
        try{
            while ((msg=in.readLine())!=null){
                System.out.println("Message is "+ msg);
                networkManager.broadcast(msg);

            }
        }catch (Exception e){
            System.out.println("Line broken?");
        }
        System.out.println("Connection with peer: "+connection.getInetAddress().getHostAddress()+" established!");
    }
    public void send(String message) {
        try
        {
            out.write(message);
            out.newLine();
            out.flush();
        }catch(IOException e){
            throw new RuntimeException(e);

        }


    }
}
