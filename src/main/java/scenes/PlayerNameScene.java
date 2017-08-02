package scenes;

import controllers.NavigationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.RuntimeDataHolder;

//klasa z ustawianiem nazwy gracza
public class PlayerNameScene {

    //przechodzenie do gry
    @FXML
    private Button      playButton;

    @FXML
    private Button      backButton;

    //pole z nazwą gracza
    @FXML
    public  TextField   playerNameField;

    public  Stage       primaryStage;



    @FXML
    private void playButtonAction(ActionEvent event) {

        String newPlayerName = playerNameField.getText();

        //jeśli podana nazwa gracza jest krótsza od 1, to należy to poprawić
        if (newPlayerName == null || newPlayerName.length() < 1) {
            new Alert(Alert.AlertType.INFORMATION,"Podaj nazwę gracza!").showAndWait();
            return;
        }

        //tymczasowe zapisanie nazwy gracza i
        RuntimeDataHolder.getInstance().setPlayerName(newPlayerName);
        NavigationController.navigateTo("GameScene.fxml",playButton.getScene(),false);

    }

    @FXML
    private void backButtonAction(ActionEvent event) {
        NavigationController.navigateTo("mainMenuScene.fxml",backButton.getScene(),false);

    }
}