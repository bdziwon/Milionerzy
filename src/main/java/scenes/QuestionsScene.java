package scenes;

import controllers.NavigationController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import sql.DatabaseWorker;
import util.Question;
import util.Question;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class QuestionsScene implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private Button removeButton;


    //tabela z pytaniami
    @FXML
    private TableView<Question> questions;

    //kolumna z treścią
    @FXML
    private TableColumn<Question, String> questionColumn;

    //kolumna z odpowiedzią
    @FXML
    private TableColumn<Question, String> answerColumn;

    //kolumna z trudnością
    @FXML
    private TableColumn<Question, String> difficultyColumn;

    //dodawanie pytania
    @FXML
    private Button addButton;

    //lista załadowanych pytań
    private ArrayList<Question> questionArrayList;

    @FXML
    public void backButtonAction(ActionEvent event) {
        NavigationController.navigateTo("mainMenuScene.fxml",addButton.getScene(), false);
    }

    @FXML
    public void addButtonAction(ActionEvent event) {
        NavigationController.navigateTo("addQuestionScene.fxml",addButton.getScene(), false);
    }

    @FXML
    public void removeButtonAction(ActionEvent event) {

        //pobieranie wybranego pytania
        Question question = questions.getSelectionModel().getSelectedItem();

        //usuwanie wybranego pytania z bazy
        DatabaseWorker.getInstance().deleteQuestion(question);

        //tymczaszowa lista pytań
        ObservableList<Question> questionsData = FXCollections.observableArrayList();

        //usuwanie pytania z listy
        questionArrayList.remove(question);

        //ustawianie pytań po usunięciu do listy
        questionsData.setAll(questionArrayList);

        //ustawianie pytań po usunięciu do tabeli
        questions.setItems(questionsData);


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Question> questionsData = FXCollections.observableArrayList();

        //ładowanie pytań z bazy przy uruchomieniu
        questionArrayList = DatabaseWorker.getInstance().selectQuestions();
        questionsData.setAll(questionArrayList);



        //ustawanie koloru tekstu i tła oraz treści komórek
        questionColumn.setCellFactory(column -> {
                    return new TableCell<Question, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            setText(item);
                            setTextFill(Color.WHITE);
                            setStyle("-fx-background-color: transparent");
                        }
                    };
        });

        //ustawianie treści pytania dla komórki
        questionColumn.setCellValueFactory(new PropertyValueFactory<Question,String>("question"));

        //ustawianie treści odpowiedzi dla komórki
        answerColumn.setCellValueFactory(new PropertyValueFactory<Question,String>("answer"));

        //ustawianie poziomu trudności dla komórki
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<Question,String>("difficulty"));

        //wyświetlenie w tabeli
        questions.setItems(questionsData);
    }
}