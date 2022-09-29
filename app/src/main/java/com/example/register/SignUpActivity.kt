package com.example.register

import android.app.ActionBar
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.register.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    //    viewbinding
    private lateinit var binding: ActivitySignUpBinding
    //    actionbar
    private lateinit var actionBar: androidx.appcompat.app.ActionBar
    //    progressdialog
    private lateinit var progressDialog: ProgressDialog

    //    firebaseauth
    private lateinit var firebaseAuth: FirebaseAuth
    private  var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

//    configure action bar, enable back button
        actionBar = supportActionBar!!
        actionBar.title = "Sign Up"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        //    configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Creating account In....")
        progressDialog.setCanceledOnTouchOutside(false)

//        init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()


        binding.haveAccount.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

//        handler click,begin signup
        binding.signUpBtn.setOnClickListener {
//            validate data
            validateData()
        }


    }

    private fun validateData() {
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
            if (password.length <6){
                binding.passwordEt.error = "Password must atleast 6 characters long"
            }
            else{
//                data is valid, continue signup
                firebaseSignUp()
            }
        }
    }

    private fun firebaseSignUp() {
//        show progress
        progressDialog.show()

//        create account
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "Account created with email $email",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{ e->
                progressDialog.dismiss()
                Toast.makeText(this, "Sign Up Failed due to ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}