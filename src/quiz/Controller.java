package quiz;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Controller {
//    private static ArrayList<Button> buttons;
    public Label questionLabel;
    public ArrayList<Button> buttons;
    public Label questionNum;
    public Button button1;
    public Button button2;
    public Button button3;
    public Button button4;
    public Label questionNumber;


    Quiz quiz = null;
    String username;

    @FXML
    void initialize() {
//        quiz = new Quiz();
        username = DialogForm.showAndGetString("Podaj swój nick");
        readSerialization(username);
        setButtons();
        quiz.start(questionLabel, questionNum, questionNumber, buttons);
        saveSerialization();
    }

    private void readSerialization(String username) {
        try {
            FileInputStream fileIn = new FileInputStream("pliki\\" + username + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            quiz = (Quiz) in.readObject();
            in.close();
            fileIn.close();
        }catch(FileNotFoundException e){
            quiz = new Quiz();
            quiz.getSettings(username);

        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
            return;
        }
    }

    private void setButtons() {
        buttons = new ArrayList<>();
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        buttons.add(button4);
    }

    @FXML
    void handleSubmit(ActionEvent e){
        String textAnswer = ((Button)e.getSource()).getText();
//        System.out.println(text);
        quiz.checkAnswer(textAnswer);
        quiz.getNextQuestion(questionLabel, questionNum, questionNumber, buttons);
        saveSerialization();

    }

    private void saveSerialization() {
        try {
            FileOutputStream fileOut = new FileOutputStream("pliki\\" + username + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(quiz);
            out.close();
            fileOut.close();
//            System.out.println("Serialized data is saved in /pliki/" + username + ".ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
