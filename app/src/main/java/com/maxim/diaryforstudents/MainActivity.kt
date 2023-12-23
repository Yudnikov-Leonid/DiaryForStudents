package com.maxim.diaryforstudents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private val REQ_ONE_TAP = 2
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSingInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("575101660631-snd45ng04apnq52vv7r4t8hk3of8rd11.apps.googleusercontent.com").requestEmail().build()

        googleSingInClient = GoogleSignIn.getClient(this, options)
        auth = Firebase.auth

        findViewById<SignInButton>(R.id.singInButton).setOnClickListener {
            val intent = googleSingInClient.signInIntent
            startActivityForResult(intent, REQ_ONE_TAP)
        }

        if (auth.currentUser != null) {
            val intent = googleSingInClient.signInIntent
            startActivityForResult(intent, REQ_ONE_TAP)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_ONE_TAP) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                val idToken = account.idToken

                if (idToken != null) {
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    auth.signInWithCredential(firebaseCredential).addOnSuccessListener {
                        val user = auth.currentUser
                        updateUi(user)

                        val uId = user!!.uid
                        val email = user.email
                        val name = user.displayName
                        //todo send to firebase this info
                        Toast.makeText(this, "Login with email $email and name $name",
                            Toast.LENGTH_LONG).show()
                    }.addOnFailureListener {
                        updateUi(null)
                    }
                } else {
                    Toast.makeText(this, "Error",
                        Toast.LENGTH_LONG).show()
                }
            } catch (e: ApiException) {
                Toast.makeText(this, e.message,
                    Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }

    fun updateUi(user: Any?) {

    }
}