package DAO;

import entities.*;
import jakarta.persistence.EntityManager;

public class PersonDAO {
    private final EntityManager entityManager;

    public PersonDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void createPerson(Person person) {
        entityManager.persist(person);
    }

    public void updatePerson(Person person) {
        entityManager.merge(person);
    }

    public Person findPerson(long id) {
        return entityManager.find(Person.class, id);
    }

    public void deletePerson(long id) {
        Person personToDelete = entityManager.find(Person.class, id);
        entityManager.remove(personToDelete);
    }
}
