package com.maxim.diaryforstudents.profile.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(), ShowEmail,
    ShowGradeInfo, ShowSchoolInfo {
    private val viewModel: ProfileViewModel by viewModels()

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.goBack()
            }
        }
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            viewModel.goBack()
        }

        binding.showEmailButton.setOnClickListener {
            viewModel.email(this)
        }
        binding.showSchoolInfoButton.setOnClickListener {
            viewModel.school(this)
        }
        binding.showGradeButton.setOnClickListener {
            viewModel.grade(this)
        }
        binding.signOutButton.setOnClickListener {
            viewModel.signOut()
        }

        viewModel.observe(this) {
            it.show(
                binding.nameTextView,
            )
        }

        viewModel.init()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(BundleWrapper.Base(outState))
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { viewModel.restore(BundleWrapper.Base(it)) }
    }

    override fun showEmail(email: String) {
        AlertDialog.Builder(requireContext()).setTitle(resources.getString(R.string.your_email))
            .setMessage(email)
            .setPositiveButton(resources.getString(R.string.close)) { _, _ -> }.show()
    }

    override fun showSchool(schoolName: String) {
        AlertDialog.Builder(requireContext()).setTitle(resources.getString(R.string.your_school))
            .setMessage(schoolName)
            .setPositiveButton(resources.getString(R.string.close)) { _, _ -> }.show()
    }

    override fun showGrade(grade: String, gradeHeadName: String) {
        AlertDialog.Builder(requireContext()).setTitle(resources.getString(R.string.your_grade))
            .setMessage("${resources.getString(R.string.grade_name)} $grade\n\n${resources.getString(R.string.grade_head)} $gradeHeadName")
            .setPositiveButton(resources.getString(R.string.close)) { _, _ -> }.show()
    }
}

interface ShowEmail {
    fun showEmail(email: String)
}

interface ShowSchoolInfo {
    fun showSchool(schoolName: String)
}

interface ShowGradeInfo {
    fun showGrade(grade: String, gradeHeadName: String)
}