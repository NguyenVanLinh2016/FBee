package dev.linhnv.fbee.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.hlab.fabrevealmenu.enums.Direction;
import com.hlab.fabrevealmenu.listeners.OnFABMenuSelectedListener;
import com.hlab.fabrevealmenu.model.FABMenuItem;
import com.hlab.fabrevealmenu.view.FABRevealMenu;

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

import javax.net.ssl.HttpsURLConnection;

import dev.linhnv.fbee.MainActivity;
import dev.linhnv.fbee.R;
import dev.linhnv.fbee.adapter.ListCommentAdapter;
import dev.linhnv.fbee.adapter.ListForumAdapter;
import dev.linhnv.fbee.model.Comment_forum;
import dev.linhnv.fbee.model.Forum;
import dev.linhnv.fbee.model.HttpHandler;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by DevLinhnv on 12/31/2016.
 */

public class ForumFragment extends BaseFragment implements OnFABMenuSelectedListener, View.OnClickListener{
    //forum
    ListView lv_forum;
    ListForumAdapter list_forum_adpater;
    List<Forum> list_forum;
    Button btn_post;
    EditText et_post_news;

    //list_comment
    ListView lv_comment_forum;
    //adapter list comment forum
    ListCommentAdapter listCommentAdapter;
    //list object
    List<Comment_forum> listComment;
    String title;
    String description;
    String image;
    String token;
    String fullname;
    AlertDialog dialog;
    private ProgressDialog progressDialog;
    private static final String TAG = "ForumFragment";
    //url doc du lieu tu table Topic
    String url;
    public ForumFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);

        SharedPreferences pre = this.getActivity().getSharedPreferences("token", MODE_PRIVATE);
        token = pre.getString("token", "");
        fullname = pre.getString("fullname", "");
        String pass = pre.getString("password", "");
        Log.d("Fullname", ""+fullname);
        Log.d("Token", ""+token);
        Log.d("PASS", ""+pass);
        //Toast.makeText(getActivity(), ""+fullname, Toast.LENGTH_SHORT).show();

        //forum
        lv_forum = (ListView) view.findViewById(R.id.listview_forum);
        list_forum = new ArrayList<Forum>();
        url =  "http://f-bee.nks.com.vn/api/topic/list?token=" + token;

        loadDataForum(); //gọi hàm load dữ liệu từ database bang topic
        new GetDataListTopic().execute();
        lv_forum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Forum forum = list_forum.get(i);
                String title = forum.getTitle();
                Toast.makeText(getActivity(), ""+title, Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                view = layoutInflater.from(getActivity()).inflate(R.layout.layout_comment_forum, null, false);
                lv_comment_forum = (ListView) view.findViewById(R.id.lv_comment_forum);
                listComment = new ArrayList<Comment_forum>();
                Bitmap img_avatar = BitmapFactory.decodeResource(getResources(),R.drawable.user);
                /*for(int j=0; j< 10; j++){
                    listComment.add(new Comment_forum(1, img_avatar, "Di Linh", "Bạn nên đến cơ sở FPT nhé!"));
                }*/
                listCommentAdapter = new ListCommentAdapter(getActivity(), R.layout.list_comment_item_forum, listComment);
                lv_comment_forum.setAdapter(listCommentAdapter);

                builder.setView(view);
                builder.setCancelable(true);
                builder.create().show();

            }
        });

        //floating button
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        final FABRevealMenu fabMenu = (FABRevealMenu) view.findViewById(R.id.fabMenu);

        try {
            if (fab != null && fabMenu != null) {
                setFabMenu(fabMenu);
                fabMenu.bindAncherView(fab);
                fabMenu.setMenuDirection(Direction.UP);
                fabMenu.setOnFABMenuSelectedListener(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }
    public void loadDataForum(){
        list_forum_adpater = new ListForumAdapter(getActivity(), R.layout.list_item_forum, list_forum);
        lv_forum.setAdapter(list_forum_adpater);
        list_forum_adpater.notifyDataSetChanged();
    }


    @Override
    public void onMenuItemSelected(View view) {
        int id = (int) view.getTag();
        switch (id){
            case R.id.menu_post_news:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                view = layoutInflater.from(getActivity()).inflate(R.layout.layout_post_news, null, false);
                btn_post = (Button) view.findViewById(R.id.btn_post);
                et_post_news = (EditText) view.findViewById(R.id.et_post_news);

                btn_post.setOnClickListener(this);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setView(view);
                builder.setCancelable(false);
                dialog = builder.create();
                dialog.show();
                break;
            case R.id.menu_search:
                Toast.makeText(getActivity(), "Click tìm kiếm theo tên", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_post:
                if(et_post_news.getText().toString().equalsIgnoreCase("")){
                    et_post_news.setError("Vui lòng bạn nhập thông tin");
                }else{
                    new SendDataForum().execute(et_post_news.getText().toString(), "null", "hinh1.png", token);
                }
                break;
        }
    }

    //class getList data table Topic
    class GetDataListTopic extends AsyncTask<Void, Void, Void>{
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
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);
            if(jsonStr != null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    //Getting Json Array node
                    JSONArray topic = jsonObject.getJSONArray("topic");
                    // looping through All product
                    for(int i=1; i< topic.length(); i++){
                        JSONObject tp = topic.getJSONObject(i);
                        String topicId = tp.getString("TopicId");
                        String title = tp.getString("Title");
                        String description = tp.getString("Description");
                        Bitmap img_avatar = BitmapFactory.decodeResource(getResources(),R.drawable.user);
                        Forum forum = new Forum(title, description, img_avatar, topicId);
                        list_forum.add(forum);
                    }
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
            loadDataForum();
        }
    }
    //class upload data table Topic
    class SendDataForum extends AsyncTask<String, String, String>{
        String title, description, image, token;

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
        protected String doInBackground(String... strings) {
            title = strings[0];
            description = strings[1];
            image = strings[2];
            token = strings[3];
            try {
                URL url = new URL("http://f-bee.nks.com.vn/api/topic/add"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("Title", title);
                postDataParams.put("Description", description);
                postDataParams.put("Image", image);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            dialog.dismiss();
            Toast.makeText(getActivity(), "Đặt câu hỏi thành công", Toast.LENGTH_SHORT).show();
        }
    }
    //class upload comment table message
    class SendCommentForum extends AsyncTask<String, String, String>{
        String title, description, dateCreated, parent, userId, topicId,token;

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
        protected String doInBackground(String... strings) {
            title = strings[0];
            description = strings[1];
            dateCreated = strings[2];
            parent = strings[3];
            userId = strings[4];
            topicId = strings[5];
            topicId = strings[6];
            try {
                URL url = new URL("http://f-bee.nks.com.vn/api/message/add"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("Title", title);
                postDataParams.put("Description", description);
                postDataParams.put("DateCreated", dateCreated);
                postDataParams.put("Parent", parent);
                postDataParams.put("UserId", userId);
                postDataParams.put("TopicId", topicId);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            loadDataForum();
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
