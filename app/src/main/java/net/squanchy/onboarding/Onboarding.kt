package net.squanchy.onboarding

import net.squanchy.support.lang.Optional

class Onboarding(private val persister: OnboardingPersister) {

    fun nextPageToShow(): Optional<OnboardingPage> =
            Optional.fromNullable(
                    OnboardingPage.values()
                            .asList()
                            .firstOrNull { page -> page.canShow() }
            )

    private fun OnboardingPage.canShow() = !persister.pageSeen(this)

    fun savePageSeen(page: OnboardingPage) {
        persister.savePageSeen(page)
    }
}
