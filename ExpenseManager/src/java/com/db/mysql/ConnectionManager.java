package com.db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author arun
 */
public class ConnectionManager {

    public static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    private Connection conn = null;

    public Connection getConnection() {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            if ((conn == null) || (conn.isClosed() == true)) {

                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/expensemanager", "root", "Arungiri1");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Exception ::" + e.getMessage());

        }
        return conn;
    }

}
