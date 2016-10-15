package com.example.michael.openwish;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


public class GetWishActivity extends Activity {

    String username;
    String wish_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_wish);
        this.loadData();
        Log.d("openWish", "Started GetWish with user " + this.username + " and wish " + this.wish_id);
        String wishText = getWishText();
        TextView wishEditText = ((TextView)findViewById(R.id.wishText));
        wishEditText.setText(wishText);
    }

    public void loadData(){
        Intent intent = getIntent();
        this.username = intent.getStringExtra("user_name");
        this.wish_id = intent.getStringExtra("wish_id");
    }

    public void toast(CharSequence text){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }


    public String getWishText(){
        JSONObject response = OpenWishAPI.getWish(this.username, this.wish_id);
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
