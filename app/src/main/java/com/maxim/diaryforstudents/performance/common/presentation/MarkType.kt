package com.maxim.diaryforstudents.performance.common.presentation

import android.view.View
import android.widget.TextView
import com.maxim.diaryforstudents.R
import java.io.Serializable

interface MarkType : Serializable {
    fun show(view: View)
    fun show(textView: TextView)

    object ControlTest : MarkType {
        override fun show(view: View) {
            view.setBackgroundResource(R.drawable.mark_control_test)
        }

        override fun show(textView: TextView) {
            textView.text = textView.context.resources.getString(R.string.control_test_type)
        }
    }

    object Current : MarkType {
        override fun show(view: View) {
            view.setBackgroundResource(R.drawable.mark_current)
        }

        override fun show(textView: TextView) {
            textView.text = textView.context.resources.getString(R.string.current_type)
        }
    }

    object Test : MarkType {
        override fun show(view: View) {
            view.setBackgroundResource(R.drawable.mark_test)
        }

        override fun show(textView: TextView) {
            textView.text = textView.context.resources.getString(R.string.test_type)
        }
    }

    object Practical : MarkType {
        override fun show(view: View) {
            view.setBackgroundResource(R.drawable.mark_test)
        }

        override fun show(textView: TextView) {
            textView.text = textView.context.resources.getString(R.string.practical_test_type)
        }
    }

    object Laboratory : MarkType {
        override fun show(view: View) {
            view.setBackgroundResource(R.drawable.mark_test)
        }

        override fun show(textView: TextView) {
            textView.text = textView.context.resources.getString(R.string.laboratory_test_type)
        }
    }
}