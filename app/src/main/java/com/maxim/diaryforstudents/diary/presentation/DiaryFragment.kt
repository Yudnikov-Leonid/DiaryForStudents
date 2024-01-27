package com.maxim.diaryforstudents.diary.presentation

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import com.maxim.diaryforstudents.core.presentation.BaseFragment
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

        val lessonsAdapter = DiaryLessonsAdapter(object : DiaryLessonsAdapter.Listener {
            override fun openDetails(item: DiaryUi.Lesson) {
                viewModel.openDetails(item)
            }
        })
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
                .setItems(arrayOf("Actual", "Previous")) { _, i ->
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
            val editText = EditText(requireContext()).apply {
                hint = "Name filter"
                inputType = (InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
                maxLines = 1
            }
            editText.setText(viewModel.nameFilter())
            editText.addTextChangedListener {
                viewModel.setNameFilter(editText.text.toString())
            }
            AlertDialog.Builder(requireContext())
                .setTitle("Set filters")
                .setMultiChoiceItems(
                    arrayOf("Have homework", "Have topic", "Have marks"),
                    viewModel.checks()
                ) { _, i, isChecked ->
                    viewModel.setFilter(i, isChecked)
                }.setView(editText).create().show()
        }

        binding.homeworkTypeButton!!.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Homework type")
                .setSingleChoiceItems(
                    arrayOf("Actual", "Previous"),
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

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        viewModel.save(BundleWrapper.Base(outState))
//    }
//
//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//        savedInstanceState?.let { viewModel.restore(BundleWrapper.Base(it)) }
//    }
}