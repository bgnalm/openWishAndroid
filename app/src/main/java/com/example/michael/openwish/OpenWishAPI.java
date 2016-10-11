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

    public static void createUser(String username){
        
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
