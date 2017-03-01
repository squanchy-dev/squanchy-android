package net.squanchy.support.lang;

import java.util.zip.CRC32;

public final class Ids {

    public static int safelyConvertIdToInt(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static long checksumOf(String id) {
        CRC32 checksum = new CRC32();
        checksum.update(id.getBytes());
        return checksum.getValue();
    }
}
