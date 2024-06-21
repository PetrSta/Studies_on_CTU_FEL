package entities;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "\"Tour\"")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"Tour ID\"", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "\"Monument ID\"", nullable = false)
    private Monument monumentID;

    @Column(name = "\"Tour name\"", nullable = false, length = 100)
    private String tourName;

    @Column(name = "\"Duration\"")
    private LocalTime duration;

    @OneToMany(mappedBy = "tourID")
    private Set<Ticket> tickets = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tourID")
    private Set<VisitedTour> visitedTours = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Monument getMonumentID() {
        return monumentID;
    }

    public void setMonumentID(Monument monumentID) {
        this.monumentID = monumentID;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Set<VisitedTour> getVisitedTours() {
        return visitedTours;
    }

    public void setVisitedTours(Set<VisitedTour> visitedTours) {
        this.visitedTours = visitedTours;
    }

}