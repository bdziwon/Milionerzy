package scenes;

import controllers.NavigationController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sql.DatabaseWorker;
import util.Question;
import util.RuntimeDataHolder;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;


public class GameScene implements Initializable {

    //przycisk powrotu
    @FXML
    private Button  backButton;

    //Treść aktualnego pytania
    @FXML
    private Text    questionText;

    //odpowiedź A
    @FXML
    private Button  optionAButton;

    //odp. B
    @FXML
    private Button  optionBButton;

    //odp. C
    @FXML
    private Button  optionCButton;

    //odp. D
    @FXML
    private Button  optionDButton;

    //Prawy panel z progami nagród
    @FXML
    private VBox    prizesBox;

    //Tekst tytułowy o który aktualnie gramy
    @FXML
    private Text    titleText;

    //Gwarantowana nagroda którą otrzymamy po poprawnej odpowiedzi
    @FXML
    private String  nextGuaranteedPrize;

    //Aktualna gwarantowana nagroda
    @FXML
    private String  guaranteedPrize;

    //Przycisk "pół na pół"
    @FXML
    private Button  halfAnswersButton;

    //Przycisk "pomoc publiczności"
    @FXML
    private Button  peopleButton;

    //Przycisk "telefon do przyjaciela"
    @FXML
    private Button  phoneButton;

    //główny widok
    public      Stage       primaryStage;

    //numer aktualnego pytania
    private     int         currentLevel            = 0;

    //aktualne pytanie
    private     Question    currentQuestion         = null;

    //aktualny panel z nagrodą z prawego menu
    private     GridPane    currentGridPane         = null;

    //poprzedni panel z nagrodą z prawego menu
    private     GridPane    previousGridPane        = null;

    //zmienna, która wyłącza przyciski odpowiedzi
    private     boolean     disableButtonsActions   = false;

    public GameScene() {

    }

    public GameScene(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public GameScene(Question currentQuestion, Button optionAButton,
                     Button optionBButton, Button optionCButton, Button optionDButton) {
        this.currentQuestion = currentQuestion;
        this.optionAButton = optionAButton;
        this.optionBButton = optionBButton;
        this.optionCButton = optionCButton;
        this.optionDButton = optionDButton;

    }

    //Inicjalizacja kół ratunkowych i ładowanie pierwszego pytania
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //setShape: zmiana na okrągłe
        //Padding: powiększenie przycisków
        halfAnswersButton.setShape(new Circle(10));
        halfAnswersButton.setPadding(new Insets(8,15,8,15));
        peopleButton.setShape(new Circle(10));
        peopleButton.setPadding(new Insets(8,15,8,15));
        phoneButton.setShape(new Circle(10));
        phoneButton.setPadding(new Insets(8,15,8,15));

        //ładowanie pierwszego poziomu
        currentLevel = loadNextLevel(currentLevel);

    }

