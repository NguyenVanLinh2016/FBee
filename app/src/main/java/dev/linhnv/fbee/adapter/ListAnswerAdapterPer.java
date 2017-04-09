package dev.linhnv.fbee.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import dev.linhnv.fbee.R;
import dev.linhnv.fbee.model.QuizGetResultUser;

/**
 * Created by DevLinhnv on 1/10/2017.
 */

public class ListAnswerAdapterPer extends ArrayAdapter<QuizGetResultUser> {
    private Context context;
    private int resource;
    private List<QuizGetResultUser> list;
    public ListAnswerAdapterPer(Context context, int resource, List<QuizGetResultUser> list) {
        super(context, resource, list);
        this.context = context;
        this.resource = resource;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewrow = inflater.inflate(R.layout.list_answer_item_per, parent, false);
        TextView txtQuestion = (TextView) viewrow.findViewById(R.id.lblQuestion);
        TextView txtAnswer1 = (TextView) viewrow.findViewById(R.id.lblAnswer1);
        TextView txtAnswer2 = (TextView) viewrow.findViewById(R.id.lblAnswer2);
        TextView txtAnswer3 = (TextView) viewrow.findViewById(R.id.lblAnswer3);
        TextView txtAnswer4 = (TextView) viewrow.findViewById(R.id.lblAnswer4);
        QuizGetResultUser quizGetResultUser = list.get(position);

        txtQuestion.setText(quizGetResultUser.getPositionAnswer() +"");
        switch (quizGetResultUser.answerUser1){
            case 0:
                txtAnswer1.setText("");
                break;
            case 1:
                txtAnswer1.setText("A");
                break;
            case 2:
                txtAnswer1.setText("B");
                break;
        }
        switch (quizGetResultUser.answerUser2){
            case 0:
                txtAnswer2.setText("");
                break;
            case 3:
                txtAnswer2.setText("C");
                break;
            case 4:
                txtAnswer2.setText("D");
                break;
        }
        switch (quizGetResultUser.answerUser3){
            case 0:
                txtAnswer3.setText("");
                break;
            case 5:
                txtAnswer3.setText("E");
                break;
            case 6:
                txtAnswer3.setText("F");
                break;
        }
        switch (quizGetResultUser.answerUser4){
            case 0:
                txtAnswer4.setText("");
                break;
            case 7:
                txtAnswer4.setText("G");
                break;
            case 8:
                txtAnswer4.setText("H");
                break;
        }

        return viewrow;

    }
}
