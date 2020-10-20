package banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class CardRepository {
    //private Connection connection;
    private String dbName;
    private final String createCardTable = "CREATE TABLE IF NOT EXISTS card (" +
            "id INTEGER PRIMARY KEY," +
            "number TEXT," +
            "pin TEXT," +
            "balance INTEGER DEFAULT 0);";
    private final String saveCardQuery = "insert into card values(?,?,?,?);";

    public CardRepository(String dbName) {
        this.dbName = dbName;
        createTableCards();
    }

    public void createTableCards(){
        try(Connection connection = DbUtil.getConnection(dbName)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(createCardTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveCard(Card card){
            try(Connection connection = DbUtil.getConnection(dbName)) {
                PreparedStatement preparedStatement = connection.prepareStatement(saveCardQuery);
                preparedStatement.setInt(1, (int) System.currentTimeMillis());
                preparedStatement.setString(2, card.getNumber());
                preparedStatement.setString(3, card.getPin());
                preparedStatement.setInt(4, card.getBalance());
                preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
