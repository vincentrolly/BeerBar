package netgloo;

import org.json.JSONObject;


/**
 * Created by ThaZalman on 04/11/2016.
 */
public class JsonExample {
    public static void main(String[] args) {
        JSONObject obj = new JSONObject("{" +
                "\"long_name\": \"10\"," +
                "\"short_name\": \"10\"," +
                "\"types\": [" +
                "  \"street_number\"" +
                "]" +
                "}");

        System.out.println(obj.getJSONArray("types")); //John;
    }
}
