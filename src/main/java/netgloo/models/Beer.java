package netgloo.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Beer")
public class Beer
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long beerId;

    @NotNull
    @Column(unique=true)
    private String name;

    @NotNull
    private long degree;

    @ManyToMany (mappedBy = "listBeer")
    @Transient
    private Set<Bar> listBars = new HashSet<>();

    // ------------------------
    // PUBLIC METHODS
    // ------------------------

    public Beer(){}
    public Beer(String Name, long Degree)
    {
        name = Name;
        degree = Degree;
    }

    public long getBeerId() {
        return this.beerId;
    }

    public void setBeerId(long beerId) {
        this.beerId = beerId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDegree() {
        return this.degree;
    }

    public void setDegree(long degree) {
        this.degree = degree;
    }

    public Set<Bar> getlistBars() {
        return this.listBars;
    }

    public void setlistBars(Set<Bar> listBars) {
        this.listBars = listBars;
    }
} // class Beer