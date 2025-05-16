import java.io.*;
import java.net.Socket;

public class GameClient {
    public static void main(String[] args) {
        String host = "localhost";
        int    port = 12345;

        try (
            Socket socket = new Socket(host, port);
            BufferedReader consoleIn = new BufferedReader(
                                        new InputStreamReader(System.in));
            PrintWriter    out       = new PrintWriter(
                                        socket.getOutputStream(), true);
            BufferedReader in        = new BufferedReader(
                                        new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("Connected to server at " + host + ":" + port);
            System.out.println("Type commands (create, join, move, stop). Type 'exit' to quit client.");

            String line;
            while ((line = consoleIn.readLine()) != null) {
                if (line.equalsIgnoreCase("exit")) {
                    System.out.println("Client exiting.");
                    break;
                }
                // send to server
                out.println(line);

                // read and print server response
                String response = in.readLine();
                if (response == null) {
                    System.out.println("Server closed connection.");
                    break;
                }
                System.out.println("Server says: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}