package scenes;

import controllers.NavigationController;
import javafx.application.Platform;
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
import javafx.stage.Stage;
import sql.DatabaseWorker;
import util.Highscore;
import util.Question;

import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;


//klasa z wynikami gier
public class HighscoreScene implements Initializable {

    //przycisk powrotu
    @FXML
    private Button backButton;

    //tabela zawierająca wyniki
    @FXML
    private TableView<Highscore> highscores;

    //kolumna z nazwą gracza
    @FXML
    private TableColumn<Highscore, String> playerColumn;

    //kolumna z nagrodą
    @FXML
    private TableColumn<Highscore, String> prizeColumn;

    @FXML
    public void backButtonAction(ActionEvent event) {
        NavigationController.navigateTo("mainMenuScene.fxml",backButton.getScene(), false);

    }

    //lista z wynikami
    private ArrayList<Highscore> highscoreArrayList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //ładowanie wyników z bazy do tabeli
        //tymczasowa lista
        ObservableList<Highscore> highscoreData = FXCollections.observableArrayList();

        //pobieranie z bazy i ustawianie
        highscoreArrayList = DatabaseWorker.getInstance().selectHighscores();
        highscoreData.setAll(highscoreArrayList);


        //zmiana stylu komórek na biały tekst, i ustawianie tła i treści
        playerColumn.setCellFactory(column -> {
            return new TableCell<Highscore, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    //treść
                    setText(item);

                    //kolor
                    setTextFill(Color.WHITE);

                    //tło
                    setStyle("-fx-background-color: transparent");
                }
            };
        });

        //określa jaką wartość ustawiać do kolumny nazwy gracza
        playerColumn.setCellValueFactory(new PropertyValueFactory<Highscore,String>("player"));

        //określa jaką wartość ustawiać do kolumny nagrody
        prizeColumn.setCellValueFactory(new PropertyValueFactory<Highscore,String>("prize"));

        //wyświetlanie
        highscores.setItems(highscoreData);
    }
}