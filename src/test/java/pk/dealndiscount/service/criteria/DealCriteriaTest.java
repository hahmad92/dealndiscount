package pk.dealndiscount.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DealCriteriaTest {

    @Test
    void newDealCriteriaHasAllFiltersNullTest() {
        var dealCriteria = new DealCriteria();
        assertThat(dealCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void dealCriteriaFluentMethodsCreatesFiltersTest() {
        var dealCriteria = new DealCriteria();

        setAllFilters(dealCriteria);

        assertThat(dealCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void dealCriteriaCopyCreatesNullFilterTest() {
        var dealCriteria = new DealCriteria();
        var copy = dealCriteria.copy();

        assertThat(dealCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(dealCriteria)
        );
    }

    @Test
    void dealCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var dealCriteria = new DealCriteria();
        setAllFilters(dealCriteria);

        var copy = dealCriteria.copy();

        assertThat(dealCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(dealCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var dealCriteria = new DealCriteria();

        assertThat(dealCriteria).hasToString("DealCriteria{}");
    }

    private static void setAllFilters(DealCriteria dealCriteria) {
        dealCriteria.id();
        dealCriteria.discountPercentage();
        dealCriteria.description();
        dealCriteria.startDate();
        dealCriteria.endDate();
        dealCriteria.isDealActive();
        dealCriteria.cardId();
        dealCriteria.storeId();
        dealCriteria.distinct();
    }

    private static Condition<DealCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDiscountPercentage()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getIsDealActive()) &&
                condition.apply(criteria.getCardId()) &&
                condition.apply(criteria.getStoreId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DealCriteria> copyFiltersAre(DealCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDiscountPercentage(), copy.getDiscountPercentage()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getIsDealActive(), copy.getIsDealActive()) &&
                condition.apply(criteria.getCardId(), copy.getCardId()) &&
                condition.apply(criteria.getStoreId(), copy.getStoreId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
