package DAO;

import entities.*;
import jakarta.persistence.EntityManager;

public class VisitedTourDAO {
    private final EntityManager entityManager;

    public VisitedTourDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void createVisitedTour(VisitedTour visitedTour) {
        entityManager.persist(visitedTour);
    }

    public void updateVisitedTour(VisitedTour visitedTour) {
        entityManager.merge(visitedTour);
    }

    public VisitedTour findVisitedTour(long id) {
        return entityManager.find(VisitedTour.class, id);
    }

    public void deleteVisitedTour(long id) {
        VisitedTour visitedTourToDelete = entityManager.find(VisitedTour.class, id);
        entityManager.remove(visitedTourToDelete);
    }
}
