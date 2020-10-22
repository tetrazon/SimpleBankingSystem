package banking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardRepository {
    private String dbName;
    private final String createCardTable = "CREATE TABLE IF NOT EXISTS card (" +
            "id INTEGER PRIMARY KEY," +
            "number TEXT," +
            "pin TEXT," +
            "balance INTEGER DEFAULT 0);";
    private final String saveCardQuery = "insert into card values(?,?,?,?);";
    private final String selectAll = "SELECT * FROM card;";
    private final String getBalance = "SELECT balance FROM card where number = ?;";
    private final String addIncome = "UPDATE card SET balance = balance + ? WHERE  number = ?;";
    private final String getCardByNumber = "SELECT * FROM card where number = ?;";
    private final String deleteCardByNumber = "DELETE FROM card where number = ?;";

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

    public List<Card> getAll(){
        try (Connection connection = DbUtil.getConnection(dbName);
             PreparedStatement preparedStatement = connection.prepareStatement(selectAll)){
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Card> cards = new ArrayList<>();
            while (resultSet.next()) {
                cards.add(new Card(resultSet.getString("number"),resultSet.getString("pin"),
                        resultSet.getInt("balance")));
            }
            return cards;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getBalance(Card card){
        try (Connection connection = DbUtil.getConnection(dbName);
             PreparedStatement preparedStatement = connection.prepareStatement(getBalance)){
            preparedStatement.setString(1, card.getNumber());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.getInt("balance");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException();
    }

    public void addIncome(int income, String cardNumber) {
        try (Connection connection = DbUtil.getConnection(dbName);
             PreparedStatement preparedStatement = connection.prepareStatement(addIncome)) {
            preparedStatement.setInt(1, income);
            preparedStatement.setString(2, cardNumber);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Card getCardByNumber(String cardNumber){
        try (Connection connection = DbUtil.getConnection(dbName);
             PreparedStatement preparedStatement = connection.prepareStatement(getCardByNumber)){
            preparedStatement.setString(1, cardNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next() ) {
                //System.out.println("no data");
                return null;
            }
            String foundCardNumber = resultSet.getString("number");
            String foundCardPin = resultSet.getString("pin");
            int foundBalance = resultSet.getInt("balance");
            Card card = new Card(foundCardNumber, foundCardPin, foundBalance);
            return card;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String doTransfer(String stringCardTo, Card cardFrom, int transfer) {
        if (getCardByNumber(cardFrom.getNumber()).getBalance() - transfer < 0) {
            return "Not enough money!\n";
        } else {
            addIncome(transfer, stringCardTo);
            addIncome(-transfer, cardFrom.getNumber());
            return "Success!\n";
        }
    }

    public void deleteCardByNumber(String stringNumber) {
        try (Connection connection = DbUtil.getConnection(dbName); PreparedStatement preparedStatement = connection.prepareStatement(deleteCardByNumber)) {
            preparedStatement.setString(1, stringNumber);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
