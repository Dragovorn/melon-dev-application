package com.dragovorn.mda.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private Connection connection;

    public Database(String ip, int port, String user, String pass) throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/", user, pass);
    }

    public Statement createStatement() throws SQLException {
        return this.connection.createStatement();
    }

    public void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}