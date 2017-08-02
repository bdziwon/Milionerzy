package sql;

import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Test;
import scenes.GameSceneTest;
import util.Question;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Created by Bartek on 05.06.2017.
 */
public class DatabaseWorkerTest {

    @BeforeClass
    public static void initJFX() {
        Thread t = new Thread("JavaFX Init Thread") {
            public void run() {
                Application.launch(GameSceneTest.AsNonApp.class, new String[0]);
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
    public void connectAndTablesShouldNotThrowException() {
        MyAssertions.assertDoesNotThrow(() -> {
            DatabaseConnectionInfo dci
                    = new DatabaseConnectionInfo("localhost","3306","root","");
            DatabaseWorker.getInstance().connect(dci);
        });

        DatabaseWorker.getInstance().createTablesIfDoesNotExists();

    }

    @Test
    public void addHighScoreShouldReturn0 () {
        DatabaseConnectionInfo dci
                = new DatabaseConnectionInfo("localhost","3306","root","");
        try {
            DatabaseWorker.getInstance().connect(dci);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int result = DatabaseWorker.getInstance().addHighScore("player", "test zł");
        assertEquals(result,0);
    }

    @Test
    public void addQuestionTest() {
        DatabaseConnectionInfo dci
                = new DatabaseConnectionInfo("localhost","3306","root","");
        try {
            DatabaseWorker.getInstance().connect(dci);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseWorker.getInstance().addQuestion("test","testa","testb","testc","testd",13,"testc");
        Question q = DatabaseWorker.getInstance().getRandomQuestionWithDifficulty(13);
        assertEquals(q.getDifficulty(),"13");
        assertEquals(DatabaseWorker.getInstance().deleteQuestion(q),1);
        q = DatabaseWorker.getInstance().getRandomQuestionWithDifficulty(13);
        assertEquals(q.getAnswer(),"testa");
    }


}
