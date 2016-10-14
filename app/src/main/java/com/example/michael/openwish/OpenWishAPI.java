package com.example.michael.openwish;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Michael on 11/10/2016.
 */
public class OpenWishAPI {

    public static final String BASE_URL = "http://10.20.109.96:5000";

    public static JSONObject addBug(String username, String bug){
        try {
            String url = BASE_URL + "/read_wish";
            JSONObject data = new JSONObject();
            data.put("user_name", username);
            data.put("bug", bug);
            return postJson(url, data);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static JSONObject getWish(String username, String wishID){
        try {
            String url = BASE_URL + "/read_wish";
            JSONObject data = new JSONObject();
            data.put("user_name", username);
            data.put("wish_id", wishID);
            return postJson(url, data);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static JSONObject getCreatedWishes(String username){
        try {
            String url = BASE_URL + "/get_wishes";
            JSONObject data = new JSONObject();
            data.put("user_name", username);
            data.put("created_wishes", true);
            return postJson(url, data);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static JSONObject getReadWishes(String username){
        try {
            String url = BASE_URL + "/get_wishes";
            JSONObject data = new JSONObject();
            data.put("user_name", username);
            data.put("read_wishes", true);
            return postJson(url, data);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static JSONObject readWish(String username){
        try {
            String url = BASE_URL + "/read_wish";
            JSONObject data = new JSONObject();
            data.put("user_name", username);
            return postJson(url, data);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static JSONObject rateWish(String username, String wishID, float rating){
        try {
            String url = BASE_URL + "/rate_wish";
            JSONObject data = new JSONObject();
            data.put("user_name", username);
            data.put("wish_id", wishID);
            data.put("rating", rating);
            return postJson(url, data);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static JSONObject addWish(String username, String text, JSONObject optional){
        try {
            String url = BASE_URL + "/add_wish";
            JSONObject data = new JSONObject();
            JSONObject wish = new JSONObject();
            data.put("user_name", username);
            wish.put("text",text);
            wish.put("optional", optional);
            data.put("wish", wish);
            return postJson(url, data);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static JSONObject createUser(String username){
        try {
            String url = BASE_URL + "/create_user";
            JSONObject data = new JSONObject();
            data.put("user_name", username);
            return postJson(url, data);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static JSONObject time_left(String username){
        try {
            String url = BASE_URL + "/time_left";
            JSONObject data = new JSONObject();
            data.put("user_name", username);
            return postJson(url, data);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static JSONObject postJson(String url, JSONObject data){
        try {
            String jsonData = data.toString();
            Log.d("openWish", "sending " + jsonData + " to " + url.toString());
            int TIMEOUT_MILLISEC = 10000;  // = 10 seconds

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpClient client = new DefaultHttpClient(httpParams);

            HttpPost request = new HttpPost(url);
            request.addHeader("content-type", "application/json");
            request.setEntity(new ByteArrayEntity(
                    jsonData.getBytes("UTF8")));
            HttpResponse response = client.execute(request);
            HttpEntity responseEntity = response.getEntity();
            InputStream instream = responseEntity.getContent();
            String result = convertStreamToString(instream);
            instream.close();
            Log.d("OpenWish", "got : " + result );
            return new JSONObject(result);
        }
        catch (Exception e){
            //Log.e("openWish", e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }



}
