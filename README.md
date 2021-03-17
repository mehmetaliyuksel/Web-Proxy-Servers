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
![image](https://user-images.githubusercontent.com/37842979/109806359-aba8a800-7c35-11eb-8649-889755fcbcf8.png)<br/><br/>
Proxy Server<br/> -Proxy server uses port number 8888.<br/>
-Proxy server only directs the requests to the web server, it doesn't direct any request to another web server.<br/>
-If the requested file size is gerater than 9999 (in other words, if the URI is greater than 9,999) it would not pass the request to the web server. 
Rather it sends “Request-URI Too Long” message. <br/>
-If the Web Server is not running currently, proxy server would return a “Not Found” 
error message.<br/>
-The proxy server should work when connected via a browser after configuring the proxy 
settings of your browser. <br/>
-Proxy also provides caching. The proxy server will cache all the 
requested objects in the file system and if the objects are requested again, it will not 
forward these requests to the origin server instead returns back the cached file.

