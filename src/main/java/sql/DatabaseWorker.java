package sql;

import util.Highscore;
import util.Question;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;


//klasa do obsługi bazy danych
public class DatabaseWorker {


    private static DatabaseWorker db = null;

    //zmienna przetrzymująca aktualne połączenie
    private Connection connection = null;

    //informacje o połączeniu
    private DatabaseConnectionInfo connectionInfo = null;


    private DatabaseWorker() {
    }

    //pobieranie instancji klasy
    public static DatabaseWorker getInstance() {
        if (db == null) {
            db = new DatabaseWorker();
            return db;
        } else {
            return db;
        }
    }


    //łaczenie z bazą, należy podać dane bazy
    public Connection connect(DatabaseConnectionInfo connectionInfo) throws IllegalArgumentException, SQLException {
        if (this.connection != null) {
            System.out.println("DatabaseWorker.connect: Już połączono");
            return this.connection;
        }

        Connection      connection;
        String          link;
        String          username = connectionInfo.getUsername();
        String          password = connectionInfo.getPassword();

        //tworzenie linku do łączenia z bazą db
        link = connectionInfo.makeDbLink();

        //wczytywanie sterownika
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            //próba połączenia z bazą db
            connection = DriverManager.getConnection(link, username, password);
        } catch (SQLException e) {

            System.out.println("DatabaseWorker.connect: Łączenie bo bazy nieudane, "+e.getMessage());

            //jeśli baza nie istnieje, próbujemy ją stworzyć
            System.out.println("DatabaseWorker.connect: Próba utworzenia bazy..");
            link = connectionInfo.makeConnectLink();

            try {
                //próba połączenia bez podawania bazy
                connection = DriverManager.getConnection(link, username, password);

                //tworzenie bazy
                connection.createStatement()
                        .executeUpdate("CREATE DATABASE DB CHARACTER SET utf8 COLLATE utf8_polish_ci;");

                //wybieranie utworzonej bazy
                connection.createStatement().executeUpdate("USE DB");

            } catch (SQLException e2) {
                System.out.println("DatabaseWorker.connect: Tworzenie bazy nieudane, "+e2.getMessage());
                return null;
            }
        }

        if (connection == null) {
            return null;
        }

        System.out.println("DatabaseWorker.connect: Połączenie udane");

