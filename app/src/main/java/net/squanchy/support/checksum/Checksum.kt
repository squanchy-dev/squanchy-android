package net.squanchy.support.checksum

import java.io.UnsupportedEncodingException
import java.util.zip.CRC32

class Checksum {

    private val crc32 = CRC32()

    fun getChecksumOf(data: String): Long {
        try {
            crc32.update(data.toByteArray(charset("UTF-8")))
            return crc32.value
        } catch (e: UnsupportedEncodingException) {
            throw UnsupportedOperationException("Unable to retrieve UTF-8 encoding, something fishy is going on", e)
        } finally {
            crc32.reset()
        }
    }
}
