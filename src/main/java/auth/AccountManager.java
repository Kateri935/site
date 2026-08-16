package auth;

import database.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountManager {
    private DatabaseConnection db;

    public AccountManager(DatabaseConnection db) {
        this.db = db;
        try {

            Statement statement;
            statement = db.getConnection().createStatement();
            String sql = "Create table if not exists Accounts(id integer primary key, username text, password_hash text)";

            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Ошибка " + e.getMessage());
        }
    }

    public void register(String username, String password) {
        try {
            String hash = BCrypt.hashpw(password, BCrypt.gensalt());
//            boolean matches = BCrypt.checkpw(password, hash);
//            String sql = "INSERT INTO account (username, password_hash) VALUES ( ?, ?)";
            String sql = "INSERT INTO Accounts (username, password_hash) VALUES (?, ?)";
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, hash);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка регистрации: " + e.getMessage());
        }
    }

    public boolean authenticate(String username, String password) throws SQLException {

        String sqlSelectUsername = "Select password_hash from Accounts where username = ?";
        PreparedStatement statement = db.getConnection().prepareStatement(sqlSelectUsername);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()){
            String hashFromDb = resultSet.getString("password_hash");
            return BCrypt.checkpw(password, hashFromDb);
        }
        else {
            return false;
        }
    }
    public Account getAccount(String username) throws SQLException {
        String sql = "Select id, username from Accounts where username = ?";
        PreparedStatement statement = db.getConnection().prepareStatement(sql);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()){
            String usernameFromDb = resultSet.getString("username");
            int idFromDb = resultSet.getInt("id");
            Account account = new Account(idFromDb, usernameFromDb);
            return  account;
        }
        else  {
            return null;
        }
    }
    public Account getAccount(int id) throws SQLException {
        String sql = "Select id, username from Accounts where id = ?";
        PreparedStatement statement = db.getConnection().prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()){
            String usernameFromDb = resultSet.getString("username");
            int idFromDb = resultSet.getInt("id");
            Account account = new Account(idFromDb, usernameFromDb);
            return  account;
        }
        else  {
            return null;
        }
    }
}
