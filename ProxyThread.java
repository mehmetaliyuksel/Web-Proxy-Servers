import java.net.*;
import java.io.*;

public class ProxyThread extends Thread {

    Socket socket;

    ProxyThread(Socket socket) {
        super();
        this.socket = socket;
        // System.out.println("IN PROXY THREAD");
    }

    @Override
    public void run() {
        try {
            // Initialize I/O objects to communicate with client
            BufferedReader reader = new BufferedReader(new InputStreamReader((socket.getInputStream())));
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            // 1. Read request

            HTTPRequest request = new HTTPRequest(reader);

            if (!"GET".equals(request.method)) { // Checks header
                socket.close();
                return;
            } else if (request.checkLength > 9999) { // Checks if request is too Long
                HTTPResponse.sendTooLong(new PrintWriter(outputStream));
                reader.close();
                outputStream.close();
            } else {
                System.out.println("* " + request);
            }

            // 2. Send request to the web server and get response

            HTTPResponse response = request.getResponse();

            // 3. Send back response to the client
            if (response != null)
                response.send(outputStream);
            else { // If web server is not open
                HTTPResponse.sendNotFound(new PrintWriter(outputStream));
                reader.close();
                outputStream.close();
            }

            // 4. Close connection
            reader.close();
            outputStream.close();

        } catch (Exception e) {
            System.err.println("* " + e);
            e.printStackTrace();
        }

        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
