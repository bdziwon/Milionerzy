package scenes;

import controllers.NavigationController;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Test;
import sql.DatabaseConnectionInfo;
import sql.DatabaseWorker;
import util.Question;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class GameSceneTest {


    @BeforeClass
    public static void initJFX() {
        Thread t = new Thread("JavaFX Init Thread") {
            public void run() {
                Application.launch(AsNonApp.class, new String[0]);
            }
        };
        t.setDaemon(true);
        t.start();
    }

    public static class AsNonApp extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {

        }
    }


    @Test
    public void getButtonWithAnswerShouldReturnOptionBButton() {
        String[] options = {"a","b","c","d"};
        Question question = new Question(0,"question","answer","1",options);
        Button optionAButton = new Button("A. d");
        Button optionBButton = new Button("B. answer");
        Button optionCButton = new Button("C. abcd");
        Button optionDButton = new Button("D. abcd");

        GameScene gameScene = new GameScene(question,optionAButton,optionBButton,optionCButton,optionDButton);

        assertEquals(optionBButton.getText(),gameScene.getButtonWithAnswer().getText());

    }

    @Test
    public void endGameShouldReturn0 (){
        DatabaseConnectionInfo dci
            = new DatabaseConnectionInfo("localhost","3306","root","");

        try {
            DatabaseWorker.getInstance().connect(dci);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        GameScene gameScene = new GameScene(0);
        String result = gameScene.endGame("GIVE UP");
        assertEquals(result, "0 zł");

    }


    @Test
    public void endGameShouldReturnMilion (){
        DatabaseConnectionInfo dci
                = new DatabaseConnectionInfo("localhost","3306","root","");

        try {
            DatabaseWorker.getInstance().connect(dci);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        GameScene gameScene = new GameScene(12);
        String result = gameScene.endGame("MILLION");
        assertEquals(result, "1 000 000 zł");

    }
}
