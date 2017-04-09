package dev.linhnv.fbee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import dev.linhnv.fbee.adapter.ListAnswerAdapterOcc;
import dev.linhnv.fbee.adapter.ListAnswerAdapterPer;
import dev.linhnv.fbee.model.Question;
import dev.linhnv.fbee.model.QuizGetResultUser;

public class QuizResultActivity extends AppCompatActivity {

    private Toolbar toolbar;
    List<Question> list_ques;
    List<QuizGetResultUser> nav_list;
    ListView lv_answer_user;
    //adapterListview Personality
    private ListAnswerAdapterPer listAnswerAdapterPer;
    //adapterListview Occupation
    private ListAnswerAdapterOcc listAnswerAdapterOcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.result));
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("BUNDLE");
        int type_quiz = bundle.getInt("type_quiz");
        list_ques = (List<Question>) bundle.getSerializable("listQuestion");
        nav_list = (List<QuizGetResultUser>) bundle.getSerializable("nav_item_list");

        lv_answer_user = (ListView)  findViewById(R.id.lv_answer_user);
        Toast.makeText(this, ""+type_quiz, Toast.LENGTH_SHORT).show();
        if(type_quiz == 0){//quiz nghe nghiep
            listAnswerAdapterOcc = new ListAnswerAdapterOcc(QuizResultActivity.this, R.layout.list_answer_item_occ, nav_list);
            lv_answer_user.setAdapter(listAnswerAdapterOcc);
        }else{ //quiz tinh canh
            listAnswerAdapterPer = new ListAnswerAdapterPer(QuizResultActivity.this, R.layout.list_answer_item_per, nav_list);
            lv_answer_user.setAdapter(listAnswerAdapterPer);
        }
    }
}
