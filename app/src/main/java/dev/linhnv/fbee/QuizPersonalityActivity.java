package dev.linhnv.fbee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import dev.linhnv.fbee.adapter.ExpandableListAdapter;
import dev.linhnv.fbee.model.Question;
import dev.linhnv.fbee.model.Question_Personnality;
import dev.linhnv.fbee.model.QuizGetResultUser;
import dev.linhnv.fbee.model.QuizGetResultUserPer;

public class QuizPersonalityActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    List<String> listHeader;
    private TextView txt_next_per, txt_header_per;
    private TextView txt_answer1, txt_answer2, txt_answer3, txt_answer4, txt_answer5, txt_answer6, txt_answer7, txt_answer8,
            txt_answer9;
    EditText edt_answer1, edt_answer2, edt_answer3, edt_answer4, edt_answer5, edt_answer6, edt_answer7, edt_answer8, edt_answer9;
    private String answer1, answer2, answer3, answer4, answer5, answer6, answer7, answer8, answer9;
    private int ans1, ans2, ans3, ans4, ans5, ans6, ans7, ans8, ans9;
    List<Question_Personnality> list_ques_per;
    private Question_Personnality question_personnality;
    //list nhan gia tri nhap vao cua user
    List<QuizGetResultUserPer> listGetAnserUserPer;
    QuizGetResultUserPer quizGetResultUserPer = new QuizGetResultUserPer();
    int mark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_personality);

        toolbar = (Toolbar) findViewById(R.id.toolbar_per);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.test_personality));
        }
        txt_next_per = (TextView) findViewById(R.id.txt_next_per);
        txt_header_per = (TextView) findViewById(R.id.txt_header_per);
        txt_answer1 = (TextView) findViewById(R.id.txt_answer1_per);
        txt_answer2 = (TextView) findViewById(R.id.txt_answer2_per);
        txt_answer3 = (TextView) findViewById(R.id.txt_answer3_per);
        txt_answer4 = (TextView) findViewById(R.id.txt_answer4_per);
        txt_answer5 = (TextView) findViewById(R.id.txt_answer5_per);
        txt_answer6 = (TextView) findViewById(R.id.txt_answer6_per);
        txt_answer7 = (TextView) findViewById(R.id.txt_answer7_per);
        txt_answer8 = (TextView) findViewById(R.id.txt_answer8_per);
        txt_answer9 = (TextView) findViewById(R.id.txt_answer9_per);
        //editText
        edt_answer1 = (EditText) findViewById(R.id.edt_answer1_per);
        edt_answer2 = (EditText) findViewById(R.id.edt_answer2_per);
        edt_answer3 = (EditText) findViewById(R.id.edt_answer3_per);
        edt_answer4 = (EditText) findViewById(R.id.edt_answer4_per);
        edt_answer5 = (EditText) findViewById(R.id.edt_answer5_per);
        edt_answer6 = (EditText) findViewById(R.id.edt_answer6_per);
        edt_answer7 = (EditText) findViewById(R.id.edt_answer7_per);
        edt_answer8 = (EditText) findViewById(R.id.edt_answer8_per);
        edt_answer9 = (EditText) findViewById(R.id.edt_answer9_per);

        txt_next_per.setOnClickListener(this);

        listHeader = new ArrayList<>();
        listHeader.add("Realistic - Người thực tế");
        listHeader.add("Investigative - Người thích nghiên cứu");
        listHeader.add("Artistic - Người có tính nghệ sĩ");
        listHeader.add("Social - Người có Tính xã hội");
        listHeader.add("Enterprising - Người dám nghĩ dám làm");
        listHeader.add("Conventional - Người công chức");

        list_ques_per = new ArrayList<Question_Personnality>();
        question_personnality = new Question_Personnality();

        list_ques_per.add(0, new Question_Personnality(1, "Tôi có tính tự lập", "Tôi suy nghĩ thực tế",
                "Tôi là người thích nghi với moi trường mới", "Tôi có thể vận hành, các máy móc thiết bị",
                "Tôi làm các công việc thủ công như gấp giấy, đan, móc", "Tôi thích tiếp xúc với thiên nhiên, động vật, cây cỏ",
                "Tôi thích những công việc sử dụng tay chân hơn là trí óc", "Tôi thích những công việc thấy ngay kết quả",
                "Tôi thích làm việc ngoài trời hơn là trong phòng học, văn phòng"));
        list_ques_per.add(1, new Question_Personnality(2, "Tôi có thể tìm hiểu khám phá nhiều vấn đề mới","Tôi có khả năng phân tích vấn đề",
                "Tôi biết suy nghĩ một cách mạch lạc, chặt chẽ","Tôi thích thực hiện các thí nghiệm hay nghiên cứu",
                "Tôi có khả năng tổng hợp, khái quát, suy đoán những vấn đề","Tôi thích những hoạt động điều tra, phân loại, kiểm tra, đánh giá",
                "Tôi tự tổ chức công việc mình phải làm","Tôi thích suy nghĩ về những vấn đề phức tạp, làm những công việc phức tạp",
                "Tôi có khả năng giải quyết các vấn đề"));
        list_ques_per.add(2, new Question_Personnality(3, "Tôi là người dễ xúc động","Tôi có người có trí óc phong phú",
                "Tôi thích sự tự do, không theo những quy định, quy tắt","Tôi có khả năng thuyết trình, diễn xuất",
                "Tôi có thể chụp hình hoặc vẽ tranh, trang trí, điêu khắc","Tôi có năng khiếu âm nhạc",
                "Tôi có khả năng viết, trình bày những ý tưởng của mình","Tôi thích làm những công việc mới, những công việc đòi hỏi sự sáng tạo",
                "Tôi thoải mái bộc lộ những ý thích"));
        list_ques_per.add(1, new Question_Personnality(4, "Tôi là người thân thiện hay giúp đỡ người khác","Tôi thích gặp gỡ làm việc với con người",
                "Tôi là người lịch sự, tử tế","Tôi thích khuyên bảo, huấn luyện hay giảng giái cho người khác",
                "Tôi là người biết lắng nghe","Tôi thích các hoạt động chăm sóc sức khoẻ của bản thân và người khác",
                "Tôi thích các hoạt động vì mục tiêu chung của cộng đồng, xã hội","Tôi mong muốn mình có thể đóng góp để xã hội tốt đẹp hơn",
                "Tôi có khả năng hoà giải, giải quyết những sự việc mâu thuẫn"));
        list_ques_per.add(1, new Question_Personnality(5, "Tôi là người có tính phiêu lưu, mạo hiểm","Tôi có tính quyết đoán",
                "Tôi là người năng động","Tôi có khả năng diễn đạt, tranh luận và thuyết phục người khác",
                "Tôi thích các việc quản lý, đánh giá","Tôi thường đặt ra các mục tiêu, kế hoạch trong cuộc sống",
                "Tôi thích gây ảnh hưởng đến người khác", "Tôi là người thích cạnh tranh, và muốn mình giỏi hơn người khác",
                "Tôi muốn người khác phải kính trọng, nể phục tôi"));
        list_ques_per.add(1, new Question_Personnality(6, "Tôi là người có đầu óc sắp xếp, tổ chức","Tôi có tính cẩn thận",
                "Tôi là người chu đáo, chính xác và đáng tin cậy","Tôi thích công việc tính toán sổ sách, ghi chép số liệu",
                "Tôi thích các công việc lưu trữ, phân loại, cập nhật thông tin","Tôi thường đặt ra những mục tiêu, kế hoạch trong cuộc sống",
                "Tôi thích dự kiến các khoảng thu chi","Tôi thích lập thời khoá biểu, sắp xếp lịch làm việc",
                "Tôi thích làm việc với các con số, làm việc theo hướng dẫn, quy trình"));

        setQuestion(0);
        listGetAnserUserPer = new ArrayList<>();
    }
    int i = 0;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_next_per:
                answer1 = edt_answer1.getText().toString();
                answer2 = edt_answer2.getText().toString();
                answer3 = edt_answer3.getText().toString();
                answer4 = edt_answer4.getText().toString();
                answer5 = edt_answer5.getText().toString();
                answer6 = edt_answer6.getText().toString();
                answer7 = edt_answer7.getText().toString();
                answer8 = edt_answer8.getText().toString();
                answer9 = edt_answer9.getText().toString();
                if(answer1.length() == 0){
                    edt_answer1.requestFocus();
                    Toast.makeText(this, getString(R.string.err_per), Toast.LENGTH_SHORT).show();
                }else if(answer2.length() == 0){
                    edt_answer2.requestFocus();
                    Toast.makeText(this, getString(R.string.err_per), Toast.LENGTH_SHORT).show();
                }else if(answer3.length() == 0){
                    edt_answer3.requestFocus();
                    Toast.makeText(this, getString(R.string.err_per), Toast.LENGTH_SHORT).show();
                }else if(answer4.length() == 0){
                    edt_answer4.requestFocus();
                    Toast.makeText(this, getString(R.string.err_per), Toast.LENGTH_SHORT).show();
                }else if(answer5.length() == 0){
                    edt_answer5.requestFocus();
                    Toast.makeText(this, getString(R.string.err_per), Toast.LENGTH_SHORT).show();
                }else if(answer6.length() == 0){
                    edt_answer6.requestFocus();
                    Toast.makeText(this, getString(R.string.err_per), Toast.LENGTH_SHORT).show();
                }else if(answer7.length() == 0){
                    edt_answer7.requestFocus();
                    Toast.makeText(this, getString(R.string.err_per), Toast.LENGTH_SHORT).show();
                }else if(answer8.length() == 0){
                    edt_answer8.requestFocus();
                    Toast.makeText(this, getString(R.string.err_per), Toast.LENGTH_SHORT).show();
                }else if (answer9.length() == 0){
                    edt_answer9.requestFocus();
                    Toast.makeText(this, getString(R.string.err_per), Toast.LENGTH_SHORT).show();
                }
                else{
                    ans1 = Integer.parseInt(answer1);
                    ans2 = Integer.parseInt(answer2);
                    ans3 = Integer.parseInt(answer3);
                    ans4 = Integer.parseInt(answer4);
                    ans5 = Integer.parseInt(answer5);
                    ans6 = Integer.parseInt(answer6);
                    ans7 = Integer.parseInt(answer7);
                    ans8 = Integer.parseInt(answer8);
                    ans9 = Integer.parseInt(answer9);
                    mark = sumMark(ans1, ans2, ans3, ans4, ans5, ans6, ans7, ans8, ans9);
                    listGetAnserUserPer.add(new QuizGetResultUserPer(i, ans1, ans2, ans3, ans4, ans5, ans6, ans7, ans8, ans9, mark));

                    i++;
                    if(i<6){
                        setQuestion(i);
                        resetEditText();
                        edt_answer1.requestFocus();
                    }else {
                        for (int j=0; j<6; j++){
                            //sap xep giam dan roi boc vi tri dau tien ra, ta co diem so cao nhat
                            Collections.sort(listGetAnserUserPer, new Comparator<QuizGetResultUserPer>() {
                                @Override
                                public int compare(QuizGetResultUserPer o1, QuizGetResultUserPer o2) {
                                    if (o1.mark < o2.mark){
                                        return 1;
                                    }else if(o1.mark == o2.mark){
                                        return 0;
                                    }else {
                                        return -1;
                                    }
                                }
                            });
                        }
                        Intent intent = new Intent(QuizPersonalityActivity.this, QuizResultUserPersonalityActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", listGetAnserUserPer.get(0).id);
                        bundle.putInt("mark", listGetAnserUserPer.get(0).mark);
                        Log.d("thu..", ""+ listGetAnserUserPer.get(0).id +"---"+ listGetAnserUserPer.get(0).mark);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                }
                break;
        }
    }

    //settext giá trị lên bang header
    public void setQuestion(int i){
        txt_header_per.setText(listHeader.get(i));
        txt_answer1.setText("1. "+list_ques_per.get(i).ques1);
        txt_answer2.setText("2. "+list_ques_per.get(i).ques2);
        txt_answer3.setText("3. "+list_ques_per.get(i).ques3);
        txt_answer4.setText("4. "+list_ques_per.get(i).ques4);
        txt_answer5.setText("5. "+list_ques_per.get(i).ques5);
        txt_answer6.setText("6. "+list_ques_per.get(i).ques6);
        txt_answer7.setText("7. "+list_ques_per.get(i).ques7);
        txt_answer8.setText("8. "+list_ques_per.get(i).ques8);
        txt_answer9.setText("9. "+list_ques_per.get(i).ques9);
    }
    //reset edittext
    public void resetEditText(){
        edt_answer1.setText("");
        edt_answer2.setText("");
        edt_answer3.setText("");
        edt_answer4.setText("");
        edt_answer5.setText("");
        edt_answer6.setText("");
        edt_answer7.setText("");
        edt_answer8.setText("");
        edt_answer9.setText("");
    }
    //viet ham tinh diem
    public int sumMark(int ans1, int ans2, int ans3, int ans4, int ans5, int ans6, int ans7, int ans8, int ans9){
        int getMark = ans1 + ans2 + ans3 + ans4 + ans5 + ans6 + ans7 + ans8 + ans9;
        return getMark;
    }
}
