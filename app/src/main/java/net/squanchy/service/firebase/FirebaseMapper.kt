package net.squanchy.service.firebase

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo
import net.squanchy.service.repository.GoogleData
import net.squanchy.service.repository.User

private const val PROVIDER_ID_GOOGLE = "google.com"

fun FirebaseUser.toUser(): User {
    val googleData = providerData.extractGoogleData()
    return User(uid, isAnonymous, email, googleData)
}

private fun List<UserInfo>.extractGoogleData(): GoogleData? {
    val googleProviderData = filter { data -> PROVIDER_ID_GOOGLE.equals(data.providerId, ignoreCase = true) }
    return googleProviderData.firstOrNull()?.let { GoogleData(it.displayName, it.photoUrl?.toString()) }
}
