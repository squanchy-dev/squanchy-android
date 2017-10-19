package net.squanchy.onboarding

import android.app.Activity

import net.squanchy.onboarding.account.AccountOnboardingActivity
import net.squanchy.onboarding.loldon.LoldonOnboardingActivity

enum class OnboardingPage(val activityClass: Class<out Activity>) {

    DISCLAIMER(LoldonOnboardingActivity::class.java),
    ACCOUNT(AccountOnboardingActivity::class.java)

}
