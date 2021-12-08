package com.example.rpgutconnexion;
import java.sql.Connection;
import java.sql.DriverManager;
public class DatabaseConnection {

    public Connection databaseLink;

    public Connection getConnection(){
        String dbName = "";
        String dbUser ="";
        String dbPassword = "";
        String url = "jdbc::mysql://localhost/"+dbName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return databaseLink;
    }
}
