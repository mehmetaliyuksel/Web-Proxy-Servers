import java.net.ServerSocket;
import java.net.Socket;

public final class HttpServer {

  public static void main(String[] args) throws Exception {
    int port = Integer.parseInt(args[0]);
    final ServerSocket server = new ServerSocket(port);
    System.out.println("Server open, ready to accept connections...");
    Socket client = null;
    while (true) {
      client = server.accept();
      System.out.println("Connected..." + client.toString());
      final HttpRequestHandler request = new HttpRequestHandler(client);
      Thread thread = new Thread(request);
      thread.start();

    }
  }
}