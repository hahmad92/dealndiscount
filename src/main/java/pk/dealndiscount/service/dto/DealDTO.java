package pk.dealndiscount.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link pk.dealndiscount.domain.Deal} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DealDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    private Integer discountPercentage;

    private String description;

    private Instant startDate;

    private Instant endDate;

    @NotNull
    private Boolean isDealActive;

    private CardDTO card;

    private StoreDTO store;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsDealActive() {
        return isDealActive;
    }

    public void setIsDealActive(Boolean isDealActive) {
        this.isDealActive = isDealActive;
    }

    public CardDTO getCard() {
        return card;
    }

    public void setCard(CardDTO card) {
        this.card = card;
    }

    public StoreDTO getStore() {
        return store;
    }

    public void setStore(StoreDTO store) {
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DealDTO)) {
            return false;
        }

        DealDTO dealDTO = (DealDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dealDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DealDTO{" +
            "id=" + getId() +
            ", discountPercentage=" + getDiscountPercentage() +
            ", description='" + getDescription() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", isDealActive='" + getIsDealActive() + "'" +
            ", card=" + getCard() +
            ", store=" + getStore() +
            "}";
    }
}
