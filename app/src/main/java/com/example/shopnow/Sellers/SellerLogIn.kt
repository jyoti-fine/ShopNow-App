package com.example.shopnow.Sellers

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.shopnow.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.HashMap

class SellerLogIn : AppCompatActivity() {

    private lateinit var registerSellerBtn: Button
    private lateinit var emailInput : EditText
    private lateinit var passwordInput : EditText
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_log_in)

        progressDialog = ProgressDialog(this)

        emailInput=findViewById(R.id.seller_logIn_email)
        passwordInput=findViewById(R.id.seller_log_In_password)
        registerSellerBtn=findViewById(R.id.seller_login_Btn)

        mAuth= FirebaseAuth.getInstance()

        registerSellerBtn.setOnClickListener {
            loginSeller()
        }
    }

    private fun loginSeller() {
        val email=emailInput.text.toString()
        val password=passwordInput.text.toString()

        if(email!="" && password!="" )
        {
            progressDialog.setTitle("Login Seller Account")
            progressDialog.setMessage("Please wait, while we are checking the credentials.")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener{


                if(it.isSuccessful) {

                    progressDialog.dismiss()

                    val intent = Intent(this@SellerLogIn, SellerHomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()

                }
            }
        }
        else
        {
            Toast.makeText(this,"Please complete the registration form", Toast.LENGTH_LONG).show()
        }

    }
    
}