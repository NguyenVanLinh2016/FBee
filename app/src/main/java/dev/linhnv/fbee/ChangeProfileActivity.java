package dev.linhnv.fbee;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Calendar;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class ChangeProfileActivity extends AppCompatActivity {

    LinearLayout layoutEditDateOfBirth;
    TextView txtChangePassword, txtEditDateOfBirth;
    EditText edtSchool, edtFullname, edtEmail, edtPhone;
    RadioButton radioNam, radioNu;
    TextInputLayout tilFullname, tilEmail, tilPhone, tilSchool;
    Button btnChange;
    String token, username, fullname, email, phone, school, field, gender, dateOfBirth, password;
    AlertDialog dialog;
    Boolean checkPassword = false;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        layoutEditDateOfBirth = (LinearLayout) findViewById(R.id.layoutEditDateOfBirth);
        txtChangePassword = (TextView) findViewById(R.id.txtChangePassword);
        txtEditDateOfBirth = (TextView) findViewById(R.id.txtEditDateOfBirth);
        edtSchool = (EditText) findViewById(R.id.edtSchoolChangeProfile);
        edtFullname = (EditText) findViewById(R.id.edtFullnameChangeProfile);
        edtEmail = (EditText) findViewById(R.id.edtEmailChangeProfile);
        edtPhone = (EditText) findViewById(R.id.edtPhoneChangeProfile);
        radioNam = (RadioButton) findViewById(R.id.radioNamChangeProfile);
        radioNu = (RadioButton) findViewById(R.id.radioNuChangeProfile);
        tilSchool = (TextInputLayout) findViewById(R.id.tilSchool_ChangeProfile);
        tilFullname = (TextInputLayout) findViewById(R.id.tilFullname_ChangeProfile);
        tilEmail = (TextInputLayout) findViewById(R.id.tilEmail_ChangeProfile);
        tilPhone = (TextInputLayout) findViewById(R.id.tilPhone_ChangeProfile);
        btnChange = (Button) findViewById(R.id.btnSaveTheChange);

        layoutEditDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                java.util.Calendar lich = java.util.Calendar.getInstance();
                int ngay = lich.get(java.util.Calendar.DAY_OF_MONTH);
                int thang = lich.get(java.util.Calendar.MONTH);
                int nam = lich.get(java.util.Calendar.YEAR);
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment();
                cdp.setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        txtEditDateOfBirth.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                });
                cdp.setFirstDayOfWeek(java.util.Calendar.SUNDAY);
                cdp.setPreselectedDate(nam, thang, ngay);
                cdp.setDateRange(null, null);
                cdp.setThemeLight();
                cdp.show(getSupportFragmentManager(), "tag1");

            }
        });

        SharedPreferences pre = getSharedPreferences("token", MODE_PRIVATE);
        token = pre.getString("token", "");
        password = pre.getString("password", "");

        //đọc dữ liệu người dùng sau đó load lên form
        new readDataUser().execute("http://f-bee.nks.com.vn/api/auth/me?token=" + token);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = false;
                if (edtFullname.getText().length() == 0) {
                    tilFullname.setError("Vui lòng nhập họ tên!");
                    tilFullname.requestFocus();
                } else if (edtEmail.getText().length() == 0) {
                    tilEmail.setError("Vui lòng nhập email!");
                    tilEmail.requestFocus();
                } else if (edtPhone.getText().length() == 0) {
                    tilPhone.setError("Vui lòng nhập số điện thoại!");
                    tilPhone.requestFocus();
                } else if(edtSchool.getText().length() == 0) {
                    tilSchool.setError("Vui lòng nhập trường học!");
                    tilPhone.requestFocus();
                } else {
                    check = true;
                }
                if (check) {
                    fullname = edtFullname.getText().toString();
                    email = edtEmail.getText().toString();
                    phone = edtPhone.getText().toString();
                    school = edtSchool.getText().toString();
                    if (radioNam.isChecked()) {
                        gender = "1";
                    } else {
                        gender = "2";
                    }
                    new SendPostRequestEditUser().execute();
                }
            }
        });
        txtChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChangeProfileActivity.this);
                LayoutInflater inflater = ChangeProfileActivity.this.getLayoutInflater();
                View inflator = inflater.inflate(R.layout.changepassworddialog, null);
                alertDialog.setView(inflator);
                alertDialog.setCancelable(false);
                final EditText edtOldPassword = (EditText) inflator.findViewById(R.id.edtOldPassword);
                final EditText edtNewPassword = (EditText) inflator.findViewById(R.id.edtNewPassword);
                final EditText edtNewPasswordAgain = (EditText) inflator.findViewById(R.id.edtNewPasswordAgain);
                final TextInputLayout tilOldPassword = (TextInputLayout) inflator.findViewById(R.id.tilOldPassword);
                final TextInputLayout tilNewPassword = (TextInputLayout) inflator.findViewById(R.id.tilNewPassword);
                final TextInputLayout tilNewPasswordAgain = (TextInputLayout) inflator.findViewById(R.id.tilNewPasswordAgain);
                alertDialog.setPositiveButton("Đổi mật khẩu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                });
                alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog = alertDialog.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (edtOldPassword.getText().length() == 0) {
                            tilOldPassword.setError("Vui lòng nhập mật khẩu cũ!");
                            tilOldPassword.requestFocus();
                        } else {
                            if (edtNewPassword.getText().length() == 0) {
                                tilNewPassword.setError("Vui lòng nhập mật khẩu mới");
                                tilNewPassword.requestFocus();
                            } else {
                                checkPassword = true;
                            }
                        }
                        edtOldPassword.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                if (edtOldPassword.getText().toString().matches(password)) {
                                    tilOldPassword.setErrorEnabled(false);
                                } else {
                                    tilOldPassword.setError("Mật khẩu chưa khớp với mật khẩu cũ!");
                                    tilOldPassword.requestFocus();
                                    checkPassword = false;
                                }
                            }
                        });
                        edtNewPassword.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                if (edtNewPassword.getText().toString().matches(password)) {
                                    tilNewPassword.setError("Mật khẩu mới phải khác mật khẩu cũ!");
                                    tilNewPassword.requestFocus();
                                    checkPassword = false;
                                } else {
                                    tilNewPassword.setErrorEnabled(false);
                                }
                            }
                        });
                        edtNewPasswordAgain.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                if (edtNewPasswordAgain.getText().toString().matches(edtNewPassword.getText().toString())) {
                                    tilNewPasswordAgain.setErrorEnabled(false);
                                } else {
                                    tilNewPasswordAgain.setError("Mật khẩu nhập lại chưa khớp!");
                                    tilNewPasswordAgain.requestFocus();
                                    checkPassword = false;
                                }
                            }
                        });
                        if (checkPassword) {
                            password = edtNewPassword.getText().toString();
                            new SendPostRequestChangePassword().execute();
                        }
                    }
                });
            }
        });
    }
    //hàm để post data server
    public class SendPostRequestEditUser extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ChangeProfileActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);//co the cancel bang phim back
            progressDialog.show();
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://f-bee.nks.com.vn/api/auth/edit");
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("token", token);
                postDataParams.put("username", username);
                postDataParams.put("Fullname", fullname);
                postDataParams.put("Email", email);
                postDataParams.put("Phone", phone);
                postDataParams.put("DateOfBirth", dateOfBirth);
                postDataParams.put("School", school);
                postDataParams.put("Field", field);
                postDataParams.put("Gender", gender);
                Log.e("params", postDataParams.toString());

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

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false");
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            try {
                JSONObject root = new JSONObject(result);
                JSONObject json = root.getJSONObject("json");
                String message = json.getString("message");
                if (message.matches("Edit ok")) {
                    Toast.makeText(ChangeProfileActivity.this, "Sửa thông tin thành công!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(ChangeProfileActivity.this, "Lỗi sửa thông tin!", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
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

    //hàm để đổi password
    public class SendPostRequestChangePassword extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://f-bee.nks.com.vn/api/auth/edit");
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("token", token);
                postDataParams.put("password", password);
                Log.e("params", postDataParams.toString());

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

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false");
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject root = new JSONObject(result);
                JSONObject json = root.getJSONObject("json");
                String message = json.getString("message");
                if (message.matches("Edit ok")) {
                    Toast.makeText(ChangeProfileActivity.this, "Đổi password thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(ChangeProfileActivity.this, "Lỗi đổi password!", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private class readDataUser extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... strings) {
            String chuoi = docNoiDung_Tu_URL(strings[0]);
            return chuoi;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject root = new JSONObject(s);
                JSONObject json = root.getJSONObject("user");
                username = json.getString("username");
                edtFullname.setText(json.getString("Fullname"));
                edtSchool.setText(json.getString("School"));
                edtEmail.setText(json.getString("Email"));
                String gender = json.getString("Gender");
                field = json.getString("Field");
                if (gender.matches("1")) {
                    radioNam.setChecked(true);
                } else {
                    radioNu.setChecked(true);
                }
                edtPhone.setText(json.getString("Phone"));
                String datOfBirth = json.getString("DateOfBirth");
                String year = datOfBirth.substring(0, 4);
                String month = datOfBirth.substring(5, 7);
                String day = datOfBirth.substring(8, 10);
                txtEditDateOfBirth.setText(day + "/" + month + "/" + year);
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
    //phương thức check email
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
}
