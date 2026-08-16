import database.DatabaseConnection;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


void main(String[] args) {
    try {
        DatabaseConnection db = new DatabaseConnection();
        db.connect("test.db");
        Statement statement;
        statement = db.getConnection().createStatement();
        String sql = "Create table if not exists TestTable( id integer primary key, name text)";
        statement.executeUpdate(sql);
        String sqlInsert = "Insert into TestTable (name) values('Балбес')";
        statement.executeUpdate(sqlInsert);
        String sqlSelect = "Select * from TestTable";
        ResultSet result = statement.executeQuery(sqlSelect);
        while (result.next()) {
            int id = result.getInt("id");
            String name = result.getString("name");
            System.out.println(id + " - " + name);
        }
        db.disconnect();
    } catch (SQLException e) {
        System.out.println("Что-то неправильно " + e.getMessage());
    }
}
