package netgloo.http;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ThaZalman on 04/11/2016.
 * from https://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
 */
public class HttpURLConnectionExample {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String ORIGIN = "http://www.google.fr";

    public static void main(String[] args) throws Exception {

        HttpURLConnectionExample http = new HttpURLConnectionExample();

        System.out.println("Testing 1 - Send Http GET request");

        String url = "https://maps.googleapis.com/maps/api/place/details/json",
                parameters = "";

        HashMap<String, String> params = new HashMap<>();
        params.put("key", "AIzaSyDOoBhiWusApq1Od-vMIRZrnRO-G2GB62A");
        params.put("reference", "CmRSAAAAhyBS5z972gUNrFhQUWuNzmKTfgTu_0ZI0Ijq15DhK2edQwvuS5sHCt1bH0jrFhpRLMYyX8EHrC1krxjnrsLE9LRLf5tmXu7kjauVSJr2RqjIEunMMgqUyOa7jTCvuKCTEhCIhNIQWyKKzzjXnLIVHkvvGhRdRF9-0ZaNdEYq4xHPFnU-JFR6Mg");

        parameters = prepareRequestParams(params, "UTF-8");

        JSONObject response = http.SendGetJson(url, parameters);
    }

    /**
     * Formats parameters from HashMap to url parameters
     * @param params map that contains parameters to format
     * @param charset charset to use for formatting
     * @return url parameter string like "param1=value1&param2=value2" if @param("params") as a length of 2
     */
    public static String prepareRequestParams(HashMap<String, String> params, String charset)
    {
        if(params == null || params.size() == 0)
            return null;

        String query = "";
        boolean isFirst = true;

        try {
            Iterator it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                String key = pair.getKey().toString(),
                        value = pair.getValue().toString(),
                        param = null;

                param = URLEncoder.encode(key, charset) + "=" + URLEncoder.encode(value, charset);

                if(isFirst)
                    isFirst = false;
                else
                    param = "&" + param;

                query += param;

                it.remove(); // avoids a ConcurrentModificationException
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            query = null;
        }
        return query;
    }

    /**
     * see @SendGet
     * @param url @SendGet
     * @param parameters @SendGet
     * @return parsed response into JSONObject
     * @throws Exception
     */
    public static JSONObject SendGetJson(String url, String parameters) throws Exception {
        String response = SendGet(url, parameters);
        return new JSONObject(response);
    }

    /**
     * Send a Http GET request
     * @param url endpoint to reach
     * @param parameters url parameters to pass
     * @return
     * @throws Exception
     */
    public static String SendGet(String url, String parameters) throws Exception {
        parameters = parameters != null && !parameters.isEmpty()
                ? parameters
                : "";

        url = url + '?' + parameters;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Origin", ORIGIN);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}
