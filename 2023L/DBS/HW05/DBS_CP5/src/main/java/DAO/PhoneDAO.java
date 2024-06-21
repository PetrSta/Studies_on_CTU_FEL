package DAO;

import entities.*;
import jakarta.persistence.EntityManager;

public class PhoneDAO {
    private final EntityManager entityManager;

    public PhoneDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void createPhone(Phone phone) {
        entityManager.persist(phone);
    }

    public void updatePhone(Phone phone) {
        entityManager.merge(phone);
    }

    public Phone findPhone(long id) {
        return entityManager.find(Phone.class, id);
    }

    public void deletePhone(long id) {
        Phone phoneToDelete = entityManager.find(Phone.class, id);
        entityManager.remove(phoneToDelete);
    }
}
