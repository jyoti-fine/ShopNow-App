@file:Suppress("DEPRECATION")

package com.example.shopnow.Buyers

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.shopnow.Admin.AdminHomeActivity
import com.example.shopnow.Sellers.SellerProductCategoryActivity
import com.example.shopnow.R
import com.example.shopnow.model.User
import com.example.shopnow.prevalent.Prevalent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.paperdb.Paper


@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity()
{

    private lateinit var progressDialog: ProgressDialog
    lateinit var parentDbName:String
    lateinit var checkBox:com.rey.material.widget.CheckBox
    lateinit var forgetPassword:TextView
    private var check=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

       if(intent.getStringArrayExtra("check") != null)
              check= intent.getStringExtra("check")!!

        progressDialog = ProgressDialog(this)
        parentDbName="User"

        checkBox =findViewById(R.id.remember_me_chkb)
        Paper.init(this);                 //user stay logged after closing the app

        val loginButton: Button =findViewById(R.id.login__btn)

        loginButton.setOnClickListener {
            loginUser()
        }

        forgetPassword=findViewById(R.id.forget_password_link)
        forgetPassword.setOnClickListener{

            val intent = Intent(this@LoginActivity, ResetPasswordActivity::class.java)
            intent.putExtra("check","login")
            startActivity(intent)
            finish()

        }


        val adminLink:TextView=findViewById(R.id.admin_panel_link)
        val notAdminLink:TextView=findViewById(R.id.not_admin_panel_link)

        adminLink.setOnClickListener{
            loginButton.text="Login Admin"
            adminLink.visibility=View.INVISIBLE
            notAdminLink.visibility=View.VISIBLE
            parentDbName="Admin"
        }
        notAdminLink.setOnClickListener{
            loginButton.text="Login"
            adminLink.visibility=View.VISIBLE
            notAdminLink.visibility=View.INVISIBLE
            parentDbName="User"
        }
    }

    override fun onStart() {
        super.onStart()

        if(check=="settings")
        {

        }
        else if(check=="login")
        {

        }
    }

    private fun loginUser()
    {
        val numberInput: EditText =findViewById(R.id.login_phone_number_input)
        val passwordInput:EditText=findViewById(R.id.login_passward_input)
        val number =numberInput.text.toString()
        val password=passwordInput.text.toString()

        if(TextUtils.isEmpty(number))
        {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_LONG)
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_LONG)
        }
        else
        {
            progressDialog.setTitle("Login Account")
            progressDialog.setMessage("Please wait, while we are checking the credentials.")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            allowAccessToAccount(number, password)
        }
    }

    private fun allowAccessToAccount(number: String, password: String)
    {

        if(checkBox.isChecked)
        {

            Paper.book().write(Prevalent.userPhoneKey,number)
            Paper.book().write(Prevalent.userPasswordKey,password)
        }

        val rootRef = FirebaseDatabase.getInstance().reference

        rootRef.addValueEventListener(object : ValueEventListener
        {    //read and change the data
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                if (dataSnapshot.child(parentDbName).child(number).exists())
                {

                    val usersData = dataSnapshot.child(parentDbName).child(number).getValue(User::class.java)

                    if (usersData != null) {
                        if (usersData.password.equals(password))
                        {

                            if (parentDbName == "Admin")
                            {
                                Toast.makeText(this@LoginActivity, "Welcome Admin,You are logged in Successfully...", Toast.LENGTH_LONG).show()
                                progressDialog.dismiss()
                                val intent = Intent(this@LoginActivity, AdminHomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            }

                            else if (parentDbName == "User")
                            {
                                Toast.makeText(this@LoginActivity, "Logged in Successfully...", Toast.LENGTH_LONG).show()
                                progressDialog.dismiss()

                                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                Prevalent.currentOnlineUser=usersData
                                startActivity(intent)
                                finish()
                            }


                           }
                             else
                           {                           //password incorrect
                                progressDialog.dismiss()
                                Toast.makeText(this@LoginActivity, "Password is incorrect. ", Toast.LENGTH_LONG).show()
                           }
                    }

                }
                else
                {
                    Toast.makeText(this@LoginActivity, "The account with this $number doesn't exist", Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()

                }


            }

            override fun onCancelled(error: DatabaseError)
            {

            }


        })
    }


}
