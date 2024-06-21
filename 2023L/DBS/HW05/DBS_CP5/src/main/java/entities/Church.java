package entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "\"Church\"")
public class Church {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"Church ID\"", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"Monument ID\"", nullable = false)
    private Monument monumentID;

    @Column(name = "\"Sanctified\"", nullable = false)
    private Boolean sanctified = false;

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

    public Boolean getSanctified() {
        return sanctified;
    }

    public void setSanctified(Boolean sanctified) {
        this.sanctified = sanctified;
    }

}