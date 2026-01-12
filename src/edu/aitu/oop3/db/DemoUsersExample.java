package edu.aitu.oop3.db;
import edu.aitu.oop3.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class DemoUsersExample {
    public static void main(String[] args) {
        System.out.println("Demo: create table, insert, select");
        try (Connection connection = DatabaseConnection.getConnection()) {
            createTableIfNeeded(connection);
            insertUser(connection, "Alice", "alice@example.com");
            insertUser(connection, "Bob", "bob@example.com");
            printAllUsers(connection);
        } catch (SQLException e) {
            System.out.println("Database error:");
            e.printStackTrace();
        }
    }
    private static void createTableIfNeeded(Connection connection) throws SQLException {
        String sql = """
        create table if not exists demo_users (
                id serial primary key,
                name varchar(100) not null,
                email varchar(100) unique not null
 );
        """;
               try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                   stmt.execute();
                   System.out.println("Table demo_users is ready.");
               }
           }
           private static void insertUser(Connection connection, String name, String email) throws SQLException {
               String sql = "insert into demo_users (name, email) values (?, ?) " +
                       "on conflict (email) do nothing;";
               try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                   stmt.execute();
                   System.out.println("Table demo_users is ready.");
               }
           }
    private static void insertUser(Connection connection, String name, String email) throws SQLException {
        String sql = "insert into demo_users (name, email) values (?, ?) " +
                "on conflict (email) do nothing;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {