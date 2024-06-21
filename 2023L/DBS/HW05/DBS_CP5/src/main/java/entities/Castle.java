package entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "\"Castle\"")
public class Castle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"Castle ID\"", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"Monument ID\"", nullable = false)
    private Monument monumentID;

    @Column(name = "\"Fortification\"", nullable = false)
    private Boolean fortification = false;

    @Column(name = "\"Palace\"", nullable = false)
    private Boolean palace = false;

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

    public Boolean getFortification() {
        return fortification;
    }

    public void setFortification(Boolean fortification) {
        this.fortification = fortification;
    }

    public Boolean getPalace() {
        return palace;
    }

    public void setPalace(Boolean palace) {
        this.palace = palace;
    }

}