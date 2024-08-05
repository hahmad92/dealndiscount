package pk.dealndiscount.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class DealAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDealAllPropertiesEquals(Deal expected, Deal actual) {
        assertDealAutoGeneratedPropertiesEquals(expected, actual);
        assertDealAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDealAllUpdatablePropertiesEquals(Deal expected, Deal actual) {
        assertDealUpdatableFieldsEquals(expected, actual);
        assertDealUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDealAutoGeneratedPropertiesEquals(Deal expected, Deal actual) {
        assertThat(expected)
            .as("Verify Deal auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDealUpdatableFieldsEquals(Deal expected, Deal actual) {
        assertThat(expected)
            .as("Verify Deal relevant properties")
            .satisfies(e -> assertThat(e.getDiscountPercentage()).as("check discountPercentage").isEqualTo(actual.getDiscountPercentage()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getStartDate()).as("check startDate").isEqualTo(actual.getStartDate()))
            .satisfies(e -> assertThat(e.getEndDate()).as("check endDate").isEqualTo(actual.getEndDate()))
            .satisfies(e -> assertThat(e.getIsDealActive()).as("check isDealActive").isEqualTo(actual.getIsDealActive()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDealUpdatableRelationshipsEquals(Deal expected, Deal actual) {
        assertThat(expected)
            .as("Verify Deal relationships")
            .satisfies(e -> assertThat(e.getCard()).as("check card").isEqualTo(actual.getCard()))
            .satisfies(e -> assertThat(e.getStore()).as("check store").isEqualTo(actual.getStore()));
    }
}
