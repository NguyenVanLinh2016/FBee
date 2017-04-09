package dev.linhnv.fbee.model;

/**
 * Created by linhnv on 19/03/2017.
 */

public class QuizGetResultUserPer {
    public int id;
    public int getAnswer1;
    public int getAnswer2;
    public int getAnswer3;
    public int getAnswer4;
    public int getAnswer5;
    public int getAnswer6;
    public int getAnswer7;
    public int getAnswer8;
    public int getAnswer9;
    public int mark;
    public QuizGetResultUserPer(){

    }
    public QuizGetResultUserPer(int id, int getAnswer1, int getAnswer2, int getAnswer3, int getAnswer4, int getAnswer5, int getAnswer6, int getAnswer7, int getAnswer8, int getAnswer9, int mark){
        this.id = id;
        this.getAnswer1 = getAnswer1;
        this.getAnswer2 = getAnswer2;
        this.getAnswer3 = getAnswer3;
        this.getAnswer4 = getAnswer4;
        this.getAnswer5 = getAnswer5;
        this.getAnswer6 = getAnswer6;
        this.getAnswer7 = getAnswer7;
        this.getAnswer8 = getAnswer8;
        this.getAnswer9 = getAnswer9;
        this.mark = mark;
    }
}
