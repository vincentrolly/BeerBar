package netgloo.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Bar")
public class Bar
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long barId;

    @NotNull
    @Column(unique=true)
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

    public Bar() {
        name = "";
        address = "";
        city = "";
        postalCode = "";
        latitude = 0.0;
        longitude = 0.0;
        description = "";
    }


    public Bar(String Name,
               String Address,
               String City,
               String PostalCode,
               double Latitude,
               double Longitude,
               String Description ) {
        name = Name;
        address = Address;
        city = City;
        postalCode = PostalCode;
        latitude = Latitude;
        longitude = Longitude;
        description = Description;
    }

    public long getBarId() {
        return barId;
    }

    public void setBarId(long barId) {
        this.barId = barId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Beer> getListBeer()
    {
      return this.listBeer;
    }

    public void setListBeer(Set<Beer> listBeer)
    {
        this.listBeer = listBeer;
    }

//    public void addBeer(Beer b, int qte, int price)
//    {
//        BeerBar association = new BeerBar();
//        association.setBeer(b);
//        // TODO check if needed
//        //association.setBar(this);
//        association.setPrice(price);
//        association.setQuantity(qte);
//
//
//
//        this.beers.add(association);
//        // Also add the association object to the employee.
//        b.getBars().add(association);
//    }

//    public Set<Beer> getListBeer() {
//        return this.listBeer;
//    }
//
//    public void setListBeer(Set<Beer> listBeer) {
//        this.listBeer = listBeer;
//    }
}