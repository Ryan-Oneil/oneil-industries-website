package biz.oneilenterprise.website.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class RandomIDGenUtilTest {

    @Test
    public void getRandomStringTest() {
        String randomId = RandomIDGenUtil.getBase62(16);

        assertThat(randomId.length()).isEqualTo(16);
    }
}
