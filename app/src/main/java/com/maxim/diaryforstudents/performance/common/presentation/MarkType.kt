package com.maxim.diaryforstudents.performance.common.presentation

import android.view.View
import com.maxim.diaryforstudents.R
import java.io.Serializable

interface MarkType : Serializable {
    fun show(view: View)

    object ControlTest : MarkType {
        override fun show(view: View) {
            view.setBackgroundResource(R.drawable.mark_control_test)
        }
    }

    object Current : MarkType {
        override fun show(view: View) {
            view.setBackgroundResource(R.drawable.mark_current)
        }
    }

    object Test : MarkType {
        override fun show(view: View) {
            view.setBackgroundResource(R.drawable.mark_test)
        }
    }

    object Practical : MarkType {
        override fun show(view: View) {
            view.setBackgroundResource(R.drawable.mark_practical)
        }
    }

    object Laboratory : MarkType {
        override fun show(view: View) {
            view.setBackgroundResource(R.drawable.mark_laboratory)
        }
    }
}