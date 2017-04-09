package dev.linhnv.fbee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

public class QuizResultUserPersonalityActivity extends AppCompatActivity {
    private int id, mark;
    private TextView txt_title, txt_content;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result_user_personality);

        toolbar = (Toolbar) findViewById(R.id.toolbar_quizget);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.test_personality));
        }

        id = getIntent().getExtras().getInt("id");
        mark = getIntent().getExtras().getInt("mark");
        txt_title = (TextView) findViewById(R.id.txt_title_result);
        txt_content = (TextView) findViewById(R.id.txt_content_result);
        setResut(id);
    }
    public void setResut(int id){
        switch (id)
        {
            case 0:
                txt_title.setText(getString(R.string.titleA));
                txt_content.setText(getString(R.string.bangA));
                break;
            case 1:
                txt_title.setText(getString(R.string.titleB));
                txt_content.setText(getString(R.string.bangB));
                break;
            case 2:
                txt_title.setText(getString(R.string.titleC));
                txt_content.setText(getString(R.string.bangC));
                break;
            case 3:
                txt_title.setText(getString(R.string.titleD));
                txt_content.setText(getString(R.string.bangD));
                break;
            case 4:
                txt_title.setText(getString(R.string.titleE));
                txt_content.setText(getString(R.string.bangE));
                break;
            case 5:
                txt_title.setText(getString(R.string.titleF));
                txt_content.setText(getString(R.string.bangF));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(QuizResultUserPersonalityActivity.this, MainActivity.class));
        finish();
    }
}
