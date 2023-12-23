package com.maxim.diaryforstudents.login.data

import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface LoginRepository {
    fun userNotLoggedIn(): Boolean
    suspend fun handleResult(authResult: AuthResultWrapper): LoginResult
    class Base(private val cloudDataSource: LoginCloudDataSource) : LoginRepository {
        override fun userNotLoggedIn() = Firebase.auth.currentUser == null

        override suspend fun handleResult(authResult: AuthResultWrapper): LoginResult {
            if (authResult.isSuccessful()) {
                try {
                    val task = authResult.task()
                    val account = task.getResult(ApiException::class.java)
                    val idToken = account.idToken

                    return if (idToken == null) {
                        LoginResult.Failed("something went wrong")
                    } else {
                        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                        val signInWithCredential =
                            Firebase.auth.signInWithCredential(firebaseCredential)
                        val (successful, errorMessage) = handleInner(signInWithCredential)
                        if (successful) {
                            cloudDataSource.login()
                            LoginResult.Successful
                        } else {
                            LoginResult.Failed(errorMessage)
                        }
                    }
                } catch (e: Exception) {
                    return LoginResult.Failed(e.message ?: "something went wrong")
                }
            } else {
                return LoginResult.Failed("not successful to login")
            }
        }

        private suspend fun handleInner(task: Task<AuthResult>): Pair<Boolean, String> =
            suspendCoroutine { cont ->
                task.addOnSuccessListener {
                    cont.resume(Pair(true, ""))
                }.addOnFailureListener {
                    cont.resume(Pair(false, it.message ?: "error"))
                }
            }
    }
}