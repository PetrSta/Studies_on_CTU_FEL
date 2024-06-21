package DAO;

import entities.*;
import jakarta.persistence.EntityManager;

public class TourDAO {
    private final EntityManager entityManager;

    public TourDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void createTour(Tour tour) {
        entityManager.persist(tour);
    }

    public void updateTour(Tour tour) {
        entityManager.merge(tour);
    }

    public Tour findTour(long id) {
        return entityManager.find(Tour.class, id);
    }

    public void deleteTour(long id) {
        Tour tourToDelete = entityManager.find(Tour.class, id);
        entityManager.remove(tourToDelete);
    }
}
