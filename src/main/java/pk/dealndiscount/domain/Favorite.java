package pk.dealndiscount.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import pk.dealndiscount.domain.enumeration.FavoriteType;

/**
 * A Favorite.
 */
@Entity
@Table(name = "favorite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Favorite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "store_id")
    private String storeId;

    @Column(name = "city_id")
    private String cityId;

    @Column(name = "card_id")
    private String cardId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "favorite_type", nullable = false)
    private FavoriteType favoriteType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Favorite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public Favorite userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreId() {
        return this.storeId;
    }

    public Favorite storeId(String storeId) {
        this.setStoreId(storeId);
        return this;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCityId() {
        return this.cityId;
    }

    public Favorite cityId(String cityId) {
        this.setCityId(cityId);
        return this;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCardId() {
        return this.cardId;
    }

    public Favorite cardId(String cardId) {
        this.setCardId(cardId);
        return this;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public FavoriteType getFavoriteType() {
        return this.favoriteType;
    }

    public Favorite favoriteType(FavoriteType favoriteType) {
        this.setFavoriteType(favoriteType);
        return this;
    }

    public void setFavoriteType(FavoriteType favoriteType) {
        this.favoriteType = favoriteType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Favorite)) {
            return false;
        }
        return getId() != null && getId().equals(((Favorite) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Favorite{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", storeId='" + getStoreId() + "'" +
            ", cityId='" + getCityId() + "'" +
            ", cardId='" + getCardId() + "'" +
            ", favoriteType='" + getFavoriteType() + "'" +
            "}";
    }
}
