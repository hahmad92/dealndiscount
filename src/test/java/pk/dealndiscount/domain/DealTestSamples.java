package pk.dealndiscount.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DealTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Deal getDealSample1() {
        return new Deal().id(1L).discountPercentage(1).description("description1");
    }

    public static Deal getDealSample2() {
        return new Deal().id(2L).discountPercentage(2).description("description2");
    }

    public static Deal getDealRandomSampleGenerator() {
        return new Deal()
            .id(longCount.incrementAndGet())
            .discountPercentage(intCount.incrementAndGet())
            .description(UUID.randomUUID().toString());
    }
}
