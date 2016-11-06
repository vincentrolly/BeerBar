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

    private static boolean TestBoth()
    {
        String search = "Bar le robinson",
                city = "";

        // appel de l'api places et recuperation de la reponse json
        JSONObject resp = TextSearch(search, city);
        if(resp == null)
        {
            System.out.println("error parsing json");
            return false;
        }

        // recuperer la reference a partir du json
        String reference = getBarReferenceFromJsonText(resp);
        if(reference == null)
        {
            System.out.println("error reference");
            return false;
        }

        // interroger api_places details avec la reference et recuperation objet JSON
        JSONObject respdet = Details(reference);
        if(respdet == null)
        {
            System.out.println("error details");
            return false;
        }

        // on cherche le nom du bar dans l'objet JSON de reponse
        String nombar = getBarNameFromJsonDetails(respdet);
        if(nombar == null)
        {
            System.out.println("error nom bar");
            return false;
        }
        //  on ecrit le nom du bar
        System.out.println("\t" + nombar);

        return true;
    }

    /**
     * Get details for the bar found in the API places GOOGLE
     * @param resp jsonobject response
     * @return
     */
    private static String getBarNameFromJsonDetails(JSONObject resp)
    {
        if(resp == null)
        {
            System.out.println("error parsing json");
            return null;
        }

        try {
            JSONObject result = resp.getJSONObject("result");
            if (result == null) {
                System.out.println("error parsing array");
                return null;
            }
            String name = result.getString("name");
            System.out.println("\t" + name);
            return name;
        }
        catch(Exception e)
        {
            System.out.println("\t" + "getBarFromJsonDetails error json");
            System.out.println("\t" + e.getMessage());
            return null;
        }
    }

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
                city = "";

        // call to api_places with the bar in search variable
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
     * Get reference from bar
     * @param resp JSON Object representing the bar from API Places
     * @return Reference for the bar represented by the JsonObject
     */
    public static String getBarReferenceFromJsonText(JSONObject resp)
    {
        if(resp == null)
        {
            System.out.println("error parsing json");
            return null;
        }

        try {
            JSONArray results = resp.getJSONArray("results");
            if (results == null) {
                System.out.println("error parsing array");
                return null;
            }
            if (results.length() == 0) {
                System.out.println("no result");
                return null;
            }

            int length = results.length();
            System.out.println("Got " + length + " results.");

            JSONObject first = (JSONObject) (results.get(0));
            System.out.println("\t" + first.getString("reference"));

            return first.getString("reference");
        }
        catch(Exception e)
        {
            System.out.println("\t" + "getBarFromJsonDetails error json");
            System.out.println("\t" + e.getMessage());
            return null;
        }
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


        success = TestBoth()
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

        HttpURLConnectionExample http = new HttpURLConnectionExample();

        System.out.println("Send Http GET request");
        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json",
                parameters = "";

        HashMap<String, String> params = new HashMap<>();
        params.put("key", AIP_KOU);
        params.put("query", textSearch);
        params.put("types", "bar");

        parameters = HttpURLConnectionExample.prepareRequestParams(params, "UTF-8");

        try {
            return http.SendGetJson(url, parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject Details(String reference)
    {
        System.out.println("Send Http GET request");

        String url = "https://maps.googleapis.com/maps/api/place/details/json",
                parameters = "";

        HashMap<String, String> params = new HashMap<>();
        params.put("key", AIP_KOU);
        params.put("reference", reference);

        parameters = HttpURLConnectionExample.prepareRequestParams(params, "UTF-8");

        try {
            return HttpURLConnectionExample.SendGetJson(url, parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
