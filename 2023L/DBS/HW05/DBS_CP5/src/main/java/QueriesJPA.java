import entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.sql.Date;
import java.util.List;

public class QueriesJPA {
    private final EntityManager entityManager;

    public QueriesJPA(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void monumentInfoExceptCity(String city) {
        System.out.println("\n|| Starting query: \"monumentInfoExceptCity\" ||");
        Query query = entityManager.createNativeQuery(
            "SELECT \"Monument name\", \"First mention\", \"Special significance\" FROM \"Monument\"" +
            " WHERE(\"Monument\".\"City\" != :City)"
        );
        query.setParameter("City", city);
        List<Object[]> result = query.getResultList();
        System.out.println("|| Results: ||\n");
        for(Object[] resultMonument : result) {
            System.out.println(
                "Monument name: " + (String) resultMonument[0] +
                " |First mention: " + (Date) resultMonument[1] +
                " |Special significance: " + (String) resultMonument[2]
            );
        }
    }

    public void numberOfToursInCity(String city) {
        System.out.println("\n|| Starting query: \"numberOfToursInCity\" ||");

        Query query = entityManager.createNativeQuery(
            "SELECT COUNT(\"Tour\".\"Monument ID\") FROM \"Tour\" INNER JOIN \"Monument\" ON \"Tour\".\"Monument ID\"" +
            " = \"Monument\".\"Monument ID\" AND (\"Monument\".\"City\" = :City)"
        );
        query.setParameter("City", city);
        Object result = query.getSingleResult();
        System.out.println("|| Results: ||\n");
        System.out.println("Number of tours in city: " + city + " is: " + result.toString());
    }

    public void numberOfVisitsInCity(String city) {
        System.out.println("\n|| Starting query: \"numberOfToursInCity\" ||");
        Query query = entityManager.createNativeQuery(
            "SELECT COUNT(\"Visited tours\".\"Person ID\") FROM \"Visited tours\" INNER JOIN \"Tour\" ON" +
            " \"Visited tours\".\"Tour ID\" = \"Tour\".\"Tour ID\" INNER JOIN \"Monument\" ON \"Tour\".\"Monument ID\"" +
            " = \"Monument\".\"Monument ID\" AND (\"Monument\".\"City\" = :City)"
        );
        query.setParameter("City", city);
        Object result = query.getSingleResult();
        System.out.println("|| Results: ||\n");
        System.out.println("Number of visits in city: " + city + " is: " + result.toString());
    }

    public void employeeSalary() {
        System.out.println("\n|| Starting query: \"employeeSalary\" ||");
        Query query = entityManager.createNativeQuery(
            "SELECT \"Forename\", \"Surname\", \"E-mail\", \"Salary\" FROM \"Person\" INNER JOIN \"Employee\" ON" +
            "\"Person\".\"Person ID\" = \"Employee\".\"Person ID\" ORDER BY \"Salary\""
        );
        List<Object[]> result = query.getResultList();
        System.out.println("|| Results: ||\n");
        for(Object[] resultPerson : result) {
            System.out.println(
                "Employee forename: " + (String) resultPerson[0] +
                " |Employee surname: " + (String) resultPerson[1] +
                " |Employee e-mail: " + (String) resultPerson[2] +
                " |Employee salary: " + (Integer) resultPerson[3]
            );
        }
    }

    public void activeTourists(int limit) {
        System.out.println("\n|| Starting query: \"activeTourists\" ||");
        Query query = entityManager.createNativeQuery(
            "SELECT \"Forename\", \"Surname\", \"E-mail\" FROM \"Person\" INNER JOIN \"Visited tours\" ON" +
            " \"Person\".\"Person ID\" = \"Visited tours\".\"Person ID\" GROUP BY \"Person\".\"Person ID\", " +
            "\"Forename\", \"Surname\", \"E-mail\" HAVING COUNT(*) >= :Limit"
        );
        query.setParameter("Limit", limit);
        List<Object[]> result = query.getResultList();
        System.out.println("|| Results: ||\n");
        for(Object[] resultPerson : result) {
            System.out.println(
                "Visitor forename: " + (String) resultPerson[0] +
                " |Visitor surname: " + (String) resultPerson[1] +
                " |Visitor e-mail: " + (String) resultPerson[2]
            );
        }
    }
}
