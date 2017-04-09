package dev.linhnv.fbee;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dev.linhnv.fbee.fragments.GiftFragment;
import dev.linhnv.fbee.model.Gift;
import dev.linhnv.fbee.model.HttpHandler;

import static android.content.ContentValues.TAG;

public class GiftActivity extends AppCompatActivity {
    private Toolbar toolbar;

    Button btn_start_gift;
    ImageSwitcher imageSwitcher;
    Runnable myRunnable;
    Boolean stop = false;
    int[] imageArray = new int[]{R.drawable.balo, R.drawable.tuirut, R.drawable.sach, R.drawable.viet, R.drawable.khongtrungqua};

    int id = 0;
    String bonus = "";
    private ProgressDialog progressDialog;
    String token;
    //url doc du lieu tu list gift
    String urlListGift;
    //doi tuong gift
    List<Gift> listGift;
    //check qua con hay khong
    String urlCheckGiftIsStock;
    String messageCheckGiftIsStock;
    int idGift;
    int userId;
    Handler myHandler = new Handler();
    int index = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.gift));
        }

        SharedPreferences pre = this.getSharedPreferences("token", MODE_PRIVATE);
        token = pre.getString("token", "");
        userId = getIntent().getExtras().getInt("userId");
        urlListGift = "http://f-bee.nks.com.vn/api/gift/list?token=" + token;
        new GetListGift().execute();

        btn_start_gift = (Button) findViewById(R.id.btn_quaythuong);
        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwithcher);

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView image = new ImageView(getApplicationContext());
                image.setImageResource(R.drawable.gift);
                return image;
            }
        });


        btn_start_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRunnable = new Runnable() {
                    int updateInterval = 1;
                    @Override
                    public void run() {
                        if (stop == false) {
                            imageSwitcher.setImageResource(imageArray[index]);
                            index++;
                            if (index >= imageArray.length){
                                index = 0;
                            }
                            imageSwitcher.postDelayed(this, updateInterval);
                        }
                    }
                };
                myRunnable.run();
                Random rd = new Random();
                final int a = rd.nextInt(100);
                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stop = true;
                        setImageResource(a);
                        checkGift();
                    }
                }, 4000);
            }
        });
    }
    public void setImageResource(int x) {
        if (x <= 50) {
            id = imageArray[4];
            bonus = "không trúng";
            idGift = 0; //hien tai khong trung thi  khong co tren database
        } else if (x <= 70) {
            id = imageArray[3];
            bonus = listGift.get(3).title;
            idGift = 4;
        } else if (x <= 85) {
            id = imageArray[2];
            bonus = listGift.get(2).title;
            idGift = 3;
        } else if (x <= 95) {
            id = imageArray[1];
            bonus = listGift.get(1).title;
            idGift = 2;
        }else if (x <= 100){
            id = imageArray[0];
            bonus = listGift.get(0).title;
            idGift = 1;
        }
        imageSwitcher.setImageResource(id);
    }
    public void checkGift(){
        if (idGift == 0){
            fail();
        }else {
            urlCheckGiftIsStock = "http://f-bee.nks.com.vn/api/gift/empty?token=" +token +"&id="+ idGift;
            new CheckListGiftIsStock().execute();
        }
    }
    //get List Gift
    class GetListGift extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(GiftActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlListGift);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    //Getting Json
                    JSONArray giftArray = jsonObject.getJSONArray("gift");
                    listGift = new ArrayList<Gift>();
                    for (int i = 0; i < giftArray.length(); i++) {
                        JSONObject list = giftArray.getJSONObject(i);
                        int giftId = list.getInt("GiftId");
                        String title = list.getString("Title");
                        int amount = list.getInt("Amount");
                        int percent = list.getInt("Percent");
                        Gift gift = new Gift();
                        gift.giftId = giftId;
                        gift.title = title;
                        gift.amount = amount;
                        gift.percent = percent;
                        listGift.add(gift);
                    }


                } catch (JSONException ex) {
                    Log.e(TAG, "Json parsing error: " + ex.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Dismiss the progress dialog
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
    //check list gift con qua hay khong
    class CheckListGiftIsStock extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(GiftActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlCheckGiftIsStock);
            Log.e(TAG, "Response from url: " + jsonStr);
            if(jsonStr != null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    //Getting Json user
                    JSONObject checkIsStockGift = jsonObject.getJSONObject("json");
                    messageCheckGiftIsStock = checkIsStockGift.getString("Message");
                    Log.e(TAG, "Message Check Gift is Stock: " + messageCheckGiftIsStock);

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
            if (messageCheckGiftIsStock.equalsIgnoreCase("Instock")){ //con qua
                inStock();
            }else{
                stock();
            }
        }
    }
    public void inStock(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GiftActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View inflator = inflater.inflate(R.layout.gift_dialog, null);
        alertDialog.setView(inflator);
        alertDialog.setCancelable(false);
        ImageView iv = (ImageView) inflator.findViewById(R.id.imageDialogQua);
        TextView tv = (TextView) inflator.findViewById(R.id.tvThongtinQua);
        iv.setImageResource(id);
        tv.setText("Bạn đã trúng " + bonus + ".\nNhấn Ok để nhận quà.");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(GiftActivity.this, GetGiftActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("userId", userId);
                bundle.putInt("idGift", idGift);
                bundle.putString("bonus", bonus);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        alertDialog.show();
    }
    public void stock(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GiftActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View inflator = inflater.inflate(R.layout.gift_dialog, null);
        alertDialog.setView(inflator);
        alertDialog.setCancelable(false);
        ImageView iv = (ImageView) inflator.findViewById(R.id.imageDialogQua);
        TextView tv = (TextView) inflator.findViewById(R.id.tvThongtinQua);
        iv.setImageResource(id);
        tv.setText("Bạn đã trúng một " + bonus + ".\nNhưng hiện tại hệ thống đã hết quà");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
                startActivity(getIntent());
            }
        });
        alertDialog.show();
    }
    public void fail(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GiftActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View inflator = inflater.inflate(R.layout.gift_dialog, null);
        alertDialog.setView(inflator);
        alertDialog.setCancelable(false);
        TextView tv_chucmung = (TextView) inflator.findViewById(R.id.tv_happy);
        ImageView iv = (ImageView) inflator.findViewById(R.id.imageDialogQua);
        TextView tv = (TextView) inflator.findViewById(R.id.tvThongtinQua);
        iv.setImageResource(id);
        tv_chucmung.setText("Rất tiếc");
        tv.setText("Bạn đã không trúng quà");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(GiftActivity.this, MainActivity.class));
                finish();
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GiftActivity.this);
        builder.setMessage(R.string.dialog_exit_gift);
        builder.setTitle(R.string.titleDialog);
        builder.setPositiveButton(R.string.setNegativeButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(GiftActivity.this, MainActivity.class));
                finish();
            }
        });
        builder.setNegativeButton(R.string.setPositiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
