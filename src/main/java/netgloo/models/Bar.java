package netgloo.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Bar")
public class Bar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long barId;

    @NotNull
    private String name;

    @NotNull
    private String address;

    @NotNull
    private String city;

    @NotNull
    private String postalCode;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    @NotNull
    private String description;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="BeerBar",
            joinColumns = {@JoinColumn(name="barId")},
            inverseJoinColumns = {@JoinColumn(name="BeerId")})

    private Set<Beer> listBeer = new HashSet<>();

    // ------------------------
    // PUBLIC METHODS
    // ------------------------

    public Bar() {}


    public Bar(String Name,
               String Address,
               String City,
               String PostalCode,
               double Latitude,
               double Longitude,
               String Description) {
        name = Name;
        address = Address;
        city = City;
        postalCode = PostalCode;
        latitude = Latitude;
        longitude = Longitude;
        description = Description;
    }

    public long getBarId() {
        return this.barId;
    }

    public void setBarId(long barId) {
        this.barId = barId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Beer> getListBeer() {
        return this.listBeer;
    }

    public void setListBeer(Set<Beer> listBeer) {
        this.listBeer = listBeer;
    }
}