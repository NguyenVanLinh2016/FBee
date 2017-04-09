package dev.linhnv.fbee.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dev.linhnv.fbee.R;
import dev.linhnv.fbee.model.Forum;

/**
 * Created by DevLinhnv on 1/7/2017.
 */

public class ListForumAdapter extends ArrayAdapter<Forum> {

    private Context context;;
    private int resoure;
    private List<Forum> list;
    public ListForumAdapter(Context context, int resource, List<Forum> list) {
        super(context, resource, list);
        this.context = context;
        this.resoure = resource;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewrow = inflater.inflate(R.layout.list_item_forum, parent, false);

        ImageView img_avater = (ImageView) viewrow.findViewById(R.id.img_avatar_forum);
        TextView tv_ques_forum = (TextView) viewrow.findViewById(R.id.tv_ques_forum);

        Forum forum = list.get(position);
        img_avater.setImageBitmap(forum.getImage());
        tv_ques_forum.setText(forum.getTitle());
        return viewrow;
    }
}
