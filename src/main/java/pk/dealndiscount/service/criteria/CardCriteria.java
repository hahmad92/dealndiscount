package pk.dealndiscount.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import pk.dealndiscount.domain.enumeration.CardCategory;
import pk.dealndiscount.domain.enumeration.CardNetwork;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link pk.dealndiscount.domain.Card} entity. This class is used
 * in {@link pk.dealndiscount.web.rest.CardResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cards?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CardCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CardCategory
     */
    public static class CardCategoryFilter extends Filter<CardCategory> {

        public CardCategoryFilter() {}

        public CardCategoryFilter(CardCategoryFilter filter) {
            super(filter);
        }

        @Override
        public CardCategoryFilter copy() {
            return new CardCategoryFilter(this);
        }
    }

    /**
     * Class for filtering CardNetwork
     */
    public static class CardNetworkFilter extends Filter<CardNetwork> {

        public CardNetworkFilter() {}

        public CardNetworkFilter(CardNetworkFilter filter) {
            super(filter);
        }

        @Override
        public CardNetworkFilter copy() {
            return new CardNetworkFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter cardName;

    private CardCategoryFilter category;

    private CardNetworkFilter cardNetwork;

    private LongFilter bankId;

    private Boolean distinct;

    public CardCriteria() {}

    public CardCriteria(CardCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.cardName = other.optionalCardName().map(StringFilter::copy).orElse(null);
        this.category = other.optionalCategory().map(CardCategoryFilter::copy).orElse(null);
        this.cardNetwork = other.optionalCardNetwork().map(CardNetworkFilter::copy).orElse(null);
        this.bankId = other.optionalBankId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CardCriteria copy() {
        return new CardCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCardName() {
        return cardName;
    }

    public Optional<StringFilter> optionalCardName() {
        return Optional.ofNullable(cardName);
    }

    public StringFilter cardName() {
        if (cardName == null) {
            setCardName(new StringFilter());
        }
        return cardName;
    }

    public void setCardName(StringFilter cardName) {
        this.cardName = cardName;
    }

    public CardCategoryFilter getCategory() {
        return category;
    }

    public Optional<CardCategoryFilter> optionalCategory() {
        return Optional.ofNullable(category);
    }

    public CardCategoryFilter category() {
        if (category == null) {
            setCategory(new CardCategoryFilter());
        }
        return category;
    }

    public void setCategory(CardCategoryFilter category) {
        this.category = category;
    }

    public CardNetworkFilter getCardNetwork() {
        return cardNetwork;
    }

    public Optional<CardNetworkFilter> optionalCardNetwork() {
        return Optional.ofNullable(cardNetwork);
    }

    public CardNetworkFilter cardNetwork() {
        if (cardNetwork == null) {
            setCardNetwork(new CardNetworkFilter());
        }
        return cardNetwork;
    }

    public void setCardNetwork(CardNetworkFilter cardNetwork) {
        this.cardNetwork = cardNetwork;
    }

    public LongFilter getBankId() {
        return bankId;
    }

    public Optional<LongFilter> optionalBankId() {
        return Optional.ofNullable(bankId);
    }

    public LongFilter bankId() {
        if (bankId == null) {
            setBankId(new LongFilter());
        }
        return bankId;
    }

    public void setBankId(LongFilter bankId) {
        this.bankId = bankId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CardCriteria that = (CardCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cardName, that.cardName) &&
            Objects.equals(category, that.category) &&
            Objects.equals(cardNetwork, that.cardNetwork) &&
            Objects.equals(bankId, that.bankId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cardName, category, cardNetwork, bankId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCardName().map(f -> "cardName=" + f + ", ").orElse("") +
            optionalCategory().map(f -> "category=" + f + ", ").orElse("") +
            optionalCardNetwork().map(f -> "cardNetwork=" + f + ", ").orElse("") +
            optionalBankId().map(f -> "bankId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
