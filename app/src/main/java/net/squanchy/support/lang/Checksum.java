package net.squanchy.support.lang;

import java.util.zip.CRC32;

public class Checksum {

    private final CRC32 checksum = new CRC32();

    public long getChecksumOf(String data) {
        checksum.update(data.getBytes());
        try {
            return checksum.getValue();
        } finally {
            checksum.reset();
        }
    }
}
