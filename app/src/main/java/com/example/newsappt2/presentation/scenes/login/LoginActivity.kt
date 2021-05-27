package com.example.newsappt2.presentation.scenes.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.newsappt2.R
import com.example.newsappt2.databinding.ActivityLoginBinding
import com.example.newsappt2.presentation.scenes.newslist.NewsListActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener{
            val registerIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
            registerIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(registerIntent)
            finish()
        }

        binding.loginButton.setOnClickListener{
            when{
                TextUtils.isEmpty(binding.email.text.toString().trim{it <= ' '}) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter your e-mail",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(binding.password.text.toString().trim{it <= ' '}) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter your password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {

                    val email: String = binding.email.text.toString().trim{it <= ' '}
                    val password: String = binding.password.text.toString().trim{it <= ' '}

                    Log.d("email", email.toString())
                    Log.d("password", password.toString())

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task->
                                if(task.isSuccessful){
                                    val firebaseUser: FirebaseUser = task.result!!.user!!
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Logged Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent = Intent(this@LoginActivity, NewsListActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                else{
                                    Toast.makeText(
                                        this@LoginActivity,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
                }
            }
        }
    }
}