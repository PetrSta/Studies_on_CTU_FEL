import java.sql.*;

public class TransactionsJDBC {
    // transaction to insert information about new Person
    // including information into Person table, Employee table and Phone table
    public static void transaction(Connection connection) throws SQLException {
        Statement query = connection.createStatement();
        query.executeQuery(
            "START TRANSACTION;" +
            "INSERT INTO \"Person\" (\"Forename\", \"Surname\", \"E-mail\", \"City\", \"Street\", \"Postal code\"," +
            "\"Birth date\") VALUES ('Jan', 'Černý', 'jancern@seznam.cz', 'Praha', 'Evropská'," +
            "'16000', '1897-04-13');" +
            "INSERT INTO \"Employee\" (\"Person ID\", \"Salary\") VALUES ('4560', '35400');" +
            "INSERT INTO \"Phone\" (\"Person ID\", \"Phone number\") VALUES ('4560', '100557947');"
        );
    }
}
