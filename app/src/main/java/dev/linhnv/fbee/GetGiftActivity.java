package dev.linhnv.fbee;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import dev.linhnv.fbee.fragments.GiftFragment;
import dev.linhnv.fbee.model.HttpHandler;

import static android.content.ContentValues.TAG;

public class GetGiftActivity extends AppCompatActivity implements View.OnClickListener{
    //url doc du lieu tu table user de lay userId
    String urlUser;

    int idGift;
    int userId;
    ImageView img_get_gift;
    private Toolbar toolbar;
    TextView tv_info_gift;
    String token;
    private ProgressDialog progressDialog;
    String bonus;
    EditText edt_idConfirm;
    Button btn_idConfirm;
    int promoter;
    //url check promoter
    String urlCheckPromoter;
    String messageCheckPromoter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_gift);
        edt_idConfirm = (EditText) findViewById(R.id.edt_idConfirm);
        btn_idConfirm = (Button) findViewById(R.id.btn_idConfirm);
        btn_idConfirm.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.get_gift));
        }
        img_get_gift = (ImageView) findViewById(R.id.img_get_gift);
        tv_info_gift = (TextView) findViewById(R.id.tv_info_gift);
        userId = getIntent().getExtras().getInt("userId");
        idGift = getIntent().getExtras().getInt("idGift");
        bonus = getIntent().getExtras().getString("bonus");
        switch (idGift){
            case 1:
                img_get_gift.setImageResource(R.drawable.balo);
                tv_info_gift.setText("Bạn đã trúng một " +bonus+".\n Vui lòng liên hệ tư vấn viên để nhận quà.");
                break;
            case 2:
                img_get_gift.setImageResource(R.drawable.tuirut);
                tv_info_gift.setText("Bạn đã trúng một " +bonus+".\n Vui lòng liên hệ tư vấn viên để nhận quà.");
                break;
            case 3:
                img_get_gift.setImageResource(R.drawable.sach);
                tv_info_gift.setText("Bạn đã trúng một " +bonus+ ".\n Vui lòng liên hệ tư vấn viên để nhận quà.");
                break;
            case 4:
                img_get_gift.setImageResource(R.drawable.viet);
                tv_info_gift.setText("Bạn đã trúng một " +bonus+ ".\n Vui lòng liên hệ tư vấn viên để nhận quà.");
                break;
        }

        SharedPreferences pre = getSharedPreferences("token", MODE_PRIVATE);
        token = pre.getString("token", "");

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_get_gift));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(GetGiftActivity.this, MainActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        //lay gia tri ma promoter nhap vao va kiem tra promoter
        promoter = Integer.parseInt(edt_idConfirm.getText().toString());
        urlCheckPromoter = "http://f-bee.nks.com.vn/api/auth/check?token="+ token +"&id="+ promoter;
        new SendIdPromoter().execute();
    }

    //sendIdPromoter để check
    class SendIdPromoter extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(GetGiftActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlCheckPromoter);
            Log.e(TAG, "Response from url: " + jsonStr);
            if(jsonStr != null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    //Getting Json user
                    JSONObject checkPromoter = jsonObject.getJSONObject("json");
                    messageCheckPromoter = checkPromoter.getString("Message");
                    Log.e(TAG, "Message: " + messageCheckPromoter);

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
            if(messageCheckPromoter.equalsIgnoreCase("Exits")){
                new SaveGiftData().execute();
            }else{
                Toast.makeText(GetGiftActivity.this, "Mã xác nhận không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //Save data gift
    class SaveGiftData extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(GetGiftActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);//co the cancel bang phim back
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://f-bee.nks.com.vn/api/usergift/add"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("UserId", userId);
                postDataParams.put("GiftId", idGift);
                postDataParams.put("DateCreated", getToday());
                postDataParams.put("Promoter", promoter);
                postDataParams.put("token", token);
                Log.e("params",postDataParams.toString());


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            startActivity(new Intent(GetGiftActivity.this, MainActivity.class));
            finish();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
    public String getToday() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return date;
    }
}
