package com.example.newsappt2.presentation.scenes.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.newsappt2.R
import com.example.newsappt2.databinding.ActivityNewsListBinding
import com.example.newsappt2.databinding.ActivityRegisterBinding
import com.example.newsappt2.presentation.scenes.newslist.NewsListActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener{
            val loginIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
            loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(loginIntent)
            finish()
        }

        binding.registerButton.setOnClickListener{
            when{
                TextUtils.isEmpty(binding.registerEmail.text.toString().trim{it <= ' '}) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter your e-mail",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(binding.registerPassword.text.toString().trim{it <= ' '}) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter your password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {

                    val email: String = binding.registerEmail.text.toString().trim{it <= ' '}
                    val password: String = binding.registerPassword.text.toString().trim{it <= ' '}

                    Log.d("email", email.toString())
                    Log.d("password", password.toString())

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task->
                                if(task.isSuccessful){
                                    val firebaseUser: FirebaseUser = task.result!!.user!!
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Registered Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent = Intent(this@RegisterActivity, NewsListActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                else{
                                    Toast.makeText(
                                        this@RegisterActivity,
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