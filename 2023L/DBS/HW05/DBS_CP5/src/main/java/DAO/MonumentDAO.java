package DAO;

import entities.*;
import jakarta.persistence.EntityManager;

public class MonumentDAO {
    private final EntityManager entityManager;

    public MonumentDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void createMonument(Monument monument) {
        entityManager.persist(monument);
    }

    public void updateMonument(Monument monument) {
        entityManager.merge(monument);
    }

    public Monument findMonument(long id) {
        return entityManager.find(Monument.class, id);
    }

    public void deleteMonument(long id) {
        Monument monumentToDelete = entityManager.find(Monument.class, id);
        entityManager.remove(monumentToDelete);
    }
}
