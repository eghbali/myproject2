package project2;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

public class ServerRunner {
    public static ArrayList<Deposit> deposits;
    int port;
    ArrayList<Thread> threads = new ArrayList();
    private ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        ServerRunner server = new ServerRunner();
        server.createConnection();

    }

    public void createConnection() throws IOException {
        JsonParser jsonparser = new JsonParser();
        deposits = jsonparser.readFile();
        port = jsonparser.portvalue;
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);

        while (true) {

            try {

                System.out.println("waiting for connection!");
                Socket server;
                server = serverSocket.accept();
                System.out.println("connect!");
                Thread service = new Service(deposits, server);
                threads.add(service);
                service.start();
                //  Thread x=(Service)threads.get(1);


            } catch (SocketTimeoutException s) {

                while (!threads.isEmpty()) {
                    for (int i = 0; i < threads.size(); i++) {
                        if (threads.get(i).getState() == Thread.State.TERMINATED) {
                            threads.remove(i);
                        }
                    }
                }
                for (int i = 0; i < deposits.size(); i++) {
                    System.out.println("**********i**************");
                    System.out.println("customer:" + deposits.get(i).customer);
                    System.out.println("initial Balance:" + deposits.get(i).initialBalance);


                }
                System.out.println("Socket timeout");
                break;

            } catch (IOException e) {
                e.printStackTrace();

            }
        }


    }

}


