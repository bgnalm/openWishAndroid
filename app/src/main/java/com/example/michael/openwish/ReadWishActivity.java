package com.example.michael.openwish;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


public class ReadWishActivity extends Activity {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_wish);
        this.loadUsername();
        String wishText = getWishText();
        TextView wishEditText = ((TextView)findViewById(R.id.wishText));
        wishEditText.setText(wishText);
    }

    public void loadUsername(){
        Intent intent = getIntent();
        this.username = intent.getStringExtra("user_name");
    }

    public void toast(CharSequence text){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }


    public String getWishText(){
        JSONObject response = OpenWishAPI.readWish(this.username);
        String wishTextForm = "";
        try {
            if (!response.getBoolean("success")){
                this.toast(response.getString("message"));
                return "error";
            }

            wishTextForm = response.getJSONObject("data").getString("text") + "\n";
            Iterator<String> keys = response.getJSONObject("data").getJSONObject("optional").keys();
            while(keys.hasNext()){
                String nextKey = keys.next();
                wishTextForm += nextKey + ": " + response.getJSONObject("data").getJSONObject("optional").get(nextKey).toString() + "\n";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return wishTextForm;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_read_wish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
