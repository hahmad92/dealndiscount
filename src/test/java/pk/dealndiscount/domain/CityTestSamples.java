package pk.dealndiscount.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static City getCitySample1() {
        return new City().id(1L).cityName("cityName1");
    }

    public static City getCitySample2() {
        return new City().id(2L).cityName("cityName2");
    }

    public static City getCityRandomSampleGenerator() {
        return new City().id(longCount.incrementAndGet()).cityName(UUID.randomUUID().toString());
    }
}
