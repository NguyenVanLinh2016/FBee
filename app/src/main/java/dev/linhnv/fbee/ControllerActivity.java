package dev.linhnv.fbee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import dev.linhnv.fbee.model.HttpHandler;

import static android.content.ContentValues.TAG;

public class ControllerActivity extends AppCompatActivity {

    //url doc du lieu tu table user de lay userId
    String token;
    String urlUser;
    int userId;
    private ProgressDialog progressDialog;
    //check user da nhan qua chua
    String urlCheckUserGift;
    String messageCheckGift;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        //get id user
        SharedPreferences sharedPreferences = this.getSharedPreferences("token", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        urlUser =  "http://f-bee.nks.com.vn/api/auth/me?token=" + token;
        new GetUserId().execute();
    }
    //getUserID
    class GetUserId extends AsyncTask<Void, Void, Void> {
        /*@Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }*/
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlUser);
            Log.e(TAG, "Response from url: " + jsonStr);
            if(jsonStr != null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    //Getting Json user
                    JSONObject profile = jsonObject.getJSONObject("user");
                    userId = profile.getInt("id");
                    Log.e(TAG, "UserId: " + userId);

                } catch (JSONException ex) {
                    Log.e(TAG, "Json parsing error: " + ex.getMessage());
                }
            }else{
                Log.e(TAG, "Couldn't get json from server.");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Dismiss the progress dialog
            /*if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }*/
            //check user da nhan qua chua
            urlCheckUserGift = "http://f-bee.nks.com.vn/api/usergift/check?token=" +token +"&id="+ userId;
            new CheckUserAlreadyGift().execute();
        }
    }
    //check user da nhan qua chua
    class CheckUserAlreadyGift extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ControllerActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlCheckUserGift);
            Log.e(TAG, "Response from url: " + jsonStr);
            if(jsonStr != null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    //Getting Json user
                    JSONObject checkUserGift = jsonObject.getJSONObject("json");
                    messageCheckGift = checkUserGift.getString("Message");
                    //Log.e(TAG, "Message: " + messageCheckGift);

                } catch (JSONException ex) {
                    Log.e(TAG, "Json parsing error: " + ex.getMessage());
                }
            }else{
                Log.e(TAG, "Couldn't get json from server.");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Dismiss the progress dialog
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            //Neu tra ve la Exits thi user da nhan qua roi
            if(messageCheckGift.equalsIgnoreCase("Exits")){
                startActivity(new Intent(ControllerActivity.this, MainActivity.class));
                finish();
            }else{ //user chua nhan qua thi chuyen qua activity quay qua
                Intent intent = new Intent(ControllerActivity.this, GiftActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("userId", userId);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }
    }
}
