package com.maxim.diaryforstudents.menu.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.App
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.databinding.FragmentMenuBinding
import com.maxim.diaryforstudents.databinding.FragmentMenuNyBinding
import com.maxim.diaryforstudents.diary.presentation.DiaryUi
import com.maxim.diaryforstudents.main.MainActivity

class MenuFragment : Fragment() {
    private lateinit var viewModel: MenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate((context?.applicationContext as App?)?.getCore()?.currentThemeSettings()?.readTheme()?.getMenuLayoutId() ?: R.layout.fragment_menu_ny, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel(MenuViewModel::class.java)

        view.findViewById<View>(R.id.diaryButton).setOnClickListener {
            viewModel.diary()
        }
        view.findViewById<View>(R.id.performanceButton).setOnClickListener {
            viewModel.performance()
        }
        view.findViewById<View>(R.id.profileButton).setOnClickListener {
            viewModel.profile()
        }
        view.findViewById<View>(R.id.newsButton).setOnClickListener {
            viewModel.news()
            viewModel.reload()
        }
        view.findViewById<View>(R.id.analyticsButton).setOnClickListener {
            viewModel.analytics()
        }
        view.findViewById<View>(R.id.settingsButton).setOnClickListener {
            viewModel.settings()
        }

        val adapter = MenuLessonsAdapter(object : MenuLessonsAdapter.Listener {
            override fun details(item: DiaryUi.Lesson) {
                viewModel.lesson(item)
            }
        })
        view.findViewById<ViewPager2>(R.id.lessonsViewPager).adapter = adapter

        viewModel.observe(this) {
            it.showNewsCount(view.findViewById(R.id.newNewsCounter))
            it.showMarksCount(view.findViewById(R.id.newMarksCounter))
            it.showLessons(view.findViewById(R.id.lessonsViewPager), adapter)
            it.showProgressBar(view.findViewById(R.id.progressBar))
        }

        viewModel.init(savedInstanceState == null)
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateLessons()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(BundleWrapper.Base(outState))
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            viewModel.restore(BundleWrapper.Base(it))
        }
    }
}