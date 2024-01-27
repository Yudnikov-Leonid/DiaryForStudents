package com.maxim.diaryforstudents.diary.presentation

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.databinding.FragmentDiaryBinding

class DiaryFragment : BaseFragment<FragmentDiaryBinding, DiaryViewModel>() {
    override val viewModelClass: Class<DiaryViewModel>
        get() = DiaryViewModel::class.java

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDiaryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.back()
            }
        }
        super.onViewCreated(view, savedInstanceState)

        val lessonsAdapter = DiaryLessonsAdapter()
        binding.lessonsRecyclerView.adapter = lessonsAdapter

        val daysAdapter = DiaryDaysAdapter(object : DiaryDaysAdapter.Listener {
            override fun selectDay(day: Int) {
                viewModel.setActualDay(day)
            }
        })
        binding.daysRecyclerView.adapter = daysAdapter

        binding.moveLeftButton.setOnClickListener {
            viewModel.previousDay()
        }
        binding.moveRightButton.setOnClickListener {
            viewModel.nextDay()
        }
        //todo !!
        binding.shareHomeworkButton!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
            }
            AlertDialog.Builder(requireContext())
                .setTitle("Share homework")
                .setItems(arrayOf("From", "For")) { _, i ->
                    if (i == 0) {
                        intent.putExtra(Intent.EXTRA_TEXT, viewModel.homeworkToShare())
                        startActivity(Intent.createChooser(intent, "Send to"))
                    } else {
                        intent.putExtra(Intent.EXTRA_TEXT, viewModel.previousHomeworkToShare())
                        startActivity(Intent.createChooser(intent, "Send to"))
                    }
                }.create().show()
        }


        binding.filtersButton!!.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Set filters")
                .setMultiChoiceItems(
                    arrayOf("Have homework", "Have topic", "Have marks"),
                    viewModel.checks()
                ) { _, i, isChecked ->
                    viewModel.setFilter(i, isChecked)
                }.create().show()
        }

        binding.homeworkTypeButton!!.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Homewrok type")
                .setSingleChoiceItems(
                    arrayOf("From", "For"),
                    if (viewModel.homeworkFrom()) 0 else 1
                ) { _, i ->
                    viewModel.setHomeworkType(i == 0)
                }.create().show()
        }

        viewModel.observe(this) {
            it.show(
                lessonsAdapter,
                daysAdapter,
                binding.shareHomeworkButton!!,
                binding.filtersButton!!,
                binding.homeworkTypeButton!!,
                binding.monthTextView,
                binding.progressBar,
                binding.errorTextView,
                binding.moveLeftButton,
                binding.moveRightButton,
                binding.daysRecyclerView,
                binding.lessonsRecyclerView,
            )
        }
        viewModel.init(savedInstanceState == null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(BundleWrapper.Base(outState))
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { viewModel.restore(BundleWrapper.Base(it)) }
    }
}