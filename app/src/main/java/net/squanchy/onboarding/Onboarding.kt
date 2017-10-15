package net.squanchy.onboarding

class Onboarding(private val persister: OnboardingPersister) {

    fun nextPageToShow(): OnboardingPage? =
            OnboardingPage.values()
                    .asList()
                    .firstOrNull { it.canShow() }

    private fun OnboardingPage.canShow() = !persister.pageSeen(this)

    fun savePageSeen(page: OnboardingPage) {
        persister.savePageSeen(page)
    }
}
