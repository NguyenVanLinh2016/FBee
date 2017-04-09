package dev.linhnv.fbee;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UserActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView img_edit, img_logout;
    String token;
    TextView txtField, txtFullname, txtEmail, txtPhone, txtGender, txtDateOfBirth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_user);

        toolbar = (Toolbar) findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);
        txtField = (TextView) findViewById(R.id.txtFiedlUser);
        txtFullname = (TextView) findViewById(R.id.txtFullnameUser);
        txtEmail = (TextView) findViewById(R.id.txtEmailUser);
        txtPhone = (TextView) findViewById(R.id.txtPhoneUser);
        txtGender = (TextView) findViewById(R.id.txtGenderUser);
        txtDateOfBirth = (TextView) findViewById(R.id.txtEditDateOfBirthUser);

        SharedPreferences pre = getSharedPreferences("token", MODE_PRIVATE);
        token = pre.getString("token", "");
        new readDataUser().execute("http://f-bee.nks.com.vn/api/auth/me?token=" + token);
        img_edit = (ImageView) findViewById(R.id.img_edit);
        img_logout = (ImageView) findViewById(R.id.img_logout);

        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(UserActivity.this, ChangeProfileActivity.class), 0);
            }
        });
        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserActivity.this, LoginActivity.class));
                LoginManager.getInstance().logOut();
                SharedPreferences prefs = getSharedPreferences("token", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("token", "");
                editor.putString("password", "");
                editor.commit();
            }
        });
    }

    private class readDataUser extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UserActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String chuoi = docNoiDung_Tu_URL(strings[0]);
            return chuoi;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            try {
                JSONObject root = new JSONObject(s);
                JSONObject json = root.getJSONObject("user");
                txtFullname.setText(json.getString("Fullname"));
                txtField.setText(json.getString("School"));
                txtEmail.setText(json.getString("Email"));
                String gender = json.getString("Gender");
                if (gender.matches("1")) {
                    txtGender.setText("Nam");
                } else {
                    txtGender.setText("Nữ");
                }
                txtPhone.setText(json.getString("Phone"));
                String datOfBirth = json.getString("DateOfBirth");
                String year = datOfBirth.substring(0, 4);
                String month = datOfBirth.substring(5, 7);
                String day = datOfBirth.substring(8, 10);
                txtDateOfBirth.setText(day + "/" + month + "/" + year);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static String docNoiDung_Tu_URL(String theUrl) {
        StringBuilder content = new StringBuilder();

        try {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            new readDataUser().execute("http://f-bee.nks.com.vn/api/auth/me?token=" + token);
        }
    }
}
