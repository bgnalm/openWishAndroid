package com.example.michael.openwish;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ReadWishesAtivity extends Activity implements View.OnClickListener {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_wishes_activity);
        this.loadUsername();
        this.loadWishes();

    }

    public void loadUsername(){
        Intent intent = getIntent();
        this.username = intent.getStringExtra("user_name");
    }

    public void loadWishes(){
        JSONObject response = OpenWishAPI.getReadWishes(this.username);
        try {
            JSONArray wishes = response.getJSONObject("data").getJSONArray("read_wishes");
            for (int i=0; i<wishes.length(); i++){
                String wish_id = wishes.getString(i);
                this.addEntryToScroll(wish_id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addEntryToScroll(String s){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_read_wishes_activity, null);
        LinearLayout layout = ((LinearLayout)findViewById(R.id.entries));
        TextView tv = new TextView(this);
        tv.setText(s);
        tv.setPadding(15, 10, 15, 10);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        params.setMargins(0,5,0,5);
        tv.setLayoutParams(params);
        tv.setBackgroundColor(Color.WHITE);
        tv.setClickable(true);
        tv.setOnClickListener(this);
        layout.addView(tv);
        layout.invalidate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_read_wishes_activity, menu);
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

    @Override
    public void onClick(View v) {
        TextView clickedText = ((TextView) v);
        Log.d("openWish", "Clicked on "+ clickedText.getText());
        this.startGetWishActivity(clickedText.getText().toString());
    }

    public void startGetWishActivity(String wish_id){
        Intent intent = new Intent(this, GetWishActivity.class);;
        intent.putExtra("user_name", this.username);
        intent.putExtra("wish_id", wish_id);
        startActivity(intent);
    }

}
