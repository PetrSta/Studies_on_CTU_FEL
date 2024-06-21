package entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "\"Person\"")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "\"Person ID\"", nullable = false)
    private Integer id;

    @Column(name = "\"Forename\"", nullable = false, length = 20)
    private String forename;

    @Column(name = "\"Surname\"", nullable = false, length = 20)
    private String surname;

    @Column(name = "\"E-mail\"", nullable = false, length = 50)
    private String eMail;

    @Column(name = "\"City\"", length = 50)
    private String city;

    @Column(name = "\"Street\"", length = 50)
    private String street;

    @Column(name = "\"Postal code\"", length = 10)
    private String postalCode;

    @Column(name = "\"Birth date\"")
    private LocalDate birthDate;

    @OneToOne(mappedBy = "personID")
    private Employee employee = new Employee();

    @OneToMany(mappedBy = "personID")
    private Set<Phone> phones = new LinkedHashSet<>();

    @OneToMany(mappedBy = "personID")
    private Set<VisitedTour> visitedTours = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployees(Employee employee) {
        this.employee = employee;
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    public void setPhones(Set<Phone> phones) {
        this.phones = phones;
    }

    public Set<VisitedTour> getVisitedTours() {
        return visitedTours;
    }

    public void setVisitedTours(Set<VisitedTour> visitedTours) {
        this.visitedTours = visitedTours;
    }

}