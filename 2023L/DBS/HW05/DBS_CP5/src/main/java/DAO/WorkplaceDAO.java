package DAO;

import entities.*;
import jakarta.persistence.EntityManager;

public class WorkplaceDAO {
    private final EntityManager entityManager;

    public WorkplaceDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void createWorkplace(Workplace workplace) {
        entityManager.persist(workplace);
    }

    public void updateWorkplace(Workplace workplace) {
        entityManager.merge(workplace);
    }

    public Workplace findWorkplace(long id) {
        return entityManager.find(Workplace.class, id);
    }

    public void deleteWorkplace(long id) {
        Workplace workplaceToDelete = entityManager.find(Workplace.class, id);
        entityManager.remove(workplaceToDelete);
    }
}
