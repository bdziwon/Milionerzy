package util;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Bartek on 2017-05-12.
 */
public class Question {

    //zmienne z javyfx używane w tabeli w QuestionScene
    private SimpleIntegerProperty   id;
    private SimpleStringProperty    question;
    private SimpleStringProperty    answer;
    private SimpleStringProperty    difficulty;


    //kolejno odpowiedzi: a,b,c,d
    private String                  optionA;
    private String                  optionB;
    private String                  optionC;
    private String                  optionD;


    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }



    public Question(int id, String question, String answer, String difficulty, String[] options) {
        this.id = new SimpleIntegerProperty(id);
        this.question = new SimpleStringProperty(question);
        this.answer = new SimpleStringProperty(answer);
        this.difficulty = new SimpleStringProperty(difficulty);
        this.optionA = options[0];
        this.optionB = options[1];
        this.optionC = options[2];
        this.optionD = options[3];
    }

    public String getQuestion() {

        return question.get();
    }

    public SimpleStringProperty questionProperty() {
        return question;
    }

    public void setQuestion(String question) {
        this.question.set(question);
    }

    public String getAnswer() {
        return answer.get();
    }

    public SimpleStringProperty answerProperty() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer.set(answer);
    }

    public String getDifficulty() {
        return difficulty.get();
    }

    public SimpleStringProperty difficultyProperty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty.set(difficulty);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }
}