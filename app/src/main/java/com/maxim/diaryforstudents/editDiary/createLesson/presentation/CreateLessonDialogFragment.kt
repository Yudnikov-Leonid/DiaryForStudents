package com.maxim.diaryforstudents.editDiary.createLesson.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.maxim.diaryforstudents.core.sl.ProvideViewModel
import com.maxim.diaryforstudents.databinding.DialogNewLessonBinding

class CreateLessonDialogFragment : DialogFragment() {
    private var _binding: DialogNewLessonBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CreateLessonViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogNewLessonBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity()).setView(binding.root)

        viewModel =
            (requireActivity() as ProvideViewModel).viewModel(CreateLessonViewModel::class.java)

        binding.startTimeInputLayout.editText!!.addTextChangedListener {
            binding.startTimeInputLayout.error = ""
            binding.startTimeInputLayout.isErrorEnabled = false
        }

        binding.endTimeInputLayout.editText!!.addTextChangedListener {
            binding.endTimeInputLayout.error = ""
            binding.endTimeInputLayout.isErrorEnabled = false
        }

        binding.saveButton.setOnClickListener {
            viewModel.save(
                binding.startTimeInputLayout.editText!!.text.toString(),
                binding.endTimeInputLayout.editText!!.text.toString(),
                binding.themeInputLayout.editText!!.text.toString(),
                binding.homeworkInputLayout.editText!!.text.toString()
            )
        }

        viewModel.observe(this) {
            it.show(binding.startTimeInputLayout,
                binding.endTimeInputLayout,
                binding.themeInputLayout.editText!!,
                binding.homeworkInputLayout.editText!!,
                binding.progressBar,
                binding.saveButton,
                object : Dismiss {
                    override fun dismiss() {
                        viewModel.reloadList()
                        dialog!!.dismiss()
                    }

                    override fun provideError(message: String) {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        dialog!!.dismiss()
                    }
                })
        }

        viewModel.init(
            savedInstanceState == null,
            binding.startTimeInputLayout.editText!!,
            binding.endTimeInputLayout.editText!!,
            binding.themeInputLayout.editText!!,
            binding.homeworkInputLayout.editText!!
        )

        return builder.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.clear()
    }
}