    //przycisk pomocy publiczności
    @FXML
    private void peopleButtonAction(ActionEvent event) {
        //wyłączenie tego koła ratunkowego
        peopleButton.setDisable(true);

        //Generator liczb
        Random rand = new Random(System.currentTimeMillis());

        //losowanie liczby od 49-99
        int percentage = rand.nextInt(50) + 50;

        //pewność odpowiedzi poprawnej, jest bardzej zmniejszana z każdym pytaniem
        int certainty = currentLevel * 2;

        //określanie procentów poprawnej odpowiedzi
        percentage = percentage - certainty;

        //przypisywanie procentów do poprawnej odpowiedzi
        ArrayList<Integer> chances = new ArrayList<>();

        chances.add(0,0);   //szansa na odpowiedź A
        chances.add(1,0);   //B
        chances.add(2,0);   //C
        chances.add(3,0);   //D

        //pobieranie przycisku z poprawną odpowiedzią.
        Button button = getButtonWithAnswer();

        //ustawianie procentu na poprawnej odpowiedzi
        if (button == optionAButton) {
            chances.set(0, percentage);

        } else if (button == optionBButton) {
            chances.set(1, percentage);

        } else if (button == optionCButton) {
            chances.set(2, percentage);

        } else if (button == optionDButton) {
            chances.set(3, percentage);

        }

        //obliczanie pozostałych procentów tak, żeby ich suma wynosiła 100
        int numberToSplit = 100 - percentage;
        int i = 0;

        for (i = 0; i<=3; i++) {
            int value = chances.get(i);

            //pomijamy różny od zera, bo to procent na poprawną odpowiedź i został już ustawiony
            if (value != 0) {
                continue;
            }
            value = rand.nextInt(numberToSplit);
            if (i == 3) {
                //wyrównywanie wartości ostatniego numeru żeby suma wynosiła 100
                int sum = chances.get(0)+chances.get(1)+chances.get(2)+value;
                value = value + (100 - sum);
            }

            chances.set(i,value);

            //pozostała wartość do podzielenia
            numberToSplit = numberToSplit - value;
        }

        String result = "Pomoc publiczności: \n" +
                "Odpowiedź A: "+chances.get(0) + "\n" +
                "Odpowiedź B: "+chances.get(1) + "\n" +
                "Odpowiedź C: "+chances.get(2) + "\n" +
                "Odpowiedź D: "+chances.get(3) + "\n";

        //Wyświetlenie wyniku
        new Alert(Alert.AlertType.INFORMATION,result).showAndWait();
    }


    // Akcja przycisku pomocy od przyjaciela
    @FXML
    private void phoneButtonAction(ActionEvent event) {

        //wyłączenie koła
        phoneButton.setDisable(true);

        //aktualna szansa na poprawną odpowiedź od znajomego
        //przy pierwszym pytaniu wynosi 99%, przy ostatnim 88%;
        double chancesToProperAnswer = 1.0d - currentLevel * 0.01d;

        Random rand = new Random(System.currentTimeMillis());

        //sprawdzanie czy pokazać poprawną czy losową odpowiedź
        if ((rand.nextDouble()) < chancesToProperAnswer) {

            //odpowiedźpoprawna

            String answer = "";
            Button buttonWithAnswer = getButtonWithAnswer();

            if (buttonWithAnswer == optionAButton) {
                answer = "A";
            } else if (buttonWithAnswer == optionBButton) {
                answer = "B";
            } else if (buttonWithAnswer == optionCButton) {
                answer = "C";
            } else {
                answer = "D";
            }

            answer = "Twój znajomy myśli, że poprawna odpowiedź to odpowiedź "+answer;
            new Alert(Alert.AlertType.INFORMATION,answer).showAndWait();

        } else {

            //odpowiedź losowa

            ArrayList<String> options = new ArrayList<>();
            options.add(0,"A");
            options.add(1, "B");
            options.add(2, "C");
            options.add(3, "D");

            //losowanie odpowiedzi z listy
            int index = rand.nextInt(4);

            String answer = "Twój znajomy myśli, że poprawna odpowiedź to odpowiedź "+options.get(index);
            new Alert(Alert.AlertType.INFORMATION,answer).showAndWait();

        }

    }

