package net.squanchy.service

import net.squanchy.service.repository.GoogleData
import net.squanchy.service.repository.User

fun aGoogleData(
    name: String? = null,
    photoUrl: String? = null
) = GoogleData(displayName = name ?: "aName aSurname", photoUrl = photoUrl)

fun aUser(
    id: String? = null,
    isAnonymous: Boolean? = false,
    email: String? = null,
    googleData: GoogleData? = aGoogleData()
) = User(id = id ?: "id", isAnonymous = isAnonymous ?: false, email = email, googleData = googleData)
