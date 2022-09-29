package com.example.register

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.register.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

//    viewbinding
    private lateinit var binding: ActivityLoginBinding

//    actionbar
    private lateinit var actionBar: ActionBar

//    progressdialog
    private lateinit var progressDialog: ProgressDialog

//    firebaseauth
    private lateinit var firebaseAuth: FirebaseAuth
    private  var email = ""
    private var password = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//    configure actionbar
    actionBar = supportActionBar!!
    actionBar.title = "Login"

//    configure progress dialog
    progressDialog = ProgressDialog(this)
    progressDialog.setTitle("Please wait")
    progressDialog.setMessage("Logging In....")
    progressDialog.setCanceledOnTouchOutside(false)

//        init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

//        handle click, open signupactivity
        binding.noAccountTv.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }

//        handle click, begin login
        binding.loginBtn.setOnClickListener {
            validateData()
        }


    }

    private fun validateData() {
//        get data
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

//        validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailEt.error = "Invalid email format"
        }
        else if (TextUtils.isEmpty(password)){
            binding.passwordEt.error = "Please enter password"
        }
        else{
//            data validated begin login
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
//        show progress
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
//                login successful
                progressDialog.dismiss()
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "LoggedIn as$email", Toast.LENGTH_SHORT).show()

//                open profile
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
            .addOnFailureListener { e->
//                login failed
                progressDialog.dismiss()
                Toast.makeText(this, "Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
//        if user is already logged in to profile activity
//        get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
//            user already logged in
            startActivity(Intent(this, ProfileActivity::class.java ))
            finish()
        }
    }
}