package quiz;

import java.io.Serializable;
import java.util.HashMap;

public class Question implements Serializable {

    int numberQuestion;
    String question;
    HashMap<String,Integer> answers;
    int correctAnswer;

    public Question(int numberQuestion, String question, HashMap<String, Integer> answers, int correctAnswer) {
        this.numberQuestion = numberQuestion;
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }
}
