package com.example.michael.openwish;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class AddWishActivity extends Activity {

    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wish);
        this.loadUsername();
        EditText mEditText = (EditText) findViewById(R.id.wish_text);
        //mEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);

        if (!this.canPost()){
                Log.d("OpenWish", "User cant post");
                this.onBackPressed();
         }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_wish, menu);
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

    public void toast(CharSequence text){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }

    public void loadUsername(){
        Intent intent = getIntent();
        this.username = intent.getStringExtra("user_name");
    }

    public boolean canPost(){
        try {
            JSONObject response = OpenWishAPI.time_left(this.username);
            if (!response.getBoolean("success")) {
                this.toast("time_left failed: " + response.getString("message"));
                return false;
            }

            else{
                if (response.getJSONObject("data").getBoolean("can_post")){
                    return true;
                }

                else{
                    this.toast("Can't post until: "+ response.getJSONObject("data").getString("next_post"));
                    return false;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void sendButtonPressed(View view){
        String wish_text = ((EditText)findViewById(R.id.wish_text)).getText().toString();
        String type = ((Spinner)findViewById(R.id.type_spinnper)).getSelectedItem().toString();
        boolean has_type = !type.equals("Type...");

        String location = ((EditText)findViewById(R.id.location)).getText().toString();
        boolean has_location = !location.equals("Location..");

        String metadataField = ((EditText)findViewById(R.id.extra_field)).getText().toString();
        String metadataValue = ((EditText)findViewById(R.id.extra_data)).getText().toString();
        boolean has_extra = !metadataField.equals("Field Name");

        JSONObject optional = new JSONObject();
        try {
            if (has_type){
                optional.put("type", type);
            }
            if (has_location){
                optional.put("location", location);
            }
            if (has_extra){
                optional.put(metadataField, metadataValue);
            }

            JSONObject response = OpenWishAPI.addWish(this.username, wish_text, optional);
            this.toast(response.getString("message"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.startReadWishActivity();
    }

    public void startReadWishActivity(){
        Intent intent = new Intent(this, ReadWishActivity.class);;
        intent.putExtra("user_name", this.username);
        startActivity(intent);
    }
}
