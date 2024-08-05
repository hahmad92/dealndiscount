package pk.dealndiscount.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import pk.dealndiscount.domain.enumeration.StoreCategory;
import pk.dealndiscount.domain.enumeration.StoreType;

/**
 * A DTO for the {@link pk.dealndiscount.domain.Store} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StoreDTO implements Serializable {

    private Long id;

    @NotNull
    private String storeName;

    @NotNull
    private StoreType storeType;

    @NotNull
    private StoreCategory storeCategory;

    @NotNull
    private String address;

    @NotNull
    private String phone;

    @NotNull
    private String geoLocation;

    private CityDTO city;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public StoreType getStoreType() {
        return storeType;
    }

    public void setStoreType(StoreType storeType) {
        this.storeType = storeType;
    }

    public StoreCategory getStoreCategory() {
        return storeCategory;
    }

    public void setStoreCategory(StoreCategory storeCategory) {
        this.storeCategory = storeCategory;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StoreDTO)) {
            return false;
        }

        StoreDTO storeDTO = (StoreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, storeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StoreDTO{" +
            "id=" + getId() +
            ", storeName='" + getStoreName() + "'" +
            ", storeType='" + getStoreType() + "'" +
            ", storeCategory='" + getStoreCategory() + "'" +
            ", address='" + getAddress() + "'" +
            ", phone='" + getPhone() + "'" +
            ", geoLocation='" + getGeoLocation() + "'" +
            ", city=" + getCity() +
            "}";
    }
}
