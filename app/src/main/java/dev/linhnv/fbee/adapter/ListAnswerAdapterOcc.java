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

public class ListAnswerAdapterOcc extends ArrayAdapter<QuizGetResultUser> {

    private Context context;
    private int resource;
    private List<QuizGetResultUser> list;
    public ListAnswerAdapterOcc(Context context, int resource, List<QuizGetResultUser> list) {
        super(context, resource, list);
        this.context = context;
        this.resource = resource;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewrow = inflater.inflate(R.layout.list_answer_item_occ, parent, false);

        TextView txtQuestion = (TextView) viewrow.findViewById(R.id.lblQuestionOcc);
        TextView txtAnswer = (TextView) viewrow.findViewById(R.id.lblAnswerOcc);

        QuizGetResultUser quizGetResultUser = list.get(position);
        txtQuestion.setText(quizGetResultUser.getPositionAnswer() +"");
        switch (quizGetResultUser.answerUser1){
            case 1:
                txtAnswer.setText("Đúng");
                break;
            case 2:
                txtAnswer.setText("Sai");
                break;
            default:
                txtAnswer.setText("");
        }
        return viewrow;
    }
}
