import java.net.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // Opens a socket on port 8888 to listen requests
        ServerSocket server = new ServerSocket(8888);
        System.out.println("Server listening on port 8888!");

        while (true) {
            // Accepts request and starts a new thread to maintain concurrency
            Socket socket = server.accept();
            new ProxyThread(socket).start();
        }
    }
}
