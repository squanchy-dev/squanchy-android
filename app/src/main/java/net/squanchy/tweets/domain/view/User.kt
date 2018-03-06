package net.squanchy.tweets.domain.view

data class User(
    val name: String,
    val screenName: String,
    val photoUrl: String
) {

    companion object {

        fun create(name: String, screenName: String, photoUrl: String) = User(name, screenName, photoUrl)
    }
}
