package pk.dealndiscount.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Deal.
 */
@Entity
@Table(name = "deal")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Deal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    @Column(name = "discount_percentage", nullable = false)
    private Integer discountPercentage;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @NotNull
    @Column(name = "is_deal_active", nullable = false)
    private Boolean isDealActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "bank" }, allowSetters = true)
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "city" }, allowSetters = true)
    private Store store;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Deal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDiscountPercentage() {
        return this.discountPercentage;
    }

    public Deal discountPercentage(Integer discountPercentage) {
        this.setDiscountPercentage(discountPercentage);
        return this;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDescription() {
        return this.description;
    }

    public Deal description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Deal startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public Deal endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsDealActive() {
        return this.isDealActive;
    }

    public Deal isDealActive(Boolean isDealActive) {
        this.setIsDealActive(isDealActive);
        return this;
    }

    public void setIsDealActive(Boolean isDealActive) {
        this.isDealActive = isDealActive;
    }

    public Card getCard() {
        return this.card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Deal card(Card card) {
        this.setCard(card);
        return this;
    }

    public Store getStore() {
        return this.store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Deal store(Store store) {
        this.setStore(store);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deal)) {
            return false;
        }
        return getId() != null && getId().equals(((Deal) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Deal{" +
            "id=" + getId() +
            ", discountPercentage=" + getDiscountPercentage() +
            ", description='" + getDescription() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", isDealActive='" + getIsDealActive() + "'" +
            "}";
    }
}
