package entities;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "\"Employee\"")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "\"Employee ID\"", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "\"Person ID\"", nullable = false)
    private Person personID;

    @Column(name = "\"Salary\"", nullable = false)
    private Integer salary;

    @ManyToMany(mappedBy = "employeeIDs")
    private Set<Workplace> workplaces = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Person getPersonID() {
        return personID;
    }

    public void setPersonID(Person personID) {
        this.personID = personID;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Set<Workplace> getWorkplaces() {
        return workplaces;
    }

    public void setWorkplaces(Set<Workplace> workplaces) {
        this.workplaces = workplaces;
    }

}