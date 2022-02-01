package com.example.shopnow.Buyers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.example.shopnow.R
import com.example.shopnow.model.Products
import com.example.shopnow.prevalent.Prevalent
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var addToCartButton:Button
    private lateinit var productImage:ImageView
    private lateinit var numberButton:ElegantNumberButton
    private lateinit var productPrice:TextView
    private lateinit var productDescription:TextView
    private lateinit var productName:TextView

    private lateinit var productID:String
    private var state="Normal"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        addToCartButton=findViewById(R.id.pd_add_product_to_cart_button)
        numberButton=findViewById(R.id.number_btn)
        productImage=findViewById(R.id.product_image_details)
        productName=findViewById(R.id.product_name_details)
        productDescription=findViewById(R.id.product_description_details)
        productPrice=findViewById(R.id.product_price_details)

        productID= intent.getStringExtra("pid").toString()


        getProductDetails(productID)

        addToCartButton.setOnClickListener{

            if(state=="Order Placed" || state == "Order Shipped")
            {
                Toast.makeText(this@ProductDetailActivity,"You can add or purchase, once your order is shipped or confirmed.",Toast.LENGTH_LONG).show()
            }
            else
            {
                addingCartList()
            }

        }

    }

    override fun onStart() {
        super.onStart()
        checkOrderState()
    }

    private fun addingCartList() {

        val callForDate = Calendar.getInstance()

        val currentDate = SimpleDateFormat("MMM dd, yyyy")
        val saveCurrentDate = currentDate.format(callForDate.time)

        val currentTime = SimpleDateFormat("HH:mm:ss a")
        val saveCurrentTime = currentTime.format(callForDate.time)

        val cartListRef:DatabaseReference=FirebaseDatabase.getInstance().reference.child("Cart List")

        val cartMap: MutableMap<String, Any> = HashMap()

        cartMap["pid"]=productID
        cartMap["pname"]=productName.text.toString()
        cartMap["price"]=productPrice.text.toString()
        cartMap["date"]=saveCurrentDate
        cartMap["time"]=saveCurrentTime
        cartMap["quantity"]=numberButton.number
        cartMap["discount"]=""

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.phone!!).child("Products").child(productID)
            .updateChildren(cartMap).addOnCompleteListener{ task ->
                if(task.isSuccessful)
                {
                    cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.phone!!).child("Products").child(productID)
                        .updateChildren(cartMap)
                        .addOnCompleteListener{
                            if(it.isSuccessful)
                            {

                                Toast.makeText(this@ProductDetailActivity,"Added to Cart",Toast.LENGTH_LONG).show()
                                val intent= Intent(this, HomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                }
            }




    }

    private fun getProductDetails(productID: String) {

        val productsRef:DatabaseReference=FirebaseDatabase.getInstance().reference.child("Products")

        productsRef.child(productID).addValueEventListener(object :ValueEventListener
        {

            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                if(dataSnapshot.exists()){
                    val products: Products? =dataSnapshot.getValue(Products::class.java)

                    productName.text=products!!.pname
                    productPrice.text=products!!.price
                    productDescription.text=products!!.description
                    Picasso.get().load(products.image).into(productImage)


                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }





        )


    }

    private fun checkOrderState()
    {
        val ordersRef:DatabaseReference=FirebaseDatabase.getInstance().reference.child("Orders").child(Prevalent.currentOnlineUser.phone!!)
        ordersRef.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists())
                {
                    val shippingState = snapshot.child("state").value.toString()


                    if(shippingState == "not shipped")
                    {
                       state = "Order Placed"

                    }
                    else if(shippingState == "shipped")
                    {
                        state = "Order Shipped"
                    }
                }


            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

}