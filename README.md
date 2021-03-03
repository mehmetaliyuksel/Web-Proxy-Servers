# Web-Proxy-Servers
 Multi-Threaded Web &amp; Proxy Server

Project consists of 2 servers;

Web Server<br/>
  -Server program takes single argument which specifies the port number.<br/>
  -Server should work when connected via a web browser (such asGoogle Chrome).<br/>
    For example, if the server port number is 8080, http://localhost:8080/500
    would be a valid URL if the server runs in the same host.<br/>
  -Server returns an HTML document according to the requested URI. The size of
    the document is determined by the requested URI (any size between 100 and 20,000 bytes).<br/>
  -Server sends back an HTTP response line with 400 status code that indicates an error if the requested<br/>
    URI is not a number, or is less than 100, or is greater than 20,000.<br/>
    As an example, the following document is 100 bytes long:<br/>
![image](https://user-images.githubusercontent.com/37842979/109806359-aba8a800-7c35-11eb-8649-889755fcbcf8.png)

