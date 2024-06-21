package entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "\"Monument\"")
public class Monument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"Monument ID\"", nullable = false)
    private Integer id;

    @Column(name = "\"Monument name\"", nullable = false, length = 100)
    private String monumentName;

    @Column(name = "\"Country\"", nullable = false, length = 30)
    private String country;

    @Column(name = "\"City\"", nullable = false, length = 30)
    private String city;

    @Column(name = "\"First mention\"")
    private LocalDate firstMention;

    @Column(name = "\"Special significance\"")
    private String specialSignificance;

    @OneToOne(mappedBy = "monumentID")
    private Castle castle = new Castle();

    @OneToOne(mappedBy = "monumentID")
    private Church church = new Church();

    @OneToMany(mappedBy = "monumentID")
    private Set<Tour> tours = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "monumentIDs")
    private Set<Workplace> workplaces = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMonumentName() {
        return monumentName;
    }

    public void setMonumentName(String monumentName) {
        this.monumentName = monumentName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getFirstMention() {
        return firstMention;
    }

    public void setFirstMention(LocalDate firstMention) {
        this.firstMention = firstMention;
    }

    public String getSpecialSignificance() {
        return specialSignificance;
    }

    public void setSpecialSignificance(String specialSignificance) {
        this.specialSignificance = specialSignificance;
    }

    public Castle getCastle() {
        return castle;
    }

    public void setCastles(Castle castle) {
        this.castle = castle;
    }

    public Church getChurch() {
        return church;
    }

    public void setChurches(Church church) {
        this.church = church;
    }

    public Set<Tour> getTours() {
        return tours;
    }

    public void setTours(Set<Tour> tours) {
        this.tours = tours;
    }

    public Set<Workplace> getWorkplaces() {
        return workplaces;
    }

    public void setWorkplaces(Set<Workplace> workplaces) {
        this.workplaces = workplaces;
    }

}