package sql;

import java.sql.DriverManager;


//klasa z danymi do logowania do bazy
public class DatabaseConnectionInfo {


    private String hostname = "localhost";
    private String port = "3306";
    private String username = "root";
    private String password = "";

    public DatabaseConnectionInfo() {
        super();
    }

    public DatabaseConnectionInfo(String hostname, String port, String username, String password) {
        this(hostname,port);
        this.username = username;
        this.password = password;
    }

    public DatabaseConnectionInfo(String hostname, String port) {
        this.hostname = hostname;
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    //tworzy link do łączenia z konkretną bazą danych (db) bazą danych z kodowaniem utf-8
    public String makeDbLink()  {
        String link = "jdbc:mysql://"+getHostname()+":"+getPort()+"/db?useUnicode=true&characterEncoding=utf-8&useTimezone=true&serverTimezone=CET";
        System.out.println(link);
        return link;
    }

    //tworzy link do łączenia z bazą bez podawania bazy aby ją stworzyć.
    public String makeConnectLink()  {
        String link = "jdbc:mysql://"+getHostname()+":"+getPort()+"/?useUnicode=true&characterEncoding=utf-8&useTimezone=true&serverTimezone=CET";
        System.out.println(link);
        return link;
    }
}
