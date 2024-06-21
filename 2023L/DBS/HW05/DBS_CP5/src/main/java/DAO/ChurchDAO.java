package DAO;

import entities.*;
import jakarta.persistence.EntityManager;

public class ChurchDAO {
    private final EntityManager entityManager;

    public ChurchDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void createChurch(Church church) {
        entityManager.persist(church);
    }

    public void updateChurch(Church church) {
        entityManager.merge(church);
    }

    public Church findChurch(long id) {
        return entityManager.find(Church.class, id);
    }

    public void deleteChurch(long id) {
        Church churchToDelete = entityManager.find(Church.class, id);
        entityManager.remove(churchToDelete);
    }
}
