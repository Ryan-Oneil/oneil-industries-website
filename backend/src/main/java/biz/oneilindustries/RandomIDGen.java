package biz.oneilindustries;

import java.util.Random;

public class RandomIDGen {
    private static char[] base62chars =
        "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
            .toCharArray();

    private static Random random = new Random();

    public static String GetBase62(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i=0; i<length; i++)
            sb.append(base62chars[random.nextInt(62)]);

        return sb.toString();
    }

    public static String GetBase36(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i=0; i<length; i++)
            sb.append(base62chars[random.nextInt(36)]);

        return sb.toString();
    }
}