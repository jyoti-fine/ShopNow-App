package com.example.shopnow.Buyers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.shopnow.R
import com.example.shopnow.prevalent.Prevalent
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


class ConfirmActivity : AppCompatActivity() {

    private lateinit var nameEditText:EditText
    private lateinit var phoneEditText:EditText
    private lateinit var addressEditText:EditText
    private lateinit var cityEditText:EditText

    private lateinit var confirmOrderBtn: Button

    private var totalAmount=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)

        totalAmount= intent.getStringExtra("Total Price").toString()

        Toast.makeText(this,"Total Price = $ $totalAmount",Toast.LENGTH_SHORT).show()

        confirmOrderBtn=findViewById(R.id.confirm_final_order_btn)
        nameEditText=findViewById(R.id.shipment_name)
        phoneEditText=findViewById(R.id.shipment_phone_number)
        addressEditText=findViewById(R.id.shipment_address)
        cityEditText=findViewById(R.id.shipment_city)


        confirmOrderBtn.setOnClickListener {

            check()
        }

    }

    private fun check() {

        if (TextUtils.isEmpty(nameEditText.text.toString()))
        {
            Toast.makeText(this,"Please provide your full name.",Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(phoneEditText.text.toString()))
        {
            Toast.makeText(this,"Please provide your phone number.",Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(addressEditText.text.toString()))
        {
            Toast.makeText(this,"Please provide address",Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(cityEditText.text.toString()))
        {
            Toast.makeText(this,"Please provide your city name",Toast.LENGTH_SHORT).show()
        }
        else
        {
            confirmOrder()
        }

    }

    private fun confirmOrder()
    {
        val callForDate = Calendar.getInstance()

        val currentDate = SimpleDateFormat("MMM dd, yyyy")
        val saveCurrentDate = currentDate.format(callForDate.time)

        val currentTime = SimpleDateFormat("HH:mm:ss a")
        val saveCurrentTime = currentTime.format(callForDate.time)

        val orderRef: DatabaseReference= FirebaseDatabase.getInstance().getReference().child("Orders")
            .child(Prevalent.currentOnlineUser.phone!!)

        val orderMap: MutableMap<String, Any> = HashMap()

        orderMap["totalAmount"]=totalAmount
        orderMap["name"]=nameEditText.text.toString()
        orderMap["phone"]=phoneEditText.text.toString()
        orderMap["address"]=addressEditText.text.toString()
        orderMap["city"]=cityEditText.text.toString()
        orderMap["date"]=saveCurrentDate
        orderMap["time"]=saveCurrentTime
        orderMap["state"]="not shipped"

        orderRef.updateChildren(orderMap).addOnCompleteListener{

            if(it.isSuccessful)
            {
                FirebaseDatabase.getInstance().reference.child("Cart List").child("User View").child(
                    Prevalent.currentOnlineUser.phone!!
                ).removeValue()
                    .addOnCompleteListener{
                        if(it.isSuccessful)
                        {
                            Toast.makeText(this,"Your final order has been placed successfully",Toast.LENGTH_SHORT).show()

                            val intent= Intent(this, HomeActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            finish()
                        }
                    }
            }
        }

    }
}