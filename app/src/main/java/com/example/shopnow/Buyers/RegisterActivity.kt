package com.example.shopnow.Buyers

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.shopnow.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.HashMap

@Suppress("DEPRECATION")
class RegisterActivity : AppCompatActivity() {

    private lateinit var progressDialog:ProgressDialog
    lateinit var toast: Toast


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        progressDialog = ProgressDialog(this)
        toast= Toast.makeText(this@RegisterActivity, "NULL", Toast.LENGTH_LONG)

        val createAccountButton: Button =findViewById(R.id.register__btn)

        createAccountButton.setOnClickListener {
            createAccount()
        }

    }

    private fun createAccount() {
        val nameInput:EditText=findViewById(R.id.register_username_input)
        val name =nameInput.text.toString()
        val numberInput:EditText=findViewById(R.id.register_phone_number_input)
        val number =numberInput.text.toString()
        val passwordInput:EditText=findViewById(R.id.register_passward_input)
        val password=passwordInput.text.toString()

        if(name.isNullOrBlank())
        {
            Toast.makeText(this,"Please write your name...",Toast.LENGTH_LONG).show()
        }
        else if(number.isNullOrBlank())
        {
            Toast.makeText(this,"Please write your number...",Toast.LENGTH_LONG).show()
        }
        if(password.isNullOrBlank())
        {
            Toast.makeText(this,"Please write your password...",Toast.LENGTH_LONG).show()
        }
        else
        {

            progressDialog.setTitle("Create Account")
            progressDialog.setMessage("Please wait, while we are checking the credentials.")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            validatephone(name,number,password)


        }
    }

    private fun validatephone(name: String, number: String, password: String)
    {
       val rootRef = FirebaseDatabase.getInstance().reference

        rootRef.addValueEventListener(object : ValueEventListener {                 //read and change the data
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (!(dataSnapshot.child("User").child(number).exists())) {

                    val userDataMap: MutableMap<String, Any> = HashMap()
                    userDataMap["phone"] = number
                    userDataMap["password"] = password
                    userDataMap["name"] = name

                    rootRef.child("User").child(number).updateChildren(userDataMap)        //in User , all details will be stored in number as reference of number
                            .addOnSuccessListener {


                                if(toast!=null){
                                    toast.cancel()
                                }

                                toast=Toast.makeText(this@RegisterActivity, "Congratulations, your account is created successfully", Toast.LENGTH_LONG)
                                toast.show()
                                progressDialog.dismiss()

                                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()


                            }.addOnFailureListener {
                                progressDialog.dismiss()
                                Toast.makeText(this@RegisterActivity, "Something went gone. Please try again", Toast.LENGTH_LONG).show()      //12.29


                            }
                }
                else {

                    progressDialog.dismiss()
                    if(toast!=null){
                        toast.cancel()
                    }

                    toast=Toast.makeText(this@RegisterActivity, "This number $number already exist", Toast.LENGTH_LONG)
                    toast.show()

                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }


        })


    }

}