        this.connectionInfo = connectionInfo;
        this.connection = connection;
        return connection;
    }


    //tworzenie tabel w bazie
    public void createTablesIfDoesNotExists() {

        String sql = "``";

        if (connection == null) {
            System.out.println("DatabaseWorker.createTablesIfDoesNotExists: Brak połączenia, anulowanie");
            return;
        }

        try {
            //tworzenie tabeli z pytaniami
            //options to odpowiedzi w formie: "odpowiedzA,odpowiedzB,odpowiedzC,odpowiedzD"
            //response to numer odpowiedzi od 0 do 3
            //text to tresc pytania
            //difficulty to poziom trudnosci pytania od 1 do 12, gdzie 12 to pytanie za milion
            sql =
                    "CREATE TABLE question (" +
                            "id             INT             NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                            "`text`         VARCHAR(150)    DEFAULT '' NOT NULL," +
                            "options        VARCHAR(100)    DEFAULT '' NOT NULL," +
                            "difficulty     INT             DEFAULT 1 NOT NULL," +
                            "response       INT             DEFAULT 0 NOT NULL)";

            //wykonywanie kodu sql
            connection.createStatement().executeUpdate(sql);


            //dodawanie domyślnych pytań do bazy
            //1

            sql =   "INSERT INTO question(text,options,difficulty,response) " +
                    "VALUES ('\"...Ojczyzno moja, ty jesteś jak zdrowie\" - o jakiej ojczyźnie pisał Adam Mickiewicz','Białorusi,Rosji,Litwie,Ukrainie','1','2')";
            connection.createStatement().executeUpdate(sql);

            sql =   "INSERT INTO question(text,options,difficulty,response) " +
                    "VALUES ('Cydr wytwarzany jest z:','wiśni,zboża,jabłek,marchwi','1','2')";
            connection.createStatement().executeUpdate(sql);

            sql =   "INSERT INTO question(text,options,difficulty,response) " +
                    "VALUES ('Jak w matematyce nazywa się kąt większy niż 90 stopni?','ostry,pełny,rozwarty,wypukły','1','2')";
            connection.createStatement().executeUpdate(sql);

            sql =   "INSERT INTO question(text,options,difficulty,response) " +
                    "VALUES ('Jak nazywa się zasłona w teatrze oddzielająca scenę od widowni?','portiera,kurtyna,parawan,kurtuazja','1','1')";
            connection.createStatement().executeUpdate(sql);

            //2

            sql =   "INSERT INTO question(text,options,difficulty,response) " +
                    "VALUES ('Który z amerykańskich stanów leży najbliżej Rosji?','Alaska,Nowy Jork,Kalifornia,Floryda','2','0')";
            connection.createStatement().executeUpdate(sql);

            sql =   "INSERT INTO question(text,options,difficulty,response) " +
                    "VALUES ('Gody ryb to:','tarło,ruja,ciągi,toki','2','0')";
            connection.createStatement().executeUpdate(sql);

            sql =   "INSERT INTO question(text,options,difficulty,response) " +
                    "VALUES ('Program wyświetlający reklamy, będący jednocześnie darmowym, jest oparty na licencji:','GPL,adware,trial,shareware','2','1')";
            connection.createStatement().executeUpdate(sql);

            //3

            sql =   "INSERT INTO question(text,options,difficulty,response) " +
                    "VALUES ('Miękka czapka wojskowa bez daszka, podłużnie składana to:','maciejówka,rogatywka,furażerka,panama','3','2')";
            connection.createStatement().executeUpdate(sql);

            sql =   "INSERT INTO question(text,options,difficulty,response) " +
                    "VALUES ('Jurand ze spychowa jest bohaterem powieści Henryka Sienkiewicza:','Quo Vadis,Potop,W pustyni i w puszczy,Krzyżacy','3','3')";
            connection.createStatement().executeUpdate(sql);

            sql =   "INSERT INTO question(text,options,difficulty,response) " +
                    "VALUES ('Waluta, którą posługują się Austriacy to:','Frank,Euro,Marka,Szyling','3','1')";
            connection.createStatement().executeUpdate(sql);

            //4

            sql =   "INSERT INTO question(text,options,difficulty,response) " +
                    "VALUES ('Z jabłek produkuje się wódkę zwaną:','calvados,cydr,brandy,arak','4','0')";
            connection.createStatement().executeUpdate(sql);

            sql =   "INSERT INTO question(text,options,difficulty,response) " +
                    "VALUES ('Która z wymienionych planet ma średnicę najbardziej zbliżoną do Ziemi?','Mars,Wenus,Merkury,Uran','4','1')";
            connection.createStatement().executeUpdate(sql);

            //5

            sql =   "INSERT INTO question(text,options,difficulty,response) " +
                    "VALUES ('Jakie zwierzę jest symbolem Kampinoskiego Parku Narodowego?','żubr,łoś,wilk,niedźwiedź','5','1')";
            connection.createStatement().executeUpdate(sql);

            //6

            sql =   "INSERT INTO question(text,options,difficulty,response) " +
                    "VALUES ('Z ilu wersów składa się sonet?','2,5,14,22','6','2')";
            connection.createStatement().executeUpdate(sql);

            //7

            sql =   "INSERT INTO question(text,options,difficulty,response) " +
                    "VALUES ('Znana polska himilaistka, Wanda Rutkiewicz-Błaszczykiewicz, zaginęła 13 maja 1992 podczas wspinaczki na:','Lhotse,K2,Makalu,Kanczendzongę','7','3')";
            connection.createStatement().executeUpdate(sql);

            //8

            sql =   "INSERT INTO question(text,options,difficulty,response) " +
                    "VALUES ('Radio Solidarność swoją pierwszą audycję nadało w roku:','1978,1986,1990,1982','8','3')";
            connection.createStatement().executeUpdate(sql);

            //9

            sql =   "INSERT INTO question(text,options,difficulty,response) " +
                    "VALUES ('Układ z roku 1993 pomiędzy USA a Rosją dotyczący ograniczenia ilości głowic nuklearnych nosił nazwę:','START II,START I,SALT II,SALT I','9','0')";
            connection.createStatement().executeUpdate(sql);

            //10

            sql =   "INSERT INTO question(text,options,difficulty,response) " +
                    "VALUES ('\"Przygody Tintina\" to komiks rysowany przez artystę pochodzenia: ','belgijskiego,francuskiego,brytyjskiego,hiszpańskiego','10','0')";
            connection.createStatement().executeUpdate(sql);

            //11

            sql =   "INSERT INTO question(text,options,difficulty,response) " +
                    "VALUES ('Jak się nazywa przyrząd trzymany przez pomnik przedstawiający Mikołaja Kopernika w Warszawie?','Astrolabium,Altazymut,Kolidar,Heliometr','11','0')";
            connection.createStatement().executeUpdate(sql);

            //12
            //pytanie za milion

            sql =   "INSERT INTO question(text,options,difficulty,response) " +
                    "VALUES ('Które z żeber licząc od góry, należy do tak zwanych żeber rzekomych?','szóste,czwarte,dziewiąte,piąte','12','2')";

            connection.createStatement().executeUpdate(sql);

            //tworzenie tabeli z wynikami
            sql =
                    "CREATE TABLE highscore (" +
                            "id             INT             NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                            "player         VARCHAR(150)    DEFAULT '' NOT NULL," +
                            "prize          VARCHAR(100)    DEFAULT '' NOT NULL)";

            connection.createStatement().executeUpdate(sql);



        } catch (SQLException e) {
            //jeśli jakaś tabela już istnieje to nie nadpisujemy jej
            if (!e.toString().contains("exists")) {
                e.printStackTrace();
            }
            System.out.println("DatabaseWorker.createTablesIfDoesNotExists: "+e.getMessage());

        }


    }

    public DatabaseConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    //dodawanie wyniku do tabeli
    public int addHighScore(String playerName, String prize) {

        String  sql =   "INSERT INTO highscore(player,prize) " +
                        "VALUES('"+playerName+"','"+prize+"')";

        try {
            connection.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    //dodawanie pytania do tabeli
    public int addQuestion(String text, String option0, String option1,
                            String option2, String option3,int difficulty, String response){

        //options musi być w formacie: "odpA,odpB,odpC,odpD"
        String  options             =   option0 + "," + option1 + "," + option2 + "," + option3;

        String  sql                 =   null;
        int     responseNumber      =   0;
        int     difficultyNumber    =   difficulty;

        //Zmiana odpowiedzi w postaci litery na liczbę
        switch (response) {
            case "A":
                responseNumber = 0;
                break;
            case "B":
                responseNumber = 1;
                break;
            case "C":
                responseNumber = 2;
                break;
            case "D":
                responseNumber = 3;
                break;
        }

        sql  =  "INSERT INTO question(text,options,difficulty,response) " +
                "VALUES ('"+text+"','"+options+"','"+difficultyNumber+"','"+responseNumber+"')";

        try {
            //dodawanie pytania
            connection.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;

    }

    //pobieranie wyników z bazy
    public ArrayList<Highscore> selectHighscores() {

        ResultSet   results;
        try {
            results = connection.createStatement().executeQuery("SELECT * FROM highscore ORDER BY prize DESC");
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<Highscore>();
        }

        ArrayList<Highscore> highscores = new ArrayList<>();


        //wczytywanie pobranych do listy
        try {
            while (results.next()) {
                //pobieranie kolumn z nazwą gracza i z nagrodą
                String      player      =   results.getString("player");
                String      prize       =   results.getString("prize");
                Highscore   highscore    =
                        new Highscore(player, prize);

                highscores.add(highscore);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return highscores;
        }


        return highscores;

    }

    //pobieranie pytań z bazy
    public ArrayList<Question> selectQuestions() {

        ResultSet results;

        try {
            results = connection.createStatement().executeQuery("SELECT * FROM question ORDER BY difficulty DESC");
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<Question>();
        }

        ArrayList<Question> questions = new ArrayList<>();

        try {
            while (results.next()) {
                //pobieranie konkretnych kolumn tabeli do zmiennych
                int         id          =   results.getInt("id");
                String      text        =   results.getString("text");
                String      options     =   results.getString("options");
                int         difficulty  =   results.getInt("difficulty");
                int         response    =   results.getInt("response");
                String      answer      =   options.split(",")[response];
                Question    question    =
                        new Question(id,text,answer,Integer.toString(difficulty),options.split(","));

                questions.add(question);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return questions;
        }


        return questions;
    }


    //usuwanie danego pytania
    public int deleteQuestion(Question question) {
        String      sql     =   "DELETE from question where id = "+question.getId();
        int         status  =   0;
        try {
            status = connection.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return status;
    }

    //pobieranie losowego pytania o danym poziomie trudności
    public Question getRandomQuestionWithDifficulty(int difficulty) {

        //losowanie pytania
        String      sql     =   "SELECT * FROM question WHERE difficulty="+difficulty+" ORDER BY RAND() LIMIT 1";
        try {
            ResultSet   results     = connection.createStatement().executeQuery(sql);
            Question    question = null;

            //wczytywanie pytania do obiektu Question
            while (results.next()) {
                int         id          =   results.getInt("id");
                String      text        =   results.getString("text");
                String      options     =   results.getString("options");
                int         response    =   results.getInt("response");
                String      answer      =   options.split(",")[response];

                question    =   new Question(id,text,answer,Integer.toString(difficulty),options.split(","));
            }

            return question;

        } catch (SQLException e) {
            e.printStackTrace();
            return  null;
        }
    }
}
