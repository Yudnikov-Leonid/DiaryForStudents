package com.maxim.diaryforstudents.login.data

import com.maxim.diaryforstudents.BuildConfig
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import com.maxim.diaryforstudents.core.service.EduUser
import com.maxim.diaryforstudents.selectUser.presentation.SelectUserUi
import java.io.Serializable

interface LoginRepository : SaveAndRestore {
    suspend fun login(login: String, password: String): LoginResult
    fun users(): List<SelectUserUi>
    fun select(position: Int)

    class Base(
        private val service: LoginService,
        private val eduUser: EduUser,
    ) :
        LoginRepository {
        private val usersList = mutableListOf<LoginSchools>()
        private var cachedEmail = ""

        override suspend fun login(login: String, password: String): LoginResult {
            return try {
                val data = service.login(LoginBody(BuildConfig.API_KEY, login, password))
                if (data.success) {
                    usersList.clear()
                    usersList.addAll(data.data.SCHOOLS)
                    cachedEmail = data.data.EMAIL
                    LoginResult.Success
                } else {
                    LoginResult.Failure(data.message)
                }
            } catch (e: Exception) {
                LoginResult.Failure(e.message!!)
            }
        }

        override fun users(): List<SelectUserUi> {
            return usersList.map {
                SelectUserUi.Base(
                    "${it.PARTICIPANT.SURNAME} ${it.PARTICIPANT.NAME} ${it.PARTICIPANT.SECONDNAME}",
                    it.PARTICIPANT.GRADE.SCHOOL.SHORT_NAME
                )
            }
        }

        override fun select(position: Int) {
            val user = usersList[position]
            val guid = user.PARTICIPANT.SYS_GUID
            val fullName =
                "${user.PARTICIPANT.SURNAME} ${user.PARTICIPANT.NAME} ${user.PARTICIPANT.SECONDNAME}"
            val grade = user.PARTICIPANT.GRADE.NAME
            val school = user.PARTICIPANT.GRADE.SCHOOL.SHORT_NAME
            val gradeHead = user.PARTICIPANT.GRADE.GRADE_HEAD
            val gradeHeadName =
                "${gradeHead.SURNAME} ${gradeHead.NAME} ${gradeHead.SECONDNAME}"
            val apikey = when {
                fullName.startsWith("Ю") -> BuildConfig.ONE_SHORT_API_KEY
                fullName.startsWith("К") -> BuildConfig.TWO_SHORT_API_KEY
                fullName.startsWith("М") -> BuildConfig.THREE_SHORT_API_KEY
                fullName.startsWith("Л") -> BuildConfig.FOUR_SHORT_API_KEY
                else -> ""
            }
            eduUser.login(guid, apikey, cachedEmail, fullName, school, grade,  gradeHeadName)
            usersList.clear()
        }

        override fun save(bundleWrapper: BundleWrapper.Save) {
            bundleWrapper.save(RESTORE_KEY, LoginUsersList(usersList.toTypedArray()))
            bundleWrapper.save(EMAIL_RESTORE_KEY, cachedEmail)
        }

        override fun restore(bundleWrapper: BundleWrapper.Restore) {
            usersList.clear()
            val item = bundleWrapper.restore(RESTORE_KEY) as? LoginUsersList
            usersList.addAll(
                item?.list?.toList() ?: emptyList()
            )
            cachedEmail = bundleWrapper.restore(EMAIL_RESTORE_KEY) ?: ""
        }

        companion object {
            private const val RESTORE_KEY = "login_repository_users_restore_key"
            private const val EMAIL_RESTORE_KEY = "login_repository_email_restore_key"
        }
    }
}

class LoginUsersList(val list: Array<LoginSchools>) : Serializable