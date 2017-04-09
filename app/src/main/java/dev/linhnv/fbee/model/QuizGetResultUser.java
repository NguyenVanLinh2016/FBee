package dev.linhnv.fbee.model;

import java.io.Serializable;

/**
 * Created by DevLinhnv on 1/5/2017.
 */

public class QuizGetResultUser implements Serializable {
    public int positionAnswer;
    public int answerUser1;
    public int answerUser2;
    public int answerUser3;
    public int answerUser4;
    public QuizGetResultUser(){

    }

    public QuizGetResultUser(int positionAnswer, int answerUser1, int answerUser2, int answerUser3, int answerUser4) {
        this.positionAnswer = positionAnswer;
        this.answerUser1 = answerUser1;
        this.answerUser2 = answerUser2;
        this.answerUser3 = answerUser3;
        this.answerUser4 = answerUser4;
    }

    public int getPositionAnswer() {
        return positionAnswer;
    }

    public void setPositionAnswer(int positionAnswer) {
        this.positionAnswer = positionAnswer;
    }

    public int getAnswerUser1() {
        return answerUser1;
    }

    public void setAnswerUser1(int answerUser1) {
        this.answerUser1 = answerUser1;
    }

    public int getAnswerUser2() {
        return answerUser2;
    }

    public void setAnswerUser2(int answerUser2) {
        this.answerUser2 = answerUser2;
    }

    public int getAnswerUser3() {
        return answerUser3;
    }

    public void setAnswerUser3(int answerUser3) {
        this.answerUser3 = answerUser3;
    }

    public int getAnswerUser4() {
        return answerUser4;
    }

    public void setAnswerUser4(int answerUser4) {
        this.answerUser4 = answerUser4;
    }
}
