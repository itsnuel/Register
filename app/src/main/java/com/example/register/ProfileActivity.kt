package com.example.register

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.register.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    //    viewbinding
    private lateinit var binding: ActivityProfileBinding
    //    actionbar
    private lateinit var actionBar: androidx.appcompat.app.ActionBar

    //    firebaseauth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        configure actionbar
        actionBar = supportActionBar!!
        actionBar.title = "Profile"

//        init firebase
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

//        handle click, logout
        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

    }

    private fun checkUser() {
//        check user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
//            user not null, user is logged in
            val email = firebaseUser.email
//            st to text view
            binding.emailTv.text = email

        }
        else{
//            user is null, user is not logged in
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}