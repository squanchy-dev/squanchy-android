package net.squanchy.extensions

inline fun Boolean.whenTrue(block: () -> Unit) {
    if (this) {
        block()
    }
}
