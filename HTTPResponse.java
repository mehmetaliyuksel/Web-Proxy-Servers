import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class HTTPResponse {
    ArrayList<String> lines = new ArrayList<>();

    HTTPResponse(BufferedReader reader) throws Exception {
        String line;
        // In constructor we read the response from web server or cached file line by
        // line
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
    }

    // Sends a regular response from web server to the given output stream which is
    // client's output stream or a file's output stream (for caching) in our case
    void send(DataOutputStream stream) throws Exception {
        for (String line : lines) {
            System.out.println("RES > " + line);
            stream.writeBytes(line + "\r\n");
            stream.flush();
        }
        stream.writeBytes("\r\n");
        stream.flush();
    }

    // NOT FOUND response message in HTML format is written to the given PrintWriter
    // object
    static void sendNotFound(PrintWriter out) {

        out.println("HTTP/1.0 400 Bad Request");
        out.println("Content-Type: text/html");
        out.println();
        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<TITLE>NOT FOUND</TITLE>");
        out.println("</HEAD>");
        out.print("<BODY>");
        out.print("NOT FOUND");
        out.println("</BODY>");
        out.print("</HTML>");
        out.close();
    }

    // Req-Too Long response message in HTML format is written to the given
    // PrintWriter object
    static void sendTooLong(PrintWriter out) {

        out.println("HTTP/1.0 414 Bad Request");
        out.println("Content-Type: text/html");
        out.println();
        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<TITLE>Request-URI Too Long</TITLE>");
        out.println("</HEAD>");
        out.print("<BODY>");
        out.print("Request-URI Too Long");
        out.println("</BODY>");
        out.print("</HTML>");
        out.close();
    }
}