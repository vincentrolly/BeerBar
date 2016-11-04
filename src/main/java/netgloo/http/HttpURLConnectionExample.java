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

    private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {

        HttpURLConnectionExample http = new HttpURLConnectionExample();

        System.out.println("Testing 1 - Send Http GET request");
//        String url = "http://www.google.com/search?q=mkyong",
//        String url = "https://maps.googleapis.com/maps/api/place/details/json?key=AIzaSyDOoBhiWusApq1Od-vMIRZrnRO-G2GB62A&reference=CmRSAAAAhyBS5z972gUNrFhQUWuNzmKTfgTu_0ZI0Ijq15DhK2edQwvuS5sHCt1bH0jrFhpRLMYyX8EHrC1krxjnrsLE9LRLf5tmXu7kjauVSJr2RqjIEunMMgqUyOa7jTCvuKCTEhCIhNIQWyKKzzjXnLIVHkvvGhRdRF9-0ZaNdEYq4xHPFnU-JFR6Mg",
        String url = "https://maps.googleapis.com/maps/api/place/details/json",
                parameters = "";

        HashMap<String, String> params = new HashMap<>();
        params.put("key", "AIzaSyDOoBhiWusApq1Od-vMIRZrnRO-G2GB62A");
        params.put("reference", "CmRSAAAAhyBS5z972gUNrFhQUWuNzmKTfgTu_0ZI0Ijq15DhK2edQwvuS5sHCt1bH0jrFhpRLMYyX8EHrC1krxjnrsLE9LRLf5tmXu7kjauVSJr2RqjIEunMMgqUyOa7jTCvuKCTEhCIhNIQWyKKzzjXnLIVHkvvGhRdRF9-0ZaNdEYq4xHPFnU-JFR6Mg");

        parameters = prepareRequestParams(params, "UTF-8");

//        System.out.println(parameters);
        JSONObject response = http.SendGetJson(url, parameters);

//        System.out.println("\nTesting 2 - Send Http POST request");
//        http.sendPost();

    }

    /**
     * Formats parameters from HashMap to url parameters
     * @param params map that contains parameters to format
     * @param charset charset to use for formatting
     * @return url parameter string like "param1=value1&param2=value2" if @param("params") as a length of 2
     */
    public static String prepareRequestParams(HashMap<String, String> params, String charset)
    {
        // TODO use Map<String, String>

        if(params == null || params.size() == 0)
            return null;

        String query = "";
        boolean isFirst = true;

        try {
            Iterator it = params.entrySet().iterator();
            while (it.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry)it.next();
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
            /*
            query = String.format("param1=%s&param2=%s",
                         URLEncoder.encode(param1, charset),
                         URLEncoder.encode(param2, charset));
            */

        return query;
    }

    /**
     * see @SendGet
     * @param url @SendGet
     * @param parameters @SendGet
     * @return parsed response into JSONObject
     * @throws Exception
     */
    public JSONObject SendGetJson(String url, String parameters) throws Exception {
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
    public String SendGet(String url, String parameters) throws Exception {
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

        //print result
//        System.out.println(response.toString());
        return response.toString();

    }

    // HTTP POST request
//    private void sendPost() throws Exception {
//
//        String url = "https://selfsolve.apple.com/wcResults.do";
//        URL obj = new URL(url);
//        HttpsURLConnection con;
//        con = (HttpsURLConnection) obj.openConnection();
//
//        //add reuqest header
//        con.setRequestMethod("POST");
//        con.setRequestProperty("User-Agent", USER_AGENT);
//        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//
//        String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
//
//        // Send post request
//        con.setDoOutput(true);
//        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//        wr.writeBytes(urlParameters);
//        wr.flush();
//        wr.close();
//
//        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'POST' request to URL : " + url);
//        System.out.println("Post parameters : " + urlParameters);
//        System.out.println("Response Code : " + responseCode);
//
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuffer response = new StringBuffer();
//
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();
//
//        //print result
//        System.out.println(response.toString());
//
//    }

}
