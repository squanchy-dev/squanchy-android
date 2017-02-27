package net.squanchy.support.lang;


public final class Ids {

    public static long safelyConvertId(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
