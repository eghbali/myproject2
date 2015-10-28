package project2;

import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by DotinSchool2 on 10/20/2015.
 */
public class JsonParser {

    int portvalue;
    String outLog;

    public JsonParser() {

    }

    public ArrayList<Deposit> readFile() throws IOException {
        ArrayList deposits = new ArrayList();
        JsonReader jsonReader = new JsonReader(new FileReader("core.json"));
        jsonReader.beginObject();
        jsonReader.nextName();
        portvalue = jsonReader.nextInt();
        jsonReader.nextName();
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            jsonReader.beginObject();
            jsonReader.nextName();
            String customer = jsonReader.nextString();
            jsonReader.nextName();
            String id = jsonReader.nextString();
            jsonReader.nextName();
            int initialBalance = jsonReader.nextInt();
            jsonReader.nextName();
            int upperBound = jsonReader.nextInt();
            jsonReader.endObject();
            deposits.add(new Deposit(customer, id, initialBalance, upperBound));
            //  Deposit deposit=new Deposit();
        }
        jsonReader.endArray();
        jsonReader.nextName();
        outLog = jsonReader.nextString();
        return deposits;
    }

    public static void main(String[] args) throws IOException {

        //System.out.println(x.deposits.get(1).customer);

    }

}






