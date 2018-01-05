package net.squanchy.onboarding

import android.app.Activity

import net.squanchy.onboarding.account.AccountOnboardingActivity

enum class OnboardingPage(val activityClass: Class<out Activity>) {

    ACCOUNT(AccountOnboardingActivity::class.java);
}
