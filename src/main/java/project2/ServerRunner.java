package project2;

import com.sun.security.ntlm.Server;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerRunner {
    public static void main(String[] args) throws IOException {
//        Handler fileHandler = new FileHandler("%t/myClient.log");
//        Logger logFile;
//      .getLogger(GreetingServer.class.getName()).addHandler(fileHandler);
//
//                logger.log(Level.SEVERE,"KKK");
//        FileHandler handler = new FileHandler("myapp-log.%u.%g.txt",
//                true);
//        logger.info("hello");
        GreetingServer server = new GreetingServer();
        server.start();
    }
}



