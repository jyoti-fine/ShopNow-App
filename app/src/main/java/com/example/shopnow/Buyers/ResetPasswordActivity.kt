package com.example.shopnow.Buyers

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.shopnow.R
import com.example.shopnow.prevalent.Prevalent
import com.google.firebase.database.*

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var check:String
    private lateinit var pageTitle:TextView
    private lateinit var titleQuestions:TextView
    private lateinit var phoneNumber:EditText
    private lateinit var question1:EditText
    private lateinit var question2:EditText
    private lateinit var verifyBtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        check = intent.getStringExtra("check")!!

        pageTitle=findViewById(R.id.page_Title)
        titleQuestions=findViewById(R.id.title_questions)
        phoneNumber=findViewById(R.id.find_phone_number)
        question1=findViewById(R.id.question_1)
        question2=findViewById(R.id.question_2)
        verifyBtn=findViewById(R.id.verify_btn)



    }

    override fun onStart() {
        super.onStart()

        phoneNumber.visibility=View.GONE

        if(check=="settings")
        {
            pageTitle.text="Set Questions"
            titleQuestions.text="Please set Answers for the following Security Questions."
            verifyBtn.text = "Set"

            displayPreviousAnswer()
            verifyBtn.setOnClickListener {
                setAnswer()

            }


        }
        else if (check=="login")
        {
            phoneNumber.visibility=View.VISIBLE

            verifyBtn.setOnClickListener{
                verifyUser()
            }

        }
    }

    private fun verifyUser()
    {
        val phone=phoneNumber.text.toString()
        val answer1=question1.text.toString().toLowerCase()
        val answer2=question2.text.toString().toLowerCase()


        if(phone!="" && answer1!="" && answer2!="") {
            val ref = FirebaseDatabase.getInstance().reference.child("User").child(phone!!)

            ref.child("Security Questions").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        val mPhone = snapshot.child("phone").value.toString()
                        if (snapshot.exists())
                        {
                            val ans1 = snapshot.child("answer1").value.toString()
                            val ans2 = snapshot.child("answer2").value.toString()

                            if (ans1 != answer1) {
                                Toast.makeText(this@ResetPasswordActivity, "Your First Answer is wrong.", Toast.LENGTH_LONG).show()
                            } else if (ans2 != answer2) {
                                Toast.makeText(this@ResetPasswordActivity, "Your Second Answer is wrong.", Toast.LENGTH_LONG).show()
                            } else {

                                val builder = AlertDialog.Builder(this@ResetPasswordActivity)
                                builder.setTitle("New Password")

                                val newPassword: EditText = EditText(this@ResetPasswordActivity)
                                newPassword.hint = "Write Password here..."
                                builder.setView(newPassword)
                                builder.setPositiveButton("Change", DialogInterface.OnClickListener() {

                                    dialogInterface: DialogInterface, i: Int ->

                                    if (newPassword.text.toString() != null) {
                                        ref.child("password").setValue(newPassword.text.toString())
                                                .addOnCompleteListener {
                                                    if (it.isSuccessful) {
                                                        Toast.makeText(this@ResetPasswordActivity, "Password Changed Successfully", Toast.LENGTH_LONG).show()
                                                        val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                                                        startActivity(intent)
                                                        finish()
                                                    }
                                                }
                                    }


                                })

                                builder.setNegativeButton("Cancel", DialogInterface.OnClickListener() {

                                    dialogInterface: DialogInterface, i: Int ->

                                    dialogInterface.cancel()

                                })

                                builder.show()


                            }
                        }
                        else {
                            Toast.makeText(this@ResetPasswordActivity, "You have not set the security questions", Toast.LENGTH_LONG).show()
                        }

                    }
                    else
                    {
                        Toast.makeText(this@ResetPasswordActivity, "This phone number not exist", Toast.LENGTH_LONG).show()
                    }

                }


                override fun onCancelled(error: DatabaseError) {

                }


            })
        }
        else
        {
            Toast.makeText(this@ResetPasswordActivity, "Please complete the form", Toast.LENGTH_LONG).show()
        }

    }

    private fun setAnswer()
    {
        val answer1=question1.text.toString().toLowerCase()
        val answer2=question2.text.toString().toLowerCase()

        if(answer1=="" && answer2=="")
        {
            Toast.makeText(this@ResetPasswordActivity,"Please Answer Both the questions...",Toast.LENGTH_LONG).show()
        }
        else
        {
            val ref=FirebaseDatabase.getInstance().reference.child("User").child(Prevalent.currentOnlineUser.phone!!)

            val userDataMap: MutableMap<String, Any> = HashMap()
            userDataMap["answer1"] = answer1
            userDataMap["answer2"] = answer2

            ref.child("Security Questions").updateChildren(userDataMap).addOnCompleteListener{
                if(it.isSuccessful)
                {
                    Toast.makeText(this@ResetPasswordActivity,"You have answered the security Questions Successfully.",Toast.LENGTH_LONG).show()
                    val intent = Intent(this@ResetPasswordActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }


        }
    }

    private fun displayPreviousAnswer(){
        val ref=FirebaseDatabase.getInstance().reference.child("User").child(Prevalent.currentOnlineUser.phone!!)

        ref.child("Security Questions").addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val ans1= snapshot.child("answer1").value.toString()
                    val ans2=snapshot.child("answer2").value.toString()

                    question1.setText(ans1)
                    question2.setText(ans2)

                }

            }


            override fun onCancelled(error: DatabaseError)
            {

            }


        })
    }



}