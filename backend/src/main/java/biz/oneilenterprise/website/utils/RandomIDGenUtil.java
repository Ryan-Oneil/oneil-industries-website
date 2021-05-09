package biz.oneilenterprise.website.utils;

import java.util.Random;

public class RandomIDGenUtil {

    private static final char[] base62chars =
        "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
            .toCharArray();

    private static final Random random = new Random();

    private RandomIDGenUtil() {
    }

    public static String getBase62(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i=0; i<length; i++)
            sb.append(base62chars[random.nextInt(62)]);

        return sb.toString();
    }
}