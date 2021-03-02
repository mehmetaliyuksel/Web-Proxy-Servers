import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class HttpRequestHandler implements Runnable {

  private Socket clientSocket;

  public HttpRequestHandler(Socket cl) {
    this.clientSocket = cl;
  }

  @Override
  public void run() {
    requestHandler();
  }

  public void printPortMessage(String message) {
    System.out.println(message + " | port: " + clientSocket.getPort());
  }

  public void requestHandler() {
    try {
      boolean headerCheck = true;
      int numOfBytes = 0;
      InputStream input = clientSocket.getInputStream();
      OutputStream output = clientSocket.getOutputStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(input));

      String requestLine = br.readLine();
      printPortMessage("Receiving Message Header : " + requestLine);
      if (requestLine == null) {
        throw new Exception("Null Request");
      }
      StringTokenizer tokens = new StringTokenizer(requestLine);

      if (!tokens.nextToken().toUpperCase().equals("GET")) {
        headerCheck = false;
      }
      if (headerCheck) {
        String fileName = tokens.nextToken();
        fileName = fileName.substring(1);
        try {
          numOfBytes = Integer.parseInt(fileName);
          if (!(numOfBytes >= 100 && numOfBytes <= 20000))
            throw new Exception();
        } catch (Exception ex) {
          headerCheck = false;
        }
      }
      PrintWriter out = new PrintWriter(output);
      if (headerCheck) {
        out.println("HTTP/1.0 200 OK");
        printPortMessage("Sending " + numOfBytes + " bytes");
        printHTML(out, numOfBytes);
        printPortMessage(numOfBytes + " bytes sent");
      } else {
        printPortMessage("Client gave a bad request. Bad Request response message is sent.");
        out.println("HTTP/1.0 400 Bad Request");
        out.println();
        out.print("Bad Request. Usage : server:port/[100..20000]");
      }

      out.close();
      output.close();
      br.close();
      input.close();

    } catch (IOException e) {
      // report exception somewhere.
      e.printStackTrace();
    } catch (Exception e) {
      printPortMessage("Error occured at connection : " + e.getMessage());
    }

    try {
      clientSocket.close();
      printPortMessage("Connection closed.");
    } catch (Exception ex) {
      printPortMessage("Error occured while closing the connection : " + ex.getMessage());
    }
  }

  private void printHTML(PrintWriter out, int numOfBytes) {
    String numOfBytesInString = String.valueOf(numOfBytes);
    out.println("Content-Type: text/html");
    out.println("Content-Length: " + numOfBytes);
    out.println();

    out.println("<HTML>");
    out.println("<HEAD>");
    out.println("<TITLE>I am " + numOfBytesInString + " bytes long</TITLE>");
    out.println("</HEAD>");
    out.print("<BODY>");
    for (int i = 0; i < numOfBytes - (80 + numOfBytesInString.length()); i++)
      out.print("a");

    out.println("</BODY>");
    out.print("</HTML>");
  }

  private boolean isModified(String fileName) {
    int num = Integer.parseInt(fileName);

    return (num % 2 == 1) ? true : false;
  }

}
