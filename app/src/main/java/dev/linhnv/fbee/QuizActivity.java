package dev.linhnv.fbee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    Button btn_start;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.test1));
        }

        MultiStateToggleButton button = (MultiStateToggleButton) findViewById(R.id.mstb_multi_id);
        button.setValue(0);

        button.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                Log.d("TestFragment", "Position: " + position);
                count = position;
            }
        });
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                if(count == 0){
                    startActivity(new Intent(QuizActivity.this, QuizOccupationActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(QuizActivity.this, QuizPersonalityActivity.class));
                    finish();
                }
                break;
        }
    }
}
