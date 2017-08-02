package scenes;

import controllers.NavigationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sql.DatabaseWorker;

import java.net.URL;
import java.util.ResourceBundle;


public class AddQuestionScene  implements Initializable {

    //przycisk powrotu
    @FXML
    private Button backButton;

    //przycisk dodawania pytania
    @FXML
    private Button addButton;

    //wybieranie poprawnej odpowiedzi
    @FXML
    private ChoiceBox<String> responseChoiceBox;

    //wybieranie trudności pytania (numeru w którym może być wylosowane)
    @FXML
    private ChoiceBox<Integer> difficultyChoiceBox;

    //miejsce na wpisanie pytania
    @FXML
    private TextField questionTextField;

    //miejsce na wpisanie opcji A
    @FXML
    private TextField optionATextField;

    //miejsce na wpisanie opcji B
    @FXML
    private TextField optionBTextField;

    //miejsce na wpisanie opcji C
    @FXML
    private TextField optionCTextField;

    //miejsce na wpisanie opcji D
    @FXML
    private TextField optionDTextField;


    //akcja przycisku powrotu
    @FXML
    public void backButtonAction(ActionEvent event)  {
        //Nawigacja do wyświetlania pytań
        NavigationController.navigateTo("QuestionsScene.fxml",addButton.getScene(),false);
    }

    //akcja przycisku dodawania
    @FXML
    public void addButtonAction(ActionEvent event) {

        //odczytywanie wpisanego tekstu
        String  text        =   questionTextField.getText();
        String  option0     =   optionATextField.getText();
        String  option1     =   optionBTextField.getText();
        String  option2     =   optionCTextField.getText();
        String  option3     =   optionDTextField.getText();
        String  response    =   responseChoiceBox.getValue();
        Integer difficulty  =   difficultyChoiceBox.getValue();

        //sprawdzanie czy wszystkie pola są wypełnione
        if (option0.length() == 0 || option1.length() == 0 || option2.length() ==  0 || option3.length() == 0
                || response == null || difficulty == null) {

            //jeśli nie, należy to poprawić
            new Alert(Alert.AlertType.INFORMATION,"Uzupełnij wszystkie pola.").showAndWait();
            return;
        }

        //jeśli tak, dodajemy pytanie
        int status = DatabaseWorker.getInstance().addQuestion(text,option0,option1,option2,option3,difficulty,response);

        if (status == -1) {
            new Alert(Alert.AlertType.ERROR,"Wystąpił błąd podczas dodawania pytania.").showAndWait();
        } else
        if (status == 0) {
            new Alert(Alert.AlertType.INFORMATION,"Pytanie dodane pomyślnie").showAndWait();
            NavigationController.navigateTo("QuestionsScene.fxml",addButton.getScene(),false);
        }
    }

    //Inicjalizacja wyboru odpowiedzi i poziomu trudności
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //dodanie opcji do wyboru poprawnej odpowiedzi i poziomu trudności
        responseChoiceBox.getItems().addAll("A","B","C","D");
        difficultyChoiceBox.getItems().addAll(1,2,3,4,5,6,7,8,9,10,11,12);

    }



}