package quiz;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


import java.io.*;
import java.util.*;

public class Quiz implements Serializable {
    String username;
    Question question;
    int questionIndex = 1;
    int correctToPass;
    int questionsCorrect = 0;
    int amountQuestion;
    final static String DELIMITER = ";";
    ArrayList<Question> listQuestions;


    public void getSettings(String username) {
        this.username = username;
        if (username.equals("Admin"))
            getRank();

        getQuestions();
        Collections.shuffle(listQuestions);
    }

    private void getRank() {
        try {
            readFileWithRank();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFileWithRank() throws IOException {
        File file = new File("ranking.dat");
        if (file.exists()) {
            ArrayList<String> list = new ArrayList<>();
            DataInputStream in = null;
            try {
                in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }

            boolean eof = false;
            String tekst = "";

            while (!eof) {
                try {
//                    list.add("Nick: " + in.readUTF() + " - Liczba punktów: " + +in.readInt() + "/" + in.readInt());
                    tekst += (in.readUTF() + " - Liczba punktów: " + +in.readInt() + "/" + in.readInt() + "\n");
                }catch (EOFException e){
                    eof = true;
                }
            }
            in.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION,tekst);
            alert.setHeaderText("Ranking");
            alert.showAndWait();
            System.exit(0);

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Brak rankingu! Nikt jeszcze nie rozwiazywał testu.");
            alert.showAndWait();
            System.exit(0);
        }
    }

    private void getQuestions() {
        listQuestions = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("pytania.csv"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] strings = line.split(";");
//                System.out.println(Arrays.toString(strings));
                HashMap<String, Integer> answers = new HashMap<>();
                int value;
                if (strings.length > 0) {
                    for (int i = 1; i <= 4; i++) {
                        if (Integer.parseInt(strings[6]) == i)
                            value = 1;
                        else
                            value = 0;
                        answers.put(strings[i + 1], value);
                    }
                    Collections.shuffle(Collections.singletonList(answers));
                    listQuestions.add(new Question(Integer.parseInt(strings[0]), strings[1], answers, Integer.parseInt(strings[6])));
                }
            }
            amountQuestion = listQuestions.size();
        } catch (IOException e) {
            Alert notFound = new Alert(Alert.AlertType.ERROR);
            notFound.setContentText("Nie można załadować pliku z pytaniami! Upewnij się, że masz plik 'pytania.csv'.");
            notFound.showAndWait();
            Platform.exit();
            System.exit(0);
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader("prog.csv"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] strings = line.split(";");
                correctToPass = Integer.parseInt(strings[0]);
//                System.out.println(Arrays.toString(strings));
//                HashMap<String, Integer> answers = new HashMap<>();
//                int value;
//                if (strings.length > 0) {
//                    for (int i = 1; i <= 4; i++) {
//                        if (Integer.parseInt(strings[6]) == i)
//                            value = 1;
//                        else
//                            value = 0;
//                        answers.put(strings[i + 1], value);
//                    }
//                    Collections.shuffle(Collections.singletonList(answers));
//                    listQuestions.add(new Question(Integer.parseInt(strings[0]), strings[1], answers, Integer.parseInt(strings[6])));
//                }
            }
//            amountQuestion = listQuestions.size();
        } catch (IOException e) {
            Alert notFound = new Alert(Alert.AlertType.ERROR);
            notFound.setContentText("Nie można załadować pliku z progiem punktowym! Upewnij się, że masz plik 'prog.csv'.");
            notFound.showAndWait();
            Platform.exit();
            System.exit(0);
        }
    }


    void showQuestion(Label quesitonLabel, Label questionNum, Label questionNumber, ArrayList<Button> buttons) {
        question = listQuestions.get(0);
        quesitonLabel.setText(question.question);
        questionNumber.setText("Nr Pytania: " + question.numberQuestion);
        questionNum.setText("Pytanie " + questionIndex + "/" + amountQuestion);
        Collections.shuffle(Arrays.asList(question.answers));
        int i = 0;

        for (Map.Entry<String, Integer> entry : question.answers.entrySet()) {
//            System.out.println(entry.getKey());
//            System.out.println(buttons.get(i));
            buttons.get(i).setText(entry.getKey());
            i++;
        }
    }

    void start(Label quesitonLabel, Label questionNum, Label questionNumber, ArrayList<Button> buttons) {
        showQuestion(quesitonLabel, questionNum, questionNumber, buttons);

    }

    public void checkAnswer(String textAnswer) {
        for (Map.Entry<String, Integer> entry : question.answers.entrySet()) {
            if (entry.getKey().equals(textAnswer)) {
                questionsCorrect = questionsCorrect + entry.getValue();
            }
        }
        listQuestions.remove(0);
    }

    public void getNextQuestion(Label questionLabel, Label questionNum, Label questionNumber, ArrayList<Button> buttons) {
        if (!listQuestions.isEmpty()) {
            questionIndex++;
            showQuestion(questionLabel, questionNum, questionNumber, buttons);
        } else {
            try {
                saveResultToFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Alert result = new Alert(Alert.AlertType.INFORMATION);
            result.setHeaderText(username + "!");
            String information = "";
            if(correctToPass<=questionsCorrect) information = "Zdałeś!"; else information="Nie zdałeś";
            result.setContentText("Próg punktowy, aby zdać: "+correctToPass+"\nTwój wynik to: " + questionsCorrect + "/" + amountQuestion +"\n"+information);
            result.showAndWait();

            File file = new File("pliki\\" + username + ".ser");
            if (file.exists()) {
                file.delete();
            }
            Platform.exit();
            System.exit(0);
        }
    }

    private void saveResultToFile() throws IOException {
        File file = new File("ranking.dat");
        DataOutputStream out;
        if (file.exists()) {
            out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file, true)));
        } else {
            out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file, false)));
        }
        out.writeUTF(username);
        out.writeInt(questionsCorrect);
        out.writeInt(amountQuestion);

        out.close();
    }
}
