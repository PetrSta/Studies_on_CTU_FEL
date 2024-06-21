package DAO;

import entities.*;
import jakarta.persistence.EntityManager;

public class TicketDAO {
    private final EntityManager entityManager;

    public TicketDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void createTicket(Ticket ticket) {
        entityManager.persist(ticket);
    }

    public void updateTicket(Ticket ticket) {
        entityManager.merge(ticket);
    }

    public Ticket findTicket(long id) {
        return entityManager.find(Ticket.class, id);
    }

    public void deleteTicket(long id) {
        Ticket ticketToDelete = entityManager.find(Ticket.class, id);
        entityManager.remove(ticketToDelete);
    }
}
