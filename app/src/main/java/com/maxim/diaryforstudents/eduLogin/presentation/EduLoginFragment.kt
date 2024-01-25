package com.maxim.diaryforstudents.eduLogin.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.databinding.FragmentEduLoginBinding

class EduLoginFragment: BaseFragment<FragmentEduLoginBinding, EduLoginViewModel>() {
    override val viewModelClass = EduLoginViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentEduLoginBinding.inflate(inflater, container, false)
}