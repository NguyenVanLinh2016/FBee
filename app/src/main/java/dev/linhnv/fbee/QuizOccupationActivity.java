package dev.linhnv.fbee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dev.linhnv.fbee.model.Question;
import dev.linhnv.fbee.model.QuizGetResultUser;

public class QuizOccupationActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private List<Question> list_question;
    TextView tv_pre_occupation, tv_next_occupation;
    TextView tv_question;
    Button btn_true, btn_false;
    //list nhận giá trị người dùng chọn
    private List<QuizGetResultUser> nav_item_list;
    //khai báo các biến answer
    private int answer1;
    private int answer2;
    private int answer3;
    private int answer4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_occupation);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.test_occupattion));
        }

        list_question = new ArrayList<Question>();
        for(int i=0; i<10; i++){
            list_question.add(new Question(i+1,"Câu hỏi đang cập nhật"+(i+1), "Đáp án đang cập nhật" +(i+1), "Đáp án đang cập nhật", "Đáp án đang cập nhật",
                    "Đáp án đang cập nhật", "Đáp án đang cập nhật!", "Đáp án đang cập nhật?", "Đáp án đang cập nhật", "Đáp án đang cập nhật", 1));
        }
        tv_next_occupation = (TextView) findViewById(R.id.tv_next_occupation);
        tv_pre_occupation = (TextView) findViewById(R.id.tv_pre_occupation);

        tv_question = (TextView) findViewById(R.id.tv_question_occ);
        btn_true = (Button) findViewById(R.id.btn_true);
        btn_false = (Button) findViewById(R.id.btn_false);

        //setText câu hỏi và câu trả lời lên
        setQuestion(0);

        tv_next_occupation.setOnClickListener(this);
        tv_pre_occupation.setOnClickListener(this);
        btn_true.setOnClickListener(this);
        btn_false.setOnClickListener(this);

        //list nhận giá trị người dùng chọn
        nav_item_list = new ArrayList<QuizGetResultUser>();
        for(int i=0; i<10; i++){
            nav_item_list.add(new QuizGetResultUser(i+1,0,0,0,0));
        }
    }

    int i = 0;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_next_occupation:
                i++;
                reset1();
                if(i < 10){
                    setQuestion(i);
                    saveStateAnswer(i);
                }else {
                    Intent intent = new Intent(QuizOccupationActivity.this, QuizResultActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("type_quiz", 0);
                    bundle.putSerializable("listQuestion", (Serializable) list_question);
                    bundle.putSerializable("nav_item_list", (Serializable) nav_item_list);
                    intent.putExtra("BUNDLE", bundle);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.tv_pre_occupation:
                if(i == 0){
                    setQuestion(0);
                }else{
                    i--;
                    setQuestion(i);
                    saveStateAnswer(i);
                }
                break;
            case R.id.btn_true:
                reset1();
                btn_true.setBackgroundResource(R.drawable.choose_button);
                answer1 = 1;
                nav_item_list.get(i).answerUser1 = answer1;
                break;
            case R.id.btn_false:
                reset1();
                btn_false.setBackgroundResource(R.drawable.choose_button);
                answer1 = 2;
                nav_item_list.get(i).answerUser1 = answer1;
                break;
        }
    }
    public void reset1(){
        btn_true.setBackgroundResource(R.drawable.design_button);
        btn_false.setBackgroundResource(R.drawable.design_button);
    }
    //settext giá trị lên Edittext
    public void setQuestion(int i){
        tv_question.setText(list_question.get(i).title);
        //btn_true.setText(list_question.get(i).ans_a);
        //btn_false.setText(list_question.get(i).ans_b);
    }
    public void saveStateAnswer(int i){
        switch (nav_item_list.get(i).answerUser1){
            case 1:
                reset1();
                btn_true.setBackgroundResource(R.drawable.choose_button);
                break;
            case 2:
                reset1();
                btn_false.setBackgroundResource(R.drawable.choose_button);
                break;
            default:
                reset1();
                break;
        }
    }
}
