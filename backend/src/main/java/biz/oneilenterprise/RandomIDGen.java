package biz.oneilenterprise;

import java.util.Random;

public class RandomIDGen {

    private RandomIDGen() {
    }

    private static char[] base62chars =
        "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
            .toCharArray();

    private static Random random = new Random();

    public static String getBase62(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i=0; i<length; i++)
            sb.append(base62chars[random.nextInt(62)]);

        return sb.toString();
    }

    public static String getBase36(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i=0; i<length; i++)
            sb.append(base62chars[random.nextInt(36)]);

        return sb.toString();
    }
}