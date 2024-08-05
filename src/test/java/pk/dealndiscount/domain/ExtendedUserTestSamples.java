package pk.dealndiscount.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ExtendedUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ExtendedUser getExtendedUserSample1() {
        return new ExtendedUser().id(1L);
    }

    public static ExtendedUser getExtendedUserSample2() {
        return new ExtendedUser().id(2L);
    }

    public static ExtendedUser getExtendedUserRandomSampleGenerator() {
        return new ExtendedUser().id(longCount.incrementAndGet());
    }
}
