package dev.linhnv.fbee;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import dev.linhnv.fbee.fragments.EntertainmentFragment;
import dev.linhnv.fbee.fragments.ForumFragment;
import dev.linhnv.fbee.fragments.FragmentAdapter;
import dev.linhnv.fbee.fragments.FragmentGiftAdapter;
import dev.linhnv.fbee.fragments.GiftFragment;
import dev.linhnv.fbee.fragments.LearningFragment;
import dev.linhnv.fbee.fragments.TestFragment;

public class TestAndGiftActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_and_gift);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.test_and_gift));
        }
        //viewpager
        viewPager = (ViewPager) findViewById(R.id.viewpagerGift);
        setupViewPager(viewPager);
        //tabLayout
        tabLayout = (TabLayout) findViewById(R.id.tabGift);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }
    private void setupTabIcons() {
        View view1 = getLayoutInflater().inflate(R.layout.customtab, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.test);

        View view2 = getLayoutInflater().inflate(R.layout.customtab, null);
        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.gift);

        tabLayout.getTabAt(0).setCustomView(view1);
        tabLayout.getTabAt(1).setCustomView(view2);

    }
    private void setupViewPager(ViewPager viewPager){
        FragmentGiftAdapter adapter = new FragmentGiftAdapter(getSupportFragmentManager());
        adapter.addFragment(new TestFragment(), "");
        adapter.addFragment(new GiftFragment(), "");
        viewPager.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(TestAndGiftActivity.this, MainActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    public void onClick(View view) {

    }
}
