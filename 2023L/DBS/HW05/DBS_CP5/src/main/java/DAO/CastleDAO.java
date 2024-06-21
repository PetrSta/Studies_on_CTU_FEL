package DAO;

import entities.*;
import jakarta.persistence.EntityManager;

public class CastleDAO {
    private final EntityManager entityManager;

    public CastleDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void createCastle(Castle castle) {
        entityManager.persist(castle);
    }

    public void updateCastle(Castle castle) {
        entityManager.merge(castle);
    }

    public Castle findCastle(long id) {
        return entityManager.find(Castle.class, id);
    }

    public void deleteCastle(long id) {
        Castle castleToDelete = entityManager.find(Castle.class, id);
        entityManager.remove(castleToDelete);
    }
}
