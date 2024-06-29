import DAO.EmployeeDAO;
import DAO.PersonDAO;
import DAO.PhoneDAO;
import entities.Employee;
import entities.Person;
import entities.Phone;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class TransactionsJPA {
    private final EntityManager entityManager;

    public TransactionsJPA(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addNewEmployeeInfo(Person newPerson, Employee newEmployee, Phone newPhone) {
        System.out.println("\n|| Starting transaction: \"addNewEmployeeInfo\" ||");
        EntityTransaction entityTransaction = entityManager.getTransaction();
        PersonDAO personDAO = new PersonDAO(entityManager);
        EmployeeDAO employeeDAO = new EmployeeDAO(entityManager);
        PhoneDAO phoneDAO = new PhoneDAO(entityManager);

        entityTransaction.begin();

        personDAO.createPerson(newPerson);
        employeeDAO.createEmployee(newEmployee);
        phoneDAO.createPhone(newPhone);
        System.out.println("\n|| Committing transaction: \"addNewEmployeeInfo\" ||");
        entityTransaction.commit();
    }

}
