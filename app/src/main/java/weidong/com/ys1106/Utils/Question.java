package weidong.com.ys1106.Utils;

public class Question {
    private String Content;
    private String answerA;

    public Question(){

    }

    public Question(String content, String answerA, String ansWerB) {
        Content = content;
        this.answerA = answerA;
        this.ansWerB = ansWerB;
    }

    private String ansWerB;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnsWerB() {
        return ansWerB;
    }

    public void setAnsWerB(String ansWerB) {
        this.ansWerB = ansWerB;
    }


}
