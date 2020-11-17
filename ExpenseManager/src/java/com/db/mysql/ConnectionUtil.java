package com.db.mysql;

import static com.db.mysql.ConnectionManager.logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author arun
 */
public class ConnectionUtil {

    public static final int SUCCESS = 1;
    public static final int FAILED = -1;
    private static Connection Conn;

    private final int CONN_TIMEOUT = 120;

    public ConnectionUtil() {
        try {
            if (Conn != null) {
                if ((Conn.isClosed() == true) || (Conn.isValid(CONN_TIMEOUT) == false)) {

                    Conn.close();
                    Conn = null;

                    ConnectionManager Connection = new ConnectionManager();
                    Conn = Connection.getConnection();
                }
            } else {
                ConnectionManager Connection = new ConnectionManager();
                Conn = Connection.getConnection();

            }
        } catch (Exception ex) {
            System.out.println("Exception :: " + ex);
        }
    }

    public int executeSQL(String strQuery) {

        Statement stmt = null;

        if (checkConnection() == false) {
            return -1;
        }

        if (strQuery.isEmpty()) {
            logger.error("executeQuery:: Query string is empty");
            return -1;
        }

        try {
            stmt = Conn.createStatement();

            logger.trace(strQuery);
            stmt.execute(strQuery);
        } catch (Exception ex) {
            logger.error("executeQuery:: Exception caught!! " + ex);

        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception exStmt) {
                logger.error("executeQuery:: Exception on closing Statment " + exStmt);

            }
        }
        return 1;
    }

    public String getValueFromQuery(String strQuery) {

        Statement stmt = null;
        ResultSet rs = null;

        String strOutputVal = "";

        if (checkConnection() == false) {
            return strOutputVal;
        }

        if (strQuery.isEmpty()) {
            logger.error("getValueFromQuery:: Query string is empty");
            return strOutputVal;
        }

        try {
            stmt = Conn.createStatement();

            logger.trace(strQuery);
            rs = stmt.executeQuery(strQuery);

            if (rs.next()) {
                strOutputVal = rs.getString("OutputValue");
            }
        } catch (Exception ex) {
            logger.error("getValueFromQuery:: Exception caught!! " + ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception exrs) {
                logger.error("getValueFromQuery:: Exception on closing resultset " + exrs);
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (Exception exStmt) {
                    logger.error("getValueFromQuery:: Exception on closing statement " + exStmt);
                }
            }
        }
        return strOutputVal;
    }

    public boolean fetchMultipleRows(String strQuery, ArrayList<String> recordKey, ArrayList<ArrayList<String>> recordValue) {
        Statement stmt = null;
        ResultSet rs = null;

        if (checkConnection() == false) {
            return false;
        }

        if (strQuery.isEmpty()) {
            logger.error("fetchMultipleRows:: No Query to execute");
            return false;
        }
        if (recordKey.isEmpty()) {
            logger.error("fetchMultipleRows:: Column Key is empty");
            return false;
        }

        try {
            logger.trace("fetchMultipleRows:: Query Stmt = " + strQuery);

            stmt = Conn.createStatement();
            rs = stmt.executeQuery(strQuery);

            //Clean-up
            recordValue.clear();

            while (rs.next()) {
                ArrayList<String> recordData = new ArrayList<>();
                for (String Key : recordKey) {
                    recordData.add(rs.getString(Key));
                }
                recordValue.add(recordData);
            }
        } catch (Exception ex) {
            logger.error("fetchMultipleRows:: Exception caught as " + ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception exRs) {
                logger.error("fetchMultipleRows:: Exception caught on closing RecordSet " + exRs);
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (Exception exStmt) {
                    logger.error("fetchMultipleRows:: Exception caught on closing Statement " + exStmt);
                }
            }
        }

        return true;
    }

    public boolean fetchSingleRow(String strQuery, ArrayList<String> recordKey, ArrayList<String> recordValue) {
        Statement stmt = null;
        ResultSet rs = null;

        if (checkConnection() == false) {
            return false;
        }

        if (strQuery.isEmpty()) {
            logger.error("fetchSingleRow:: No Query to execute");
            return false;
        }
        if (recordKey.isEmpty()) {
            logger.error("fetchSingleRow:: Column Key is empty");
            return false;
        }

        try {
            logger.trace("fetchSingleRow:: Query Stmt = " + strQuery);

            stmt = Conn.createStatement();
            rs = stmt.executeQuery(strQuery);

            //Clean-up
            recordValue.clear();

            while (rs.next()) {
                for (String Key : recordKey) {
                    recordValue.add(rs.getString(Key));
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception :: " + ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception exRs) {
                System.out.println("Exception :: " + exRs);
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (Exception exStmt) {
                    System.out.println("Exception :: " + exStmt);

                }
            }
        }

        return true;
    }

    boolean checkConnection() {
        try {
            if (Conn != null) {
                if ((Conn.isClosed() == true) || (Conn.isValid(CONN_TIMEOUT) == false)) {
                    logger.warn("checkConnection:: Database connection is closed!!!. Re-connecting to the database...");
                    Conn.close();
                    Conn = null;

                    ConnectionManager Connection = new ConnectionManager();
                    Conn = Connection.getConnection();
                }
            } else {
                ConnectionManager Connection = new ConnectionManager();
                Conn = Connection.getConnection();
            }
        } catch (Exception exConnCheck) {
            logger.error("checkConnection:: Exception caught on connection check " + exConnCheck);
            return false;
        }
        return true;
    }
}
