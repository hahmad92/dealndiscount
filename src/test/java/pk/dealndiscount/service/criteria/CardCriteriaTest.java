package pk.dealndiscount.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CardCriteriaTest {

    @Test
    void newCardCriteriaHasAllFiltersNullTest() {
        var cardCriteria = new CardCriteria();
        assertThat(cardCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void cardCriteriaFluentMethodsCreatesFiltersTest() {
        var cardCriteria = new CardCriteria();

        setAllFilters(cardCriteria);

        assertThat(cardCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void cardCriteriaCopyCreatesNullFilterTest() {
        var cardCriteria = new CardCriteria();
        var copy = cardCriteria.copy();

        assertThat(cardCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(cardCriteria)
        );
    }

    @Test
    void cardCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cardCriteria = new CardCriteria();
        setAllFilters(cardCriteria);

        var copy = cardCriteria.copy();

        assertThat(cardCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(cardCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cardCriteria = new CardCriteria();

        assertThat(cardCriteria).hasToString("CardCriteria{}");
    }

    private static void setAllFilters(CardCriteria cardCriteria) {
        cardCriteria.id();
        cardCriteria.cardName();
        cardCriteria.category();
        cardCriteria.cardNetwork();
        cardCriteria.bankId();
        cardCriteria.distinct();
    }

    private static Condition<CardCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCardName()) &&
                condition.apply(criteria.getCategory()) &&
                condition.apply(criteria.getCardNetwork()) &&
                condition.apply(criteria.getBankId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CardCriteria> copyFiltersAre(CardCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCardName(), copy.getCardName()) &&
                condition.apply(criteria.getCategory(), copy.getCategory()) &&
                condition.apply(criteria.getCardNetwork(), copy.getCardNetwork()) &&
                condition.apply(criteria.getBankId(), copy.getBankId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
