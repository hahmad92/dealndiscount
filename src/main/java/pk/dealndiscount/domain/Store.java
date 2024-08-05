package pk.dealndiscount.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import pk.dealndiscount.domain.enumeration.StoreCategory;
import pk.dealndiscount.domain.enumeration.StoreType;

/**
 * A Store.
 */
@Entity
@Table(name = "store")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "store_name", nullable = false)
    private String storeName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "store_type", nullable = false)
    private StoreType storeType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "store_category", nullable = false)
    private StoreCategory storeCategory;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Column(name = "geo_location", nullable = false)
    private String geoLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    private City city;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Store id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStoreName() {
        return this.storeName;
    }

    public Store storeName(String storeName) {
        this.setStoreName(storeName);
        return this;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public StoreType getStoreType() {
        return this.storeType;
    }

    public Store storeType(StoreType storeType) {
        this.setStoreType(storeType);
        return this;
    }

    public void setStoreType(StoreType storeType) {
        this.storeType = storeType;
    }

    public StoreCategory getStoreCategory() {
        return this.storeCategory;
    }

    public Store storeCategory(StoreCategory storeCategory) {
        this.setStoreCategory(storeCategory);
        return this;
    }

    public void setStoreCategory(StoreCategory storeCategory) {
        this.storeCategory = storeCategory;
    }

    public String getAddress() {
        return this.address;
    }

    public Store address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return this.phone;
    }

    public Store phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGeoLocation() {
        return this.geoLocation;
    }

    public Store geoLocation(String geoLocation) {
        this.setGeoLocation(geoLocation);
        return this;
    }

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Store city(City city) {
        this.setCity(city);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Store)) {
            return false;
        }
        return getId() != null && getId().equals(((Store) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Store{" +
            "id=" + getId() +
            ", storeName='" + getStoreName() + "'" +
            ", storeType='" + getStoreType() + "'" +
            ", storeCategory='" + getStoreCategory() + "'" +
            ", address='" + getAddress() + "'" +
            ", phone='" + getPhone() + "'" +
            ", geoLocation='" + getGeoLocation() + "'" +
            "}";
    }
}