    //koło podziału odpowiedzi na pół
    @FXML
    private void halfAnswersButtonAction(ActionEvent event) {

        //wyłączenie koła po użyciu
        halfAnswersButton.setDisable(true);

        //lista określająca które odpowiedzi można wykluczyć
        ArrayList<Integer> canBeDisabled = new ArrayList<Integer>();
        canBeDisabled.add(0);
        canBeDisabled.add(1);
        canBeDisabled.add(2);
        canBeDisabled.add(3);

        Button answerButton = getButtonWithAnswer();

        //usuwanie z listy odpowiedzi do wykluczenia odpowiedzi poprawnej
        if (answerButton == optionAButton) {
            canBeDisabled.remove(0);

        } else if (answerButton == optionBButton) {
            canBeDisabled.remove(1);

        } else if (answerButton == optionCButton) {
            canBeDisabled.remove(2);

        } else if (answerButton == optionDButton) {
            canBeDisabled.remove(3);
        }


        Random random = new Random();


        //wyłączanie dwóch odpowiedzi losowo z trzech
        for (int i = 1; i <= 2; i++) {
            int removeIndex = random.nextInt(canBeDisabled.size());
            int number = canBeDisabled.get(removeIndex);
            canBeDisabled.remove(removeIndex);
            switch (number) {
                case 0:
                    optionAButton.setVisible(false);
                    break;
                case 1:
                    optionBButton.setVisible(false);
                    break;
                case 2:
                    optionCButton.setVisible(false);
                    break;
                case 3:
                    optionDButton.setVisible(false);
                    break;
            }
        }

    }

    @FXML
    private void backButtonAction(ActionEvent event) {
        //koniec gry przez rezygnację
        String prize = endGame("GIVE UP");
        new Alert(Alert.AlertType.INFORMATION,"Wygrano "+prize+"!").showAndWait();
        NavigationController.navigateUp(backButton.getScene());


    }

    //ładowanie następnego poziomu
    public int loadNextLevel(int currentLevel) {

        Question    question;
        GridPane    gridPane;
        String      currentPrize;

        //ustawianie wszystkich przycisków odpowiedzi na widoczne, bo po "pół na pół" mogą być ukryte;
        optionAButton.setVisible(true);
        optionBButton.setVisible(true);
        optionCButton.setVisible(true);
        optionDButton.setVisible(true);


        //jeśli odpowiedzieliśmy na ostatnie pytanie, to wygraliśmy milion
        if (currentLevel >= 12) {
            String prize = endGame("MILLION");
            new Alert(Alert.AlertType.INFORMATION,"Gratulacje, wygrano milion złotych!").showAndWait();
            NavigationController.navigateUp(backButton.getScene());


            return 0;
        }

        currentLevel++;

        //pobieranie losowego pytania z bazy o danym poziomie
        question = DatabaseWorker.getInstance().getRandomQuestionWithDifficulty(currentLevel);

        if (question == null) {
            new Alert(Alert.AlertType.INFORMATION,"Błąd, dodaj pytanie na każdy poziom trudności!").showAndWait();

            NavigationController.navigateUp(backButton.getScene());

        }

        currentQuestion = question;

        //ustawianie treści przycisków odpowiedzi na podstawie pobranego pytania
        optionAButton.setText("A. "+question.getOptionA());
        optionBButton.setText("B. "+question.getOptionB());
        optionCButton.setText("C. "+question.getOptionC());
        optionDButton.setText("D. "+question.getOptionD());

        //ustawianie treści pytania
        questionText.setText(question.getQuestion());

        //wyszukiwanie aktualnego pytania i ustawianie jego tła na prawym panelu
        gridPane = (GridPane) prizesBox.lookup("#prize"+Integer.toString(currentLevel));
        gridPane.setStyle("-fx-background-color: rgba(255,152,16,70);");

        //pobieranie kolejnej nagrody i ustawianie na nagłówek
        currentPrize = ((Text)gridPane.lookup("#prize")).getText();
        titleText.setText("Pytanie o "+currentPrize+"!");

        //progi do zmiany gwarantowanej nagrody
        if (currentLevel == 2 || currentLevel == 7) {
            nextGuaranteedPrize = currentPrize;
        }

        //czyszczenie zaznaczenia z poprzedniej nagrody po prawej stronie
        if (currentGridPane != null) {
            currentGridPane.setStyle("");
        }
        previousGridPane    = currentGridPane;
        currentGridPane     = gridPane;

        return currentLevel;
    }


    //akcja przycisków odpowiedzi
    private void answerButtonAction(Button button) {

        //jeśli przyciski są wyłączone to wyjdź
        if (disableButtonsActions == false) {
            disableButtonsActions = true;
        } else {
            return;
        }

        //jeśli wybrana odpowiedź jest poprawna
        if (currentQuestion.getAnswer().equals(button.getText().substring(3))) {
            guaranteedPrize = nextGuaranteedPrize;

            //podświetlenie odpowiedzi na dwie sekundy
            button.setStyle("-fx-background-color: rgba(255,152,16,70);");
            new Thread(() -> {
                try {
                    //czekanie 2s
                    Thread.sleep(2000);
                    Platform.runLater(() -> {
                        //usunięcie podświetlenia
                        button.setStyle("-fx-background-color:  rgb(0,39,105); -fx-background-radius: 15;");
                        currentLevel = loadNextLevel(currentLevel);
                        disableButtonsActions = false;

                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();


        }  else {
            //jeśli odpowiedź jest błędna, podświetlanie błędnej i poprawnej odpowiedzi na 2 sekundy
            button.setStyle("-fx-background-color: rgba(255,20,20,70);");
            Button answerButton = getButtonWithAnswer();
            answerButton.setStyle("-fx-background-color: rgba(255,152,16,70);");

            new Thread(() -> {
                try {
                    Thread.sleep(2000);

                    //zakończenie gry przez złą odpowiedź
                    Platform.runLater(() -> {
                        String prize = endGame("WRONG ANSWER");
                        new Alert(Alert.AlertType.INFORMATION,"Wygrana: gwarantowane "+prize).showAndWait();

                        NavigationController.navigateUp(backButton.getScene());



                    });
                    disableButtonsActions = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }


    //zwraca przycisk z poprawną odpowiedzią.
    public Button getButtonWithAnswer() {

        if (currentQuestion.getAnswer().equals(optionAButton.getText().substring(3))) {
            return optionAButton;

        } else if (currentQuestion.getAnswer().equals(optionBButton.getText().substring(3))) {
            return optionBButton;

        } else if (currentQuestion.getAnswer().equals(optionCButton.getText().substring(3))) {
            return optionCButton;

        } else if (currentQuestion.getAnswer().equals(optionDButton.getText().substring(3))) {
            return optionDButton;

        }
        return null;
    }

    //kończenie gry
    public String endGame(String status) {

        //pobieranie wcześniej podanej nazwy gracza
        String  player  =   RuntimeDataHolder.getInstance().getPlayerName();
        String  prize   =   null;

        //jeśli wygrano milion
        if (status.equals("MILLION")) {
            prize = "1 000 000 zł";
        } else

        //jeśli podano złą odpowiedź
        if (status.equals("WRONG ANSWER")) {

            //przed gwarantowaną nagrodą:
            if (guaranteedPrize == null) {
                prize = "0 zł";

            } else {
                //po gwarantowanej nagrodzie:
                prize = guaranteedPrize;
            }

            //wyjście ze sceny gry
        } else

        //jeśli się poddano
        if (status.equals("GIVE UP")) {
            if (previousGridPane != null) {
                //wygrana z ostatniego pytania
                prize = ((Text)previousGridPane.lookup("#prize")).getText();

            } else {
                //przy pierwszym pytaniu, wygrana to 0zł
                prize = "0 zł";
            }
        }

        //dodawanie wyniku do bazy danych
        DatabaseWorker.getInstance().addHighScore(player, prize);

        return prize;

    }

    @FXML
    private void optionAButtonAction(ActionEvent event) {
        answerButtonAction(optionAButton);
    }

    @FXML
    private void optionBButtonAction(ActionEvent event) {
        answerButtonAction(optionBButton);
    }

    @FXML
    private void optionCButtonAction(ActionEvent event) {
        answerButtonAction(optionCButton);
    }

    @FXML
    private void optionDButtonAction(ActionEvent event) {
        answerButtonAction(optionDButton);
    }



}