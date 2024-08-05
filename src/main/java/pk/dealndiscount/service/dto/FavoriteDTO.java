package pk.dealndiscount.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import pk.dealndiscount.domain.enumeration.FavoriteType;

/**
 * A DTO for the {@link pk.dealndiscount.domain.Favorite} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FavoriteDTO implements Serializable {

    private Long id;

    @NotNull
    private String userId;

    private String storeId;

    private String cityId;

    private String cardId;

    @NotNull
    private FavoriteType favoriteType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public FavoriteType getFavoriteType() {
        return favoriteType;
    }

    public void setFavoriteType(FavoriteType favoriteType) {
        this.favoriteType = favoriteType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FavoriteDTO)) {
            return false;
        }

        FavoriteDTO favoriteDTO = (FavoriteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, favoriteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FavoriteDTO{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", storeId='" + getStoreId() + "'" +
            ", cityId='" + getCityId() + "'" +
            ", cardId='" + getCardId() + "'" +
            ", favoriteType='" + getFavoriteType() + "'" +
            "}";
    }
}
