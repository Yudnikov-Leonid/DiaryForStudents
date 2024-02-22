package com.maxim.diaryforstudents.performance.common.presentation

import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.maxim.diaryforstudents.R
import java.io.Serializable

interface MarkType: Serializable {
    fun show(view: View)

    object ControlTest : MarkType {
        override fun show(view: View) {
            view.background = ResourcesCompat.getDrawable(
                view.resources,
                R.drawable.mark_control_test,
                view.context.theme
            )
        }
    }

    object Current : MarkType {
        override fun show(view: View) {
            view.background = ResourcesCompat.getDrawable(
                view.resources,
                R.drawable.mark_current,
                view.context.theme
            )
        }
    }

    object Test : MarkType {
        override fun show(view: View) {
            view.background = ResourcesCompat.getDrawable(
                view.resources,
                R.drawable.mark_test,
                view.context.theme
            )
        }
    }

    object Practical : MarkType {
        override fun show(view: View) {
            view.background = ResourcesCompat.getDrawable(
                view.resources,
                R.drawable.mark_practical,
                view.context.theme
            )
        }
    }

    object Laboratory : MarkType {
        override fun show(view: View) {
            view.background = ResourcesCompat.getDrawable(
                view.resources,
                R.drawable.mark_laboratory,
                view.context.theme
            )
        }
    }
}