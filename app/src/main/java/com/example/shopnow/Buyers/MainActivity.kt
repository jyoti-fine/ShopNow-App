@file:Suppress("DEPRECATION")

package com.example.shopnow.Buyers

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.shopnow.R
import com.example.shopnow.Sellers.SellerHomeActivity
import com.example.shopnow.Sellers.SellerRegistrationActivity
import com.example.shopnow.model.User
import com.example.shopnow.prevalent.Prevalent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.paperdb.Paper

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var sellerBegin: TextView


    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val joinNowButton:Button=findViewById(R.id.main_join__btn)
        val loginButton:Button=findViewById(R.id.main_login__btn)
        sellerBegin=findViewById(R.id.seller_begin)

        sellerBegin.setOnClickListener{

            val intent = Intent(this@MainActivity,SellerRegistrationActivity::class.java)
            startActivity(intent)
            finish()

        }

        progressDialog = ProgressDialog(this)

        Paper.init(this)




        loginButton.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }

        joinNowButton.setOnClickListener {
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        try{
        val userPhoneKey:String=Paper.book().read(Prevalent.userPhoneKey)
        val userPasswordKey:String=Paper.book().read(Prevalent.userPasswordKey)

        if(userPhoneKey!="" && userPasswordKey!="")
        {
            if(!TextUtils.isEmpty(userPhoneKey) && !TextUtils.isEmpty(userPasswordKey))
            {
                allowAccess(userPhoneKey,userPasswordKey)
                progressDialog.setTitle("Already LoggedIn")
                progressDialog.setMessage("Please wait...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()
            }
        }}
        catch (e:NullPointerException)
        {

        }
    }

    override fun onStart() {
        super.onStart()

        val firebaseUser=FirebaseAuth.getInstance().currentUser

        if (firebaseUser!=null)
        {
            val intent = Intent(this@MainActivity, SellerHomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun allowAccess(number: String, password: String) {
        val rootRef = FirebaseDatabase.getInstance().reference

        rootRef.addValueEventListener(object : ValueEventListener {    //read and change the data
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child("User").child(number).exists()) {

                    val usersData = dataSnapshot.child("User").child(number).getValue(User::class.java)

                    if (usersData != null) {
                        if (usersData.password.equals(password)) {

//                            val t=Toast(this@MainActivity)
//                            t.setText("Please wait,You are Logged in .")
//                            t.duration=Toast.LENGTH_SHORT
//                            t.show()
                        //    Toast.makeText(this@MainActivity, "Please wait,You are Logged in .", Toast.LENGTH_SHORT).show()

                            progressDialog.dismiss()

                            val intent = Intent(this@MainActivity, HomeActivity::class.java)
                            Prevalent.currentOnlineUser=usersData
                            startActivity(intent)
                            finish()
                        }
                        else {                           //password incorrect
                            progressDialog.dismiss()
                            Toast.makeText(this@MainActivity, "Password is incorrect. ", Toast.LENGTH_LONG).show()
                        }
                    }

                } else {
                    Toast.makeText(this@MainActivity, "The account with this $number doesn't exist", Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()

                }


            }

            override fun onCancelled(error: DatabaseError) {

            }


        })

    }
}