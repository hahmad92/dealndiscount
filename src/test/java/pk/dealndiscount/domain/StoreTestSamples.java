package pk.dealndiscount.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StoreTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Store getStoreSample1() {
        return new Store().id(1L).storeName("storeName1").address("address1").phone("phone1").geoLocation("geoLocation1");
    }

    public static Store getStoreSample2() {
        return new Store().id(2L).storeName("storeName2").address("address2").phone("phone2").geoLocation("geoLocation2");
    }

    public static Store getStoreRandomSampleGenerator() {
        return new Store()
            .id(longCount.incrementAndGet())
            .storeName(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .geoLocation(UUID.randomUUID().toString());
    }
}
