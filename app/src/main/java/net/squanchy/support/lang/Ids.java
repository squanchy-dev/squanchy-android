package net.squanchy.support.lang;


public final class Ids {

    public static long safelyConvertIdToLong(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static int safelyConvertIdToInt(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
