package dev.linhnv.fbee.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import dev.linhnv.fbee.R;
import dev.linhnv.fbee.model.Post;

/**
 * Created by DevLinhnv on 1/16/2017.
 */

public class PostAdapter extends BaseAdapter {

    Context context;
    ArrayList<Post> list;

    public PostAdapter(Context context, ArrayList<Post> list){
        this.context = context;
        this.list = list;
    }

    public static class View_One_Line
    {
        public ImageView iv_thumbnail;
        public TextView tv_title;
        public TextView tv_description;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View_One_Line mot_o;
        LayoutInflater inf = ((Activity) context).getLayoutInflater();
        if (convertView == null) {
            mot_o = new View_One_Line();
            convertView = inf.inflate(R.layout.one_line_topic, null);
            mot_o.iv_thumbnail = (ImageView) convertView.findViewById(R.id.imageViewThumbnail);
            mot_o.tv_title = (TextView) convertView.findViewById(R.id.textViewTitle);
            mot_o.tv_description = (TextView) convertView.findViewById(R.id.textViewDescription);
            convertView.setTag(mot_o);
        } else {
            mot_o = (View_One_Line) convertView.getTag();
        }

        mot_o.tv_title.setText(list.get(position).post_title);
        String description = list.get(position).post_description;
        mot_o.tv_description.setText(description.substring(0,70)+"...");

        return convertView;
    }
}
