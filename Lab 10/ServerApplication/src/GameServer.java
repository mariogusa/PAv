import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    private ServerSocket serverSocket;
    private volatile boolean running = true;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (running) {
                Socket clientSocket = serverSocket.accept();
                //For each new client, spawn a thread:
                new Thread(new ClientThread(clientSocket, this)).start();
            }
        } catch (IOException e) {
            if (running) {
                e.printStackTrace();
            }
            // else — expected on shutdown
        } finally {
            stop();
        }
    }

    // Called by ClientThread when it sees the "stop" command
    public void stop() {
        running = false;
        System.out.println("Server shutting down...");
        try {
            if (serverSocket != null && !serverSocket.isClosed())
                serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 12345;            // port
        new GameServer().start(port);
    }
}