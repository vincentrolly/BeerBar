package netgloo.services;

import netgloo.http.HttpURLConnectionExample;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by ThaZalman on 04/11/2016.
 */
public class GooglePlacesService {

    /**
     * Main for tests (run file directly)
     * @param args nothing
     */
    public static void main(String[] args){
        String search = "Le Robinson",
//                city = "43000"
                city = "";
//                city = "Le puy en velay";

        JSONObject resp = TextSearch(search, city);
        if(resp == null)
        {
            System.out.println("error parsing json");
            return;
        }

        JSONArray results = resp.getJSONArray("results");
        if(results == null)
        {
            System.out.println("error parsing array");
            return;
        }
        if(results.length() == 0)
        {
            System.out.println("no result");
            return;
        }

        int legth = results.length();
        System.out.println("Got " + legth + " results.");

        for(int i = 0; i < legth; ++i) {
            JSONObject first = (JSONObject) (results.get(i));
            System.out.println(first.getString("name"));
        }
    }


    /**
     * Calls Google Places API's text search for type 'bar'
     * @param text the text (bar name) to search for
     * @param city the city to search in (works weird)
     * @return a JSONObject containing the response from the api
     */
    public static JSONObject TextSearch(String text, String city)
    {
        String textSearch = city == null || city.length() == 0
                ? text
                : text + ", " + city;
//        textSearch = textSearch.replaceAll(" ", "+");

        HttpURLConnectionExample http = new HttpURLConnectionExample();

        System.out.println("Send Http GET request");
//        String url = "https://maps.googleapis.com/maps/api/place/details/json",
        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json",
                parameters = "";

        HashMap<String, String> params = new HashMap<>();
        params.put("key", "AIzaSyDOoBhiWusApq1Od-vMIRZrnRO-G2GB62A");
        params.put("query", textSearch);
        params.put("types", "bar");

        parameters = HttpURLConnectionExample.prepareRequestParams(params, "UTF-8");

//        System.out.println(parameters);
        try {
            return http.SendGetJson(url, parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
