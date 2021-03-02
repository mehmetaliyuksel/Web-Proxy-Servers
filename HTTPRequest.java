import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

import java.util.HashMap;

class HTTPRequest {
    int port;
    String method = "GET";
    String path = "/";
    String version = "HTTP/1.0";
    boolean isServerRunning = true;
    int checkLength;
    final String cacheFolderPATH = "cache";
    String cacheFileName;
    HashMap<String, String> headers = new HashMap<>();
    // Used for getResponse()
    Socket socket;
    DataOutputStream outputStream;
    BufferedReader reader;
    PrintWriter out;

    HTTPRequest(BufferedReader reader) throws Exception {
        // In constructor we split the lines taken from reader object and put them in
        // variables for further use
        String reqLine = reader.readLine();
        if (reqLine == null) {
            throw new Exception("Invalid request!");
        }

        String[] split = reqLine.split(" ");
        method = split[0];
        path = split[1];
        version = split[2];
        System.out.println(method + " " + path + " " + version);

        try {// we define variables to be used
            URL url = new URL(path);
            port = url.getPort();
            path = url.getPath();
            checkLength = Integer.parseInt(path.substring(1));
            cacheFileName = path.substring(1) + ".cache";
            headers.put("Host", url.getHost());
            System.out.println(url + " " + path + "--------");
        } catch (Exception e) {
            // Do nothing
        }
    }

    // In this method we check cache folder if we have the requested file or not
    private boolean checkCache() {
        File folder = new File(cacheFolderPATH);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && listOfFiles[i].getName().equals(cacheFileName)) {
                return true;
            }

        }
        return false;
    }

    HTTPResponse getResponse() throws Exception {

        // 1. Get host address
        InetAddress address = InetAddress.getByName((headers.get("Host")));
        boolean isCached = checkCache(); // checks if requested file is cached or not
        if (!isCached) {
            try {
                // if requsted file is not cached connects to web server and directs the request
                socket = new Socket(address, port); // creates a socket to communicate with web server
                outputStream = new DataOutputStream(socket.getOutputStream());
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(outputStream);
            } catch (Exception e) {

            } // If web server is not open so that we cannot connect,
              // NOT FOUND info is responded to client
            if (socket == null || !socket.isConnected()) {
                // System.out.println("NOT FOUND");
                isServerRunning = false;
                return null;
            }

            // 2. Send request
            write(method + " " + path + " " + version); // sends request to web server
            headers.put("If-Modified", "0");
            headers.put("Accept-Encoding", "identity");
            headers.put("Connection", "close");
            for (String key : headers.keySet()) {
                write(key + ": " + headers.get(key));
            }

            write("");

            // 3. Read response
            HTTPResponse response = new HTTPResponse(reader);
            // caches the new file
            File fileToCache = new File(cacheFolderPATH + "\\" + cacheFileName);
            FileOutputStream fileOutStream = new FileOutputStream(fileToCache);
            DataOutputStream outData = new DataOutputStream(fileOutStream);
            response.send(outData);

            return response;

        } else {
            // Requsted file is in cache so we return the content of cached file as response
            File cachedFile = new File(cacheFolderPATH + "\\" + cacheFileName);
            InputStream fileStream = new FileInputStream(cachedFile);
            BufferedReader cachedFileReader = new BufferedReader(new InputStreamReader(fileStream));
            HTTPResponse cachedResponse = new HTTPResponse(cachedFileReader);
            return cachedResponse;

        }

    }

    // This method simply writes a line to output stream of socket which is
    // connected to web server
    private void write(String line) throws Exception {
        System.out.println("REQ > " + line);
        out.println(line + "\r\n");
        out.flush();
    }

    @Override
    public String toString() {
        return "[REQUEST] " + "Method: " + method + " " + "Path: " + path + " " + "Version: " + version;
    }
}
