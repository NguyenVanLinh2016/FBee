package dev.linhnv.fbee.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import dev.linhnv.fbee.R;
import dev.linhnv.fbee.adapter.PostAdapter;
import dev.linhnv.fbee.model.Post;

/**
 * Created by DevLinhnv on 12/31/2016.
 */

public class LearningFragment extends Fragment {

    ListView listView;
    PostAdapter adapter;
    ArrayList<Post> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_learning, container, false);

        /*listView = (ListView)v.findViewById(R.id.listViewLearning);
        list = new ArrayList<Post>();
        list.add(new Post(1, 1, "Android 1", "Android cơ bản Android cơ bản Android cơ bản Android cơ bản " +
                "Android cơ bản Android cơ bản Android cơ bản Android cơ bản Android cơ bản Android cơ bản " +
                "Android cơ bản Android cơ bản Android cơ bản Android cơ bản Android cơ bản Android cơ bản ", "", "", ""));
        list.add(new Post(1, 1, "Android 1", "Android cơ bản Android cơ bản Android cơ bản Android cơ bản " +
                "Android cơ bản Android cơ bản Android cơ bản Android cơ bản Android cơ bản Android cơ bản " +
                "Android cơ bản Android cơ bản Android cơ bản Android cơ bản Android cơ bản Android cơ bản ", "", "", ""));
        adapter = new PostAdapter(getContext(), list);
        listView.setAdapter(adapter);*/

        //Toast.makeText(getContext(), list.size()+"", Toast.LENGTH_SHORT).show();

        return v;
    }
}
