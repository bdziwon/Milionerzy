package controllers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sql.DatabaseConnectionInfo;
import sql.DatabaseWorker;
import util.RuntimeDataHolder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

/**
 * Klasa do nawigacji między scenami i główna klasa uruchomieniowa
 */
public class NavigationController extends Application {

    //kolejka scen do cofania do poprzednich
    private static Deque<Scene> scenes = new ArrayDeque<Scene>();

    public static void main(String[] args) {
        launch(args);
    }


    //główny widok na którym zmieniamy sceny
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {

        //element główny sceny
        GridPane root = new GridPane();

        //tytuł i brak możliwości zmiany rozmiaru okna
        primaryStage.setTitle("Milionerzy");
        primaryStage.setResizable(false);

        //ładowanie menu głównego
        try {
            root = (GridPane) FXMLLoader.load(getClass().getClassLoader()
                    .getResource("MainMenuScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        //tworzenie sceny z danym elementem
        Scene scene = new Scene(root);

        //dodawanie stylów do wyglądu przycisków itd
        scene.getStylesheets().addAll(
                getClass().getClassLoader().getResource("styles.css").toExternalForm()
        );

        //wyświetlenie i wyśrodkowanie sceny
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.centerOnScreen();

        //wyjście przez naciśnięcie krzyżyka
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                exitApp();
            }
        });

        //zapisanie głównego widoku
        this.primaryStage = primaryStage;

        //logowanie do bazy, podawanie danych
        DatabaseConnectionInfo dci
                = new DatabaseConnectionInfo("localhost","3306","root","");

        //łączenie z bazą
        try {
            DatabaseWorker.getInstance().connect(dci);
        } catch (SQLException e) {
            System.out.println("database connection failed");
            exitApp();
        }

        //tworzenie tabel jeśli nie istnieją
        DatabaseWorker.getInstance().createTablesIfDoesNotExists();
    }

    /**
     * Nawigacja do innej sceny o danej nazwie
     * @param sceneFXML nazwa sceny
     * @param scene     aktualna scena
     * @param centered  czy wyśrodkować na ekranie
     */
    public static void navigateTo(String sceneFXML, Scene scene, boolean centered) {

        //Aktualny widok i scena
        Stage  stage         = (Stage) scene.getWindow();
        Scene  currentScene  = stage.getScene();

        Parent root          = null;

        //dodajemy aktualną scenę do kolejki jakbśmy chcieli do niej wrócić
        scenes.push(currentScene);

        //ładowanie nowej sceny
        try {
            root = (GridPane) FXMLLoader.load(NavigationController.class.getClassLoader()
                    .getResource(sceneFXML));
        } catch (IOException e) {
        }

        //wyświetlenie nowej sceny
        try {
            scene = new Scene(root);

        } catch (Exception e) {

        }

        //dodawanie styli
        scene.getStylesheets().addAll(
                NavigationController.class
                        .getClassLoader()
                        .getResource("styles.css").toExternalForm()
        );

        //ustawianie sceny na widok
        stage.setScene(scene);

        //wyśrodkowanie
        if (centered) {
            stage.centerOnScreen();
        }
    }

    /**
     * nawigacja do poprzedniej sceny
     * @param scene aktualna scena
     */
    public static void navigateUp(Scene scene) {

        if (scene == null) {
            return;
        }
        //pobranie aktualnego widoku
        Stage   stage = (Stage) scene.getWindow();

        try {
            //zdjęcie sceny ze stosu
            scene = scenes.pop();
        } catch (NoSuchElementException e) {
            return;
        }

        //ustawienie sceny na widok
        stage.setScene(scene);


    }

    /**
     * zakończenie aplikacji
     */
    public static void exitApp() {
        Platform.exit();
    }
}