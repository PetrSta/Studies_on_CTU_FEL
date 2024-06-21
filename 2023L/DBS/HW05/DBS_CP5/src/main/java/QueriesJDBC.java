import java.sql.*;

public class QueriesJDBC {
    // information about monument in all cities except the selected city
    public static void monumentInfoExceptCity(Connection connection, String city) throws SQLException {
        PreparedStatement query = connection.prepareStatement(
            "SELECT \"Monument name\", \"First mention\", \"Special significance\" " +
            "FROM \"Monument\" WHERE (\"City\" != ?)"
        );
        query.setString(1, city);
        ResultSet result = query.executeQuery();
        while(result.next()) {
            System.out.println(
                "Monument name: " + result.getString("Monument name") +
                " |First mention: " + result.getDate("First mention") +
                " |Special significance: " + result.getString("Special significance")
            );
        }
    }

    // number of tours in the selected city
    public static void numberOfToursInCity(Connection connection, String city) throws SQLException {
        PreparedStatement query = connection.prepareStatement(
            "SELECT COUNT(\"Tour\".\"Monument ID\") FROM \"Tour\" INNER JOIN \"Monument\" ON \"Tour\".\"Monument ID\"" +
            "= \"Monument\".\"Monument ID\" AND (\"City\" = ?)"
        );
        query.setString(1, city);
        ResultSet result = query.executeQuery();
        while(result.next()) {
            System.out.println(
                "Result: " + result.getInt(1)
            );
        }
    }

    // number of visits on monument in selected city
    public static void numberOfVisitsInCity(Connection connection, String city) throws SQLException {
        PreparedStatement query = connection.prepareStatement(
            "SELECT COUNT(\"Visited tours\".\"Person ID\") FROM \"Visited tours\" INNER JOIN \"Tour\" ON" +
            "\"Visited tours\".\"Tour ID\" = \"Tour\".\"Tour ID\" INNER JOIN \"Monument\" ON" +
            "\"Tour\".\"Monument ID\" = \"Monument\".\"Monument ID\" AND (\"City\" = ?)"
        );
        query.setString(1, city);
        ResultSet result = query.executeQuery();
        while(result.next()) {
            System.out.println(
                "Result: " + result.getInt(1)
            );
        }
    }

    // information about employees ordered by salary
    public static void employeeSalary(Connection connection) throws SQLException {
        Statement query = connection.createStatement();
        ResultSet result = query.executeQuery(
            "SELECT \"Forename\", \"Surname\", \"E-mail\", \"Salary\" FROM \"Person\" INNER JOIN \"Employee\" ON" +
            "\"Person\".\"Person ID\" = \"Employee\".\"Person ID\" ORDER BY \"Salary\""
        );
        while(result.next()) {
            System.out.println(
                "Forename: " + result.getString("Forename") +
                " |Surname: " + result.getString("Surname") +
                " |E-mail: " + result.getString("E-mail") +
                " |Salary: " + result.getInt("Salary")
            );
        }
    }

    // information about people that visited x+ (given value) tours
    public static void activeTourists(Connection connection, Integer limit) throws SQLException {
        PreparedStatement query = connection.prepareStatement(
            "SELECT \"Forename\", \"Surname\", \"E-mail\" FROM \"Person\" INNER JOIN \"Visited tours\" ON" +
            "\"Person\".\"Person ID\" = \"Visited tours\".\"Person ID\" GROUP BY \"Person\".\"Person ID\"," +
            "\"Forename\", \"Surname\", \"E-mail\" HAVING COUNT(*) >= ?"
        );
        query.setInt(1, limit);
        ResultSet result = query.executeQuery();
        while(result.next()) {
            System.out.println(
                "Forename: " + result.getString("Forename") +
                " |Surname: " + result.getString("Surname") +
                " |E-mail: " + result.getString("E-mail")
            );
        }
    }
}
