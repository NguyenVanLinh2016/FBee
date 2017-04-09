package dev.linhnv.fbee.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

import dev.linhnv.fbee.GetGiftActivity;
import dev.linhnv.fbee.MainActivity;
import dev.linhnv.fbee.R;
import dev.linhnv.fbee.TestAndGiftActivity;
import dev.linhnv.fbee.model.Forum;
import dev.linhnv.fbee.model.Gift;
import dev.linhnv.fbee.model.HttpHandler;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by DevLinhnv on 12/31/2016.
 */

public class GiftFragment extends Fragment {

    //url doc du lieu tu table user de lay userId
    String urlUser;
    int userId;

    Button btn_start_gift;
    ImageSwitcher imageSwitcher;
    TextView tv_gift;
    Runnable myRunnable;
    Boolean stop = false;
    int[] imageArray = new int[]{R.drawable.book, R.drawable.bag, R.drawable.clock, R.drawable.bell};

    int id = 0;
    String bonus = "";

    private ProgressDialog progressDialog;
    String token;
    //url doc du lieu tu list gift
    String urlListGift;
    //doi tuong gift
    List<Gift> listGift;
    //check user da nhan qua chua
    String urlCheckUserGift;
    String messageCheckGift;
    //check qua con hay khong
    String urlCheckGiftIsStock;
    String messageCheckGiftIsStock;
    int idGift;
    public GiftFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gift, container, false);

        SharedPreferences pre = this.getActivity().getSharedPreferences("token", MODE_PRIVATE);
        token = pre.getString("token", "");
        //get id user
        urlUser =  "http://f-bee.nks.com.vn/api/auth/me?token=" + token;
        new GetUserId().execute();

        urlListGift = "http://f-bee.nks.com.vn/api/gift/list?token=" + token;


        btn_start_gift = (Button) view.findViewById(R.id.btn_quaythuong);
        tv_gift = (TextView) view.findViewById(R.id.tvQua);
        imageSwitcher = (ImageSwitcher) view.findViewById(R.id.imageSwithcher);

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView image = new ImageView(getActivity().getApplicationContext());
                image.setImageResource(R.drawable.gift);
                return image;
            }
        });

        Animation in = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left);
        in.setDuration(50);
        Animation out = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right);
        out.setDuration(50);
        imageSwitcher.setInAnimation(in);
        imageSwitcher.setOutAnimation(out);

        myRunnable = new Runnable() {
            int updateInterval = 150;
            int i = 0;

            @Override
            public void run() {

                if (!stop) {
                    imageSwitcher.setImageResource(imageArray[i]);
                    i++;
                    if (i >= imageArray.length)
                        i = 0;

                    imageSwitcher.postDelayed(this, updateInterval);
                    imageSwitcher.setImageResource(imageArray[i]);
                }
            }
        };
        btn_start_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRunnable.run();
                Random rd = new Random();
                final int a = rd.nextInt(100);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stop = true;
                        setImageResource(a);
                    }
                }, 3000);
            }
        });

        return view;
    }

    public void setImageResource(int x) {

        if (x <= 40) {
            id = imageArray[0];
            bonus = listGift.get(0).title;
            idGift = 1;
        } else if (x <= 70) {
            id = imageArray[1];
            bonus = listGift.get(1).title;
            idGift = 2;
        } else if (x <= 90) {
            id = imageArray[2];
            bonus = listGift.get(2).title;
            idGift = 3;
        } else if (x <= 100) {
            id = imageArray[3];
            bonus = listGift.get(3).title;
            idGift = 4;
        }

        imageSwitcher.setImageResource(id);
        //lấy quà và kiểm tra xem trên database còn có hok
        urlCheckGiftIsStock = "http://f-bee.nks.com.vn/api/gift/empty?token=" +token +"&id="+ idGift;
        new CheckListGiftIsStock().execute();

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
            /*if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }*/
            //Neu tra ve la Exits thi user da nhan qua roi
            if(messageCheckGift.equalsIgnoreCase("Exits")){
                tv_gift.setVisibility(View.VISIBLE);
                tv_gift.setText(getString(R.string.finishGift));
            }else{ //user chua nhan qua thi load du lieu qua ve
                new GetListGift().execute();
                btn_start_gift.setVisibility(View.VISIBLE);
            }
        }
    }

    //get List Gift
    class GetListGift extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
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
            progressDialog = new ProgressDialog(getActivity());
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
            if (messageCheckGiftIsStock.equalsIgnoreCase("Instock")){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                final LayoutInflater inflater = getActivity().getLayoutInflater();
                View inflator = inflater.inflate(R.layout.gift_dialog, null);
                alertDialog.setView(inflator);
                alertDialog.setCancelable(false);
                ImageView iv = (ImageView) inflator.findViewById(R.id.imageDialogQua);
                TextView tv = (TextView) inflator.findViewById(R.id.tvThongtinQua);
                iv.setImageResource(id);
                tv.setText("Bạn đã trúng một " + bonus + ".\nNhấn Ok để nhận quà.");

                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        tv_gift.setVisibility(View.VISIBLE);
                        btn_start_gift.setVisibility(View.GONE);
                        tv_gift.setText(getString(R.string.getGiftDone));
                        Intent intent = new Intent(getActivity(), GetGiftActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("userId", userId);
                        bundle.putInt("idGift", idGift);
                        bundle.putString("bonus", bonus);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                final LayoutInflater inflater = getActivity().getLayoutInflater();
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
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                });
                alertDialog.show();
            }
        }
    }
}
