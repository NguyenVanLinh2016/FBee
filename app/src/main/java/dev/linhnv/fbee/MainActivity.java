package dev.linhnv.fbee;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.linhnv.fbee.adapter.ListForumAdapter;
import dev.linhnv.fbee.adapter.ViewPagerAdapter;
import dev.linhnv.fbee.fragments.EntertainmentFragment;
import dev.linhnv.fbee.fragments.ForumFragment;
import dev.linhnv.fbee.fragments.FragmentAdapter;
import dev.linhnv.fbee.fragments.LearningFragment;
import dev.linhnv.fbee.model.Forum;
import dev.linhnv.fbee.model.HttpHandler;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ImageView img_user, img_test;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //demo git hub

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }
        setSupportActionBar(toolbar);

        img_user = (ImageView) findViewById(R.id.img_user);
        img_test = (ImageView) findViewById(R.id.img_test);
        img_user.setVisibility(View.VISIBLE);
        img_test.setVisibility(View.VISIBLE);
        img_user.setOnClickListener(this);
        img_test.setOnClickListener(this);
        //viewpager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        //tabLayout
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();


    }
    private void setupTabIcons() {
        /*View view1 = getLayoutInflater().inflate(R.layout.customtab, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.learning);
        //TextView tabLearning = (TextView) getLayoutInflater().inflate(R.layout.customtab, null);
        //tabLearning.setText("Hoc tap");

        View view2 = getLayoutInflater().inflate(R.layout.customtab, null);
        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.forum);

        View view3 = getLayoutInflater().inflate(R.layout.customtab, null);
        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.entertainment);*/

        tabLayout.getTabAt(0).setIcon(R.drawable.learning);
        tabLayout.getTabAt(1).setIcon(R.drawable.forum);
        tabLayout.getTabAt(2).setIcon(R.drawable.entertainment);

    }
    private void setupViewPager(ViewPager viewPager){
        /*FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new LearningFragment(), "Hoc Tap");
        adapter.addFragment(new ForumFragment(), getString(R.string.relax));
        adapter.addFragment(new EntertainmentFragment(), getString(R.string.forum));
        viewPager.setAdapter(adapter);*/
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new LearningFragment(), getString(R.string.learning));
        adapter.addFrag(new ForumFragment(), getString(R.string.forum));
        adapter.addFrag(new EntertainmentFragment(), getString(R.string.relax));
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_user:
                startActivity(new Intent(MainActivity.this, UserActivity.class));
                break;
            case R.id.img_test:
                startActivity(new Intent(MainActivity.this, QuizActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.dialog_exit);
        builder.setTitle(R.string.titleDialog);
        builder.setPositiveButton(R.string.setNegativeButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
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