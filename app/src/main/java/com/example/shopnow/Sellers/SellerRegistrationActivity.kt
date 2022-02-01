package com.example.shopnow.Sellers

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.shopnow.Buyers.LoginActivity
import com.example.shopnow.Buyers.MainActivity
import com.example.shopnow.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider
import java.util.HashMap

class SellerRegistrationActivity : AppCompatActivity() {

    private lateinit var sellerLogInBegin: Button
    private lateinit var nameInput : EditText
    private lateinit var phoneInput : EditText
    private lateinit var emailInput : EditText
    private lateinit var passwordInput : EditText
    private lateinit var addressInput : EditText
    private lateinit var registerButton : Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_registration)

        progressDialog = ProgressDialog(this)

        mAuth= FirebaseAuth.getInstance()


        sellerLogInBegin = findViewById(R.id.seller_already_have_registered_Btn)
        nameInput=findViewById(R.id.seller_name)
        phoneInput=findViewById(R.id.seller_phone)
        emailInput=findViewById(R.id.seller_email)
        passwordInput=findViewById(R.id.seller_password)
        addressInput=findViewById(R.id.seller_address)
        registerButton=findViewById(R.id.seller_register_btn)

        registerButton.setOnClickListener {

            registerSeller()
        }

        sellerLogInBegin.setOnClickListener {
            val intent = Intent(this@SellerRegistrationActivity,SellerLogIn::class.java)
            startActivity(intent)
            finish()

        }
    }

    private fun registerSeller() {
        val name=nameInput.text.toString()
        val phone=phoneInput.text.toString()
        val email=emailInput.text.toString()
        val password=passwordInput.text.toString()
        val address=addressInput.text.toString()

        if(name!="" &&  phone!="" && email!="" && password!="" && address!="")
        {
            progressDialog.setTitle("Creating Seller Account")
            progressDialog.setMessage("Please wait, while we are checking the credentials.")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

           mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{


               if(it.isSuccessful) {
                   val rootRef = FirebaseDatabase.getInstance().reference

                   val sid = mAuth.currentUser!!.uid

                   val sellerMap: MutableMap<String, Any> = HashMap()

                   sellerMap["sid"] = sid
                   sellerMap["phone"] = phone
                   sellerMap["email"] = email
                   sellerMap["address"] = address
                   sellerMap["name"] = name


                   rootRef.child("Sellers").child(sid).updateChildren(sellerMap).addOnCompleteListener {

                       progressDialog.dismiss()
                       Toast.makeText(this@SellerRegistrationActivity, "You are registered successfully", Toast.LENGTH_LONG).show()

                       val intent = Intent(this@SellerRegistrationActivity, SellerHomeActivity::class.java)
                       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                       startActivity(intent)
                       finish()


                   }


               }
           }
        }
        else
        {
            Toast.makeText(this,"Please complete the registration form",Toast.LENGTH_LONG).show()
        }

    }
}