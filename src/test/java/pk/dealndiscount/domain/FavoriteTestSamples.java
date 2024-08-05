package pk.dealndiscount.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FavoriteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Favorite getFavoriteSample1() {
        return new Favorite().id(1L).userId("userId1").storeId("storeId1").cityId("cityId1").cardId("cardId1");
    }

    public static Favorite getFavoriteSample2() {
        return new Favorite().id(2L).userId("userId2").storeId("storeId2").cityId("cityId2").cardId("cardId2");
    }

    public static Favorite getFavoriteRandomSampleGenerator() {
        return new Favorite()
            .id(longCount.incrementAndGet())
            .userId(UUID.randomUUID().toString())
            .storeId(UUID.randomUUID().toString())
            .cityId(UUID.randomUUID().toString())
            .cardId(UUID.randomUUID().toString());
    }
}
