package pk.dealndiscount.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import pk.dealndiscount.domain.enumeration.CardCategory;
import pk.dealndiscount.domain.enumeration.CardNetwork;

/**
 * A DTO for the {@link pk.dealndiscount.domain.Card} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CardDTO implements Serializable {

    private Long id;

    @NotNull
    private String cardName;

    @NotNull
    private CardCategory category;

    @NotNull
    private CardNetwork cardNetwork;

    private BankDTO bank;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public CardCategory getCategory() {
        return category;
    }

    public void setCategory(CardCategory category) {
        this.category = category;
    }

    public CardNetwork getCardNetwork() {
        return cardNetwork;
    }

    public void setCardNetwork(CardNetwork cardNetwork) {
        this.cardNetwork = cardNetwork;
    }

    public BankDTO getBank() {
        return bank;
    }

    public void setBank(BankDTO bank) {
        this.bank = bank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardDTO)) {
            return false;
        }

        CardDTO cardDTO = (CardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardDTO{" +
            "id=" + getId() +
            ", cardName='" + getCardName() + "'" +
            ", category='" + getCategory() + "'" +
            ", cardNetwork='" + getCardNetwork() + "'" +
            ", bank=" + getBank() +
            "}";
    }
}
