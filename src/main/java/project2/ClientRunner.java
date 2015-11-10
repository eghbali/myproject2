package project2;

/**
 * Created by DotinSchool2 on 11/3/2015.
 */
public class ClientRunner {
    public static void main(String[] args) {

        GreetingClient client = new GreetingClient("input.xml");
        GreetingClient client2 = new GreetingClient("input2.xml");
        client.setName("client1");
        client2.setName("client2");
        client.start();
        //System.out.println("Client 1 has been started");
        client2.start();
        // System.out.println("Client 2 has been started");

    }
}
