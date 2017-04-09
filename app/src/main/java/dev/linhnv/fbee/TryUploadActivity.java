package dev.linhnv.fbee;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class TryUploadActivity extends AppCompatActivity {
    EditText et_title, et_date_created, et_finish, et_mark, et_quiz_id;
    Button btn_submit;
    private String token = "eyJpdiI6IjNCV00wclpUYXlIMHJZKzZpN0tZMFE9PSIsInZhbHVlIjoiSWFwYW9OK1FzRGhKbHZaeDl3dm1sQT09IiwibWFjIjoiMzE0ZjcwM2UxZjZlMjExZTM5YTMxN2QyNTgzNTA0N2U0MDZjMTU5YWY1Nzg1ZjgwYmJiM2YxNWIwZDRiODczNyJ9";
    private String url_upload = "http://f-bee.nks.com.vn/api/userquiz/add";
    private static final String TAG_SUCCESS = "success";
    ProgressDialog progressDialog;
    private String TAG = TryUploadActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_upload);
        et_title = (EditText) findViewById(R.id.et_title);
        et_date_created = (EditText) findViewById(R.id.et_date_created);
        et_finish = (EditText) findViewById(R.id.et_finish);
        et_mark = (EditText) findViewById(R.id.et_mark);
        et_quiz_id = (EditText) findViewById(R.id.et_quiz_id);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = et_title.getText().toString();
                String date_created = et_date_created.getText().toString();
                String finish = et_finish.getText().toString();
                String mark = et_mark.getText().toString();
                String quiz_id = et_quiz_id.getText().toString();
                new AddNewQuestion().execute(title, date_created, finish, mark, quiz_id, token);
            }
        });
    }
    class AddNewQuestion extends AsyncTask<String, String, String> {
        String title, date_created, finish, mark, quiz_id, token1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(TryUploadActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);//co the cancel bang phim back
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            title = strings[0];
            date_created = strings[1];
            finish = strings[2];
            mark = strings[3];
            quiz_id = strings[4];
            token1 = strings[5];
            try {
                URL url = new URL("http://f-bee.nks.com.vn/api/userquiz/add"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("Title", title);
                postDataParams.put("DateCreate", date_created);
                postDataParams.put("Finish", finish);
                postDataParams.put("Mark", mark);
                postDataParams.put("Quiz_id", quiz_id);
                postDataParams.put("token", token1);
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
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
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
}
