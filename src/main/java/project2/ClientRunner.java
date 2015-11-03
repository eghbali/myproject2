package project2;

/**
 * Created by DotinSchool2 on 11/3/2015.
 */
public class ClientRunner {
    public static void main(String[] args) {
        GreetingClient client = new GreetingClient(1234, "input.xml");
        GreetingClient client2 = new GreetingClient(1234, "input2.xml");
        client.start();
        //System.out.println("Client 1 has been started");
       client2.start();
       // System.out.println("Client 2 has been started");

    }
}
