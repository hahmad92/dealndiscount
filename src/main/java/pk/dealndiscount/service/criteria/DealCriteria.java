package pk.dealndiscount.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link pk.dealndiscount.domain.Deal} entity. This class is used
 * in {@link pk.dealndiscount.web.rest.DealResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /deals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DealCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter discountPercentage;

    private StringFilter description;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private BooleanFilter isDealActive;

    private LongFilter cardId;

    private LongFilter storeId;

    private Boolean distinct;

    public DealCriteria() {}

    public DealCriteria(DealCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.discountPercentage = other.optionalDiscountPercentage().map(IntegerFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(InstantFilter::copy).orElse(null);
        this.isDealActive = other.optionalIsDealActive().map(BooleanFilter::copy).orElse(null);
        this.cardId = other.optionalCardId().map(LongFilter::copy).orElse(null);
        this.storeId = other.optionalStoreId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DealCriteria copy() {
        return new DealCriteria(this);
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

    public IntegerFilter getDiscountPercentage() {
        return discountPercentage;
    }

    public Optional<IntegerFilter> optionalDiscountPercentage() {
        return Optional.ofNullable(discountPercentage);
    }

    public IntegerFilter discountPercentage() {
        if (discountPercentage == null) {
            setDiscountPercentage(new IntegerFilter());
        }
        return discountPercentage;
    }

    public void setDiscountPercentage(IntegerFilter discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public InstantFilter getStartDate() {
        return startDate;
    }

    public Optional<InstantFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public InstantFilter startDate() {
        if (startDate == null) {
            setStartDate(new InstantFilter());
        }
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getEndDate() {
        return endDate;
    }

    public Optional<InstantFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public InstantFilter endDate() {
        if (endDate == null) {
            setEndDate(new InstantFilter());
        }
        return endDate;
    }

    public void setEndDate(InstantFilter endDate) {
        this.endDate = endDate;
    }

    public BooleanFilter getIsDealActive() {
        return isDealActive;
    }

    public Optional<BooleanFilter> optionalIsDealActive() {
        return Optional.ofNullable(isDealActive);
    }

    public BooleanFilter isDealActive() {
        if (isDealActive == null) {
            setIsDealActive(new BooleanFilter());
        }
        return isDealActive;
    }

    public void setIsDealActive(BooleanFilter isDealActive) {
        this.isDealActive = isDealActive;
    }

    public LongFilter getCardId() {
        return cardId;
    }

    public Optional<LongFilter> optionalCardId() {
        return Optional.ofNullable(cardId);
    }

    public LongFilter cardId() {
        if (cardId == null) {
            setCardId(new LongFilter());
        }
        return cardId;
    }

    public void setCardId(LongFilter cardId) {
        this.cardId = cardId;
    }

    public LongFilter getStoreId() {
        return storeId;
    }

    public Optional<LongFilter> optionalStoreId() {
        return Optional.ofNullable(storeId);
    }

    public LongFilter storeId() {
        if (storeId == null) {
            setStoreId(new LongFilter());
        }
        return storeId;
    }

    public void setStoreId(LongFilter storeId) {
        this.storeId = storeId;
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
        final DealCriteria that = (DealCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(discountPercentage, that.discountPercentage) &&
            Objects.equals(description, that.description) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(isDealActive, that.isDealActive) &&
            Objects.equals(cardId, that.cardId) &&
            Objects.equals(storeId, that.storeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, discountPercentage, description, startDate, endDate, isDealActive, cardId, storeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DealCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDiscountPercentage().map(f -> "discountPercentage=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalIsDealActive().map(f -> "isDealActive=" + f + ", ").orElse("") +
            optionalCardId().map(f -> "cardId=" + f + ", ").orElse("") +
            optionalStoreId().map(f -> "storeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
