package DAO;

import entities.*;
import jakarta.persistence.EntityManager;

public class EmployeeDAO {
    private final EntityManager entityManager;

    public EmployeeDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void createEmployee(Employee employee) {
        entityManager.persist(employee);
    }

    public void updateEmployee(Employee employee) {
        entityManager.merge(employee);
    }

    public Employee findEmployee(long id) {
        return entityManager.find(Employee.class, id);
    }

    public void deleteEmployee(long id) {
        Employee employeeToDelete = entityManager.find(Employee.class, id);
        entityManager.remove(employeeToDelete);
    }
}
