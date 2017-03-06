package net.squanchy.support.lang;

public final class Ids {

    public static int safelyConvertIdToInt(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
