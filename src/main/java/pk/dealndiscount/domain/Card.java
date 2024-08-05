package pk.dealndiscount.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import pk.dealndiscount.domain.enumeration.CardCategory;
import pk.dealndiscount.domain.enumeration.CardNetwork;

/**
 * A Card.
 */
@Entity
@Table(name = "card")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "card_name", nullable = false)
    private String cardName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private CardCategory category;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "card_network", nullable = false)
    private CardNetwork cardNetwork;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bank bank;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Card id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardName() {
        return this.cardName;
    }

    public Card cardName(String cardName) {
        this.setCardName(cardName);
        return this;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public CardCategory getCategory() {
        return this.category;
    }

    public Card category(CardCategory category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(CardCategory category) {
        this.category = category;
    }

    public CardNetwork getCardNetwork() {
        return this.cardNetwork;
    }

    public Card cardNetwork(CardNetwork cardNetwork) {
        this.setCardNetwork(cardNetwork);
        return this;
    }

    public void setCardNetwork(CardNetwork cardNetwork) {
        this.cardNetwork = cardNetwork;
    }

    public Bank getBank() {
        return this.bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Card bank(Bank bank) {
        this.setBank(bank);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Card)) {
            return false;
        }
        return getId() != null && getId().equals(((Card) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Card{" +
            "id=" + getId() +
            ", cardName='" + getCardName() + "'" +
            ", category='" + getCategory() + "'" +
            ", cardNetwork='" + getCardNetwork() + "'" +
            "}";
    }
}
