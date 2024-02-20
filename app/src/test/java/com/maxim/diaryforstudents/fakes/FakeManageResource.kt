package com.maxim.diaryforstudents.fakes

import com.maxim.diaryforstudents.core.sl.ManageResource

class FakeManageResources(private val value: String) : ManageResource {
    override fun string(key: Int) = value
    override fun string(key: Int, format: String) = value
}