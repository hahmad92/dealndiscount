package pk.dealndiscount.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class StoreAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStoreAllPropertiesEquals(Store expected, Store actual) {
        assertStoreAutoGeneratedPropertiesEquals(expected, actual);
        assertStoreAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStoreAllUpdatablePropertiesEquals(Store expected, Store actual) {
        assertStoreUpdatableFieldsEquals(expected, actual);
        assertStoreUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStoreAutoGeneratedPropertiesEquals(Store expected, Store actual) {
        assertThat(expected)
            .as("Verify Store auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStoreUpdatableFieldsEquals(Store expected, Store actual) {
        assertThat(expected)
            .as("Verify Store relevant properties")
            .satisfies(e -> assertThat(e.getStoreName()).as("check storeName").isEqualTo(actual.getStoreName()))
            .satisfies(e -> assertThat(e.getStoreType()).as("check storeType").isEqualTo(actual.getStoreType()))
            .satisfies(e -> assertThat(e.getStoreCategory()).as("check storeCategory").isEqualTo(actual.getStoreCategory()))
            .satisfies(e -> assertThat(e.getAddress()).as("check address").isEqualTo(actual.getAddress()))
            .satisfies(e -> assertThat(e.getPhone()).as("check phone").isEqualTo(actual.getPhone()))
            .satisfies(e -> assertThat(e.getGeoLocation()).as("check geoLocation").isEqualTo(actual.getGeoLocation()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStoreUpdatableRelationshipsEquals(Store expected, Store actual) {
        assertThat(expected)
            .as("Verify Store relationships")
            .satisfies(e -> assertThat(e.getCity()).as("check city").isEqualTo(actual.getCity()));
    }
}