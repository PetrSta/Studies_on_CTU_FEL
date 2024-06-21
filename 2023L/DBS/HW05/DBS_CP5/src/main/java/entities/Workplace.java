package entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "\"Workplace\"")
public class Workplace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"Workplace ID\"", nullable = false)
    private Integer id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"Employee ID\"", nullable = false)
    private Set<Employee> employeeIDs;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"Monument ID\"", nullable = false)
    private Set<Monument> monumentIDs;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<Employee> getEmployeeIDs() {
        return employeeIDs;
    }

    public void setEmployeeIDs(Set<Employee> employeeIDs) {
        this.employeeIDs = employeeIDs;
    }

    public Set<Monument> getMonumentIDs() {
        return monumentIDs;
    }

    public void setMonumentIDs(Set<Monument> monumentIDs) {
        this.monumentIDs = monumentIDs;
    }

}