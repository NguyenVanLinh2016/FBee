package dev.linhnv.fbee.model;

import java.io.Serializable;

/**
 * Created by DevLinhnv on 1/5/2017.
 */

public class Question implements Serializable {
    public int question_id;
    public String title;
    public String ans_a;
    public String ans_b;
    public String ans_c;
    public String ans_d;
    public String ans_e;
    public String ans_f;
    public String ans_g;
    public String ans_h;
    public int right_answer;
    public Question(){

    }

    public Question(int question_id, String title, String ans_a, String ans_b, String ans_c, String ans_d, String ans_e,
                    String ans_f, String ans_g, String ans_h, int right_answer) {
        this.question_id = question_id;
        this.title = title;
        this.ans_a = ans_a;
        this.ans_b = ans_b;
        this.ans_c = ans_c;
        this.ans_d = ans_d;
        this.ans_e = ans_e;
        this.ans_f = ans_f;
        this.ans_g = ans_g;
        this.ans_h = ans_h;
        this.right_answer = right_answer;
    }
}
