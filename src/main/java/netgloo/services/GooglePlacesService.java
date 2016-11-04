package netgloo.services;

import netgloo.http.HttpURLConnectionExample;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by ThaZalman on 04/11/2016.
 */
public class GooglePlacesService {

    private static final String AIP_KOU = "AIzaSyDOoBhiWusApq1Od-vMIRZrnRO-G2GB62A";

    private static boolean TestDetails()
    {
        String reference = "CmRSAAAASHdPtI2UHoiIN47g0aRBi3b0wS3Y2_AcBizZXmeLjXQRM2EXmodOM-qKbl6VRP7D985RNIfJk7hqEN9Ursapma6P8ImncfoHFt0euhGKc9OvJ3keFW3ckYDYLh-eY-fcEhD0Wms6Th1m9W0hpKe9E9v7GhQkwEO8Krha-rB3wQpIBnhXeoxP4w";

        JSONObject resp = Details(reference);
        if(resp == null)
        {
            System.out.println("error parsing json");
            return false;
        }

        JSONObject result = resp.getJSONObject("result");
        if(result == null)
        {
            System.out.println("error parsing array");
            return false;
        }
        System.out.println("\t" + result.getString("name"));

        return true;
    }

    private static boolean TestTextSeachr()
    {
        String search = "Bar le robinson",
//                city = "43000"
                city = "";
//                city = "Le puy en velay";

        JSONObject resp = TextSearch(search, city);
        if(resp == null)
        {
            System.out.println("error parsing json");
            return false;
        }

        JSONArray results = resp.getJSONArray("results");
        if(results == null)
        {
            System.out.println("error parsing array");
            return false;
        }
        if(results.length() == 0)
        {
            System.out.println("no result");
            return false;
        }

        int legth = results.length();
        System.out.println("Got " + legth + " results.");

        for(int i = 0; i < legth; ++i) {
            JSONObject first = (JSONObject) (results.get(i));
            System.out.println("\t" + first.getString("name"));
        }

        return true;
    }

    /**
     * Main for tests (run file directly)
     * @param args nothing
     */
    public static void main(String[] args){
        String success = TestTextSeachr()
                ? "succeeded"
                : "failed";
        System.out.println("TestTextSeachr " + success);


        success = TestDetails()
                ? "succeeded"
                : "failed";
        System.out.println("TestDetails " + success);

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
        params.put("key", AIP_KOU);
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

    public static JSONObject Details(String reference)
    {
        //HttpURLConnectionExample http = new HttpURLConnectionExample();

        System.out.println("Send Http GET request");
//        String url = "https://maps.googleapis.com/maps/api/place/details/json",
        String url = "https://maps.googleapis.com/maps/api/place/details/json",
                parameters = "";

        HashMap<String, String> params = new HashMap<>();
        params.put("key", AIP_KOU);
        params.put("reference", reference);

        parameters = HttpURLConnectionExample.prepareRequestParams(params, "UTF-8");

//        System.out.println(parameters);
        try {
            return HttpURLConnectionExample.SendGetJson(url, parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
