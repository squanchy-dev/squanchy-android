package net.squanchy.onboarding.loldon

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_onboarding_loldon.onboardingSignInButton
import net.squanchy.R
import net.squanchy.fonts.TypefaceStyleableActivity
import net.squanchy.navigation.Navigator
import net.squanchy.onboarding.Onboarding
import net.squanchy.onboarding.OnboardingPage
import net.squanchy.support.view.enableLightNavigationBar

class LoldonOnboardingActivity : TypefaceStyleableActivity() {

    private lateinit var onboarding: Onboarding
    private lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val component = loldonOnboardingComponent(this)
        onboarding = component.onboarding()
        navigator = component.navigator()

        setContentView(R.layout.activity_onboarding_loldon)
        enableLightNavigationBar(this)

        onboardingSignInButton.setOnClickListener { markPageAsSeenAndFinish() }

        setResult(Activity.RESULT_CANCELED)
    }

    private fun markPageAsSeenAndFinish() {
        onboarding.savePageSeen(OnboardingPage.DISCLAIMER)
        setResult(Activity.RESULT_OK)
        finish()
    }
}
