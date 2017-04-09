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
import dev.linhnv.fbee.model.Comment_forum;
import dev.linhnv.fbee.model.Forum;

/**
 * Created by DevLinhnv on 1/11/2017.
 */

public class ListCommentAdapter extends ArrayAdapter<Comment_forum> {
    private Context context;
    private int resource;
    private List<Comment_forum> list;

    public ListCommentAdapter(Context context, int resource, List<Comment_forum> list) {
        super(context, resource, list);
        this.context = context;
        this.resource = resource;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewrow = inflater.inflate(R.layout.list_comment_item_forum, parent, false);

        ImageView img_comment_forum = (ImageView) viewrow.findViewById(R.id.img_comment_forum);
        TextView tv_username = (TextView) viewrow.findViewById(R.id.tv_username_forum);
        TextView tv_comment_forum = (TextView) viewrow.findViewById(R.id.tv_comment_forum);

        Comment_forum comment_forum = list.get(position);
        /*img_comment_forum.setImageBitmap(comment_forum.getAvatar());
        tv_username.setText(comment_forum.getUsername());
        tv_comment_forum.setText(comment_forum.getComment_title());*/
        return viewrow;
    }
}
