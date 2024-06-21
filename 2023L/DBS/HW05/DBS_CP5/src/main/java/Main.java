import java.sql.*;
import java.time.LocalDate;

import DAO.MonumentDAO;
import entities.Employee;
import entities.Monument;
import entities.Person;
import entities.Phone;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {

    private static void setUpData(Person newPerson, Employee newEmployee, Phone newPhone) {
        newPerson.setId(4600);
        newPerson.setForename("Jan");
        newPerson.setSurname("Černý");
        newPerson.setEMail("jancern@seznam.cz");
        newPerson.setCity("Praha");
        newPerson.setStreet("Evropská");
        newPerson.setPostalCode("160 00");
        newPerson.setBirthDate(LocalDate.parse("1925-04-13"));

        newEmployee.setPersonID(newPerson);
        newEmployee.setSalary(35400);

        newPhone.setPersonID(newPerson);
        newPhone.setPhoneNumber(100557947);
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("application_pu");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        QueriesJPA queriesJPA = new QueriesJPA(entityManager);
//        queriesJPA.monumentInfoExceptCity("Praha");
//        queriesJPA.numberOfToursInCity("Kutná Hora");
//        queriesJPA.numberOfVisitsInCity("Praha");
//        queriesJPA.employeeSalary();
//        queriesJPA.activeTourists(13);

        TransactionsJPA transactionsJPA = new TransactionsJPA(entityManager);

        Person newPerson = new Person();
        Employee newEmployee = new Employee();
        Phone newPhone = new Phone();
        setUpData(newPerson, newEmployee, newPhone);

        transactionsJPA.addNewEmployeeInfo(newPerson, newEmployee, newPhone);

        entityManager.close();
        entityManagerFactory.close();
    }
}
