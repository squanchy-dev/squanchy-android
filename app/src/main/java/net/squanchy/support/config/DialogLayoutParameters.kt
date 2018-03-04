package net.squanchy.support.config

import android.app.Activity
import android.view.ViewGroup
import android.view.Window

class DialogLayoutParameters private constructor(
        private val formFactorChecker: FormFactorChecker,
        private val height: Int
) {

    fun applyTo(window: Window) {
        if (formFactorChecker.isTablet) {
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, height)
        } else {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height)
        }
    }

    companion object {

        fun fullHeight(activity: Activity): DialogLayoutParameters {
            return DialogLayoutParameters(
                    FormFactorChecker(activity),
                    ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        fun wrapHeight(activity: Activity): DialogLayoutParameters {
            return DialogLayoutParameters(
                    FormFactorChecker(activity),
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}
