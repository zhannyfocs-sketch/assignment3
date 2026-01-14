package Data.DB;

import java.sql.*;

public class PostgresDB {
    private static final String URL = "jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:5432/postgres?sslmode=require";
    private static final String USER = "postgres.aeeyxigpjglbzteqolmh";
    private static final String PASSWORD = "AirisFIRSTdata99";
    private PostgresDB(){

    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
