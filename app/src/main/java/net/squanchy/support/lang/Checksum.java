package net.squanchy.support.lang;

import java.io.UnsupportedEncodingException;
import java.util.zip.CRC32;

public class Checksum {

    private final CRC32 checksum = new CRC32();

    public long getChecksumOf(String data) {
        try {
            checksum.update(data.getBytes("UTF-8"));
            return checksum.getValue();
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException("Unable to retrieve UTF-8 encoding, something fishy is going on", e);
        } finally {
            checksum.reset();
        }
    }
}
