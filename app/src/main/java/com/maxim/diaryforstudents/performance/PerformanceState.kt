package com.maxim.diaryforstudents.performance

import android.content.res.Resources
import android.widget.Button
import com.maxim.diaryforstudents.R

interface PerformanceState {
    fun show(
        first: Button,
        second: Button,
        third: Button,
        fourth: Button,
        adapter: PerformanceLessonsAdapter,
        resourceManager: Resources
    )

    data class Base(
        private val quarter: Int,
        private val lessons: List<PerformanceUi>
    ) : PerformanceState {
        override fun show(
            first: Button,
            second: Button,
            third: Button,
            fourth: Button,
            adapter: PerformanceLessonsAdapter,
            resourceManager: Resources
        ) {
            //todo deprecated
            first.setBackgroundColor(
                if (quarter == 1) resourceManager.getColor(R.color.blue) else resourceManager.getColor(
                    R.color.back_blue
                )
            )
            second.setBackgroundColor(
                if (quarter == 2) resourceManager.getColor(R.color.blue) else resourceManager.getColor(
                    R.color.back_blue
                )
            )
            third.setBackgroundColor(
                if (quarter == 3) resourceManager.getColor(R.color.blue) else resourceManager.getColor(
                    R.color.back_blue
                )
            )
            fourth.setBackgroundColor(
                if (quarter == 4) resourceManager.getColor(R.color.blue) else resourceManager.getColor(
                    R.color.back_blue
                )
            )
            adapter.update(lessons as List<PerformanceUi.Lesson>)
        }
    }
}