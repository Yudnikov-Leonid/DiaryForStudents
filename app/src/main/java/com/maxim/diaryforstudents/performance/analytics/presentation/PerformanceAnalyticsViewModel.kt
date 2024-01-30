package com.maxim.diaryforstudents.performance.analytics.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Init

class PerformanceAnalyticsViewModel(
    private val communication: AnalyticsCommunication
) : BaseViewModel(), Communication.Observe<AnalyticsState>, Init {

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun)
            communication.update(
                AnalyticsState.Base(
                    listOf(
                        AnalyticsUi.Base(
                            listOf(
                                Pair(0f, 3.5f), //start
                                Pair(1f, 3.7f),
                                Pair(2f, 3.4f),
                                Pair(3f, 4.0f),
                                Pair(4f, 4.4f),
                                Pair(5f, 4.3f),
                                Pair(6f, 4.1f),
                                Pair(7f, 3.9f),
                                Pair(8f, 3.8f),
                            ),
                            listOf(
                                "",
                                "1 week",
                                "2 week",
                                "3 week",
                                "4 week",
                                "5 week",
                                "6 week",
                                "7 week",
                                "8 week"
                            )
                        )
                    )
                )
            )
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<AnalyticsState>) {
        communication.observe(owner, observer)
    }
}