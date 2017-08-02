package scenes;

import controllers.NavigationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class MainMenuScene  {

    //rozpoczęcie nowej gry
    @FXML
    private Button newGameButton;

    //najlepsze wyniki
    @FXML
    private Button highscoreButton;

    //przycisk wyjścia
    @FXML
    private Button exitButton;

    //przycisk edycji pytań
    @FXML
    private Button questionsButton;

    public Stage primaryStage;

    @FXML
    private void newGameButtonAction(ActionEvent event) {
        NavigationController.navigateTo("PlayerNameScene.fxml", newGameButton.getScene(), false);

    }

    @FXML
    private void highscoreButtonAction(ActionEvent event) {
        NavigationController.navigateTo("HighscoreScene.fxml", highscoreButton.getScene(), false);
    }

    @FXML
    private void questionsButtonAction(ActionEvent event) {
        NavigationController.navigateTo("QuestionsScene.fxml", questionsButton.getScene(), false);
    }

    @FXML
    private void exitButtonAction(ActionEvent event) {
        NavigationController.exitApp();
    }

}