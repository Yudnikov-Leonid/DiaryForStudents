package com.maxim.diaryforstudents

import com.maxim.diaryforstudents.core.ManageResource

class FakeManageResources(private val value: String) : ManageResource {
    override fun string(key: Int) = value
}