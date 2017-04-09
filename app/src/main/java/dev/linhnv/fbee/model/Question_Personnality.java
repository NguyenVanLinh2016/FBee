package dev.linhnv.fbee.model;

/**
 * Created by linhnv on 18/03/2017.
 */

public class Question_Personnality {
    public int question_id;
    public String ques1;
    public String ques2;
    public String ques3;
    public String ques4;
    public String ques5;
    public String ques6;
    public String ques7;
    public String ques8;
    public String ques9;
    public Question_Personnality(){}
    public Question_Personnality(int question_id, String ques1, String ques2, String ques3, String ques4, String ques5, String ques6, String ques7, String ques8, String ques9){
        this.question_id = question_id;
        this.ques1 = ques1;
        this.ques2 = ques2;
        this.ques3 = ques3;
        this.ques4 = ques4;
        this.ques5 = ques5;
        this.ques6 = ques6;
        this.ques7 = ques7;
        this.ques8 = ques8;
        this.ques9 = ques9;
    }
}
