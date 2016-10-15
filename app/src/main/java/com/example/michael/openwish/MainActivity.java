package com.example.michael.openwish;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.UUID;


public class MainActivity extends Activity{

    String user_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        createVerifierStrings();




    }

    public void addWishButtonClick(View view){
        Intent intent = new Intent(this, AddWishActivity.class);;
        intent.putExtra("user_name", this.user_name);
        startActivity(intent);
    }

    public void readWishesButtonClick(View view){
        Intent intent = new Intent(this, ReadWishesAtivity.class);;
        intent.putExtra("user_name", this.user_name);
        startActivity(intent);
    }

    public void createdWishesButtonClick(View view){

    }

    public void bugButtonClick(View view){

    }

    private void createVerifierStrings() {
        SharedPreferences prefs = this.getSharedPreferences("Someprefstringreference", 0);
        String not_set = "NOTSET";
        String android_key;
        android_key = prefs.getString("id", not_set);

        if (android_key.equals(not_set)) {
            Log.d("openWish", "Creating keys for 1st time");
            android_key = UUID.randomUUID().toString();
            Log.d("openWish", "User name is "+ android_key);
            OpenWishAPI.createUser(android_key);
            prefs.edit().putString("id", android_key).commit();
        }

        this.user_name = prefs.getString("id", not_set);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
