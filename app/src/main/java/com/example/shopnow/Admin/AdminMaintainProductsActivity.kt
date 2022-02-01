package com.example.shopnow.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.shopnow.R
import com.example.shopnow.Sellers.SellerProductCategoryActivity
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.HashMap

class AdminMaintainProductsActivity : AppCompatActivity() {

    private lateinit var applyChangesBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var name: EditText
    private lateinit var price: EditText
    private lateinit var description: EditText
    private lateinit var imageView: ImageView

    private lateinit var productRef:DatabaseReference

    private lateinit var productID:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_maintain_products)


        productID = intent.getStringExtra("pid")!!
        productRef = FirebaseDatabase.getInstance().reference.child("Products").child(productID)

        applyChangesBtn=findViewById(R.id.apply_changes_Btn)
        name=findViewById(R.id.product_name_maintain)
        price=findViewById(R.id.product_price_maintain)
        description=findViewById(R.id.product_description_maintain)
        imageView=findViewById(R.id.product_image_maintain)
        deleteBtn=findViewById(R.id.delete_products_Btn)

        displaySpecificProductInfo()

        applyChangesBtn.setOnClickListener {
            applyChanges()
        }

        deleteBtn.setOnClickListener {
            deleteThisProduct()
        }



    }

    private fun deleteThisProduct() {
        productRef.removeValue().addOnCompleteListener{

            val intent = Intent(this@AdminMaintainProductsActivity, SellerProductCategoryActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@AdminMaintainProductsActivity,"This Product is deleted successfully",Toast.LENGTH_LONG).show()
        }
    }

    private fun applyChanges() {
        val pName=name.text.toString()
        val pPrice=price.text.toString()
        val pDescription=description.text.toString()

        if(pName == "")
        {
            Toast.makeText(this,"Write down Product Name.",Toast.LENGTH_LONG).show()
        }
        else if(pPrice == "")
        {
            Toast.makeText(this,"Write down Product Price.",Toast.LENGTH_LONG).show()
        }
        else if(pDescription == "")
        {
            Toast.makeText(this,"Write down Product Description.",Toast.LENGTH_LONG).show()
        }
        else
        {
            val productMap: MutableMap<String, Any> = HashMap()
            productMap["pid"] = productID
            productMap["description"] = pDescription
            productMap["price"] = pPrice
            productMap["pname"] = pName

            productRef.updateChildren(productMap).addOnCompleteListener{
                if(it.isSuccessful)
                {
                    Toast.makeText(this@AdminMaintainProductsActivity,"Changes Applied",Toast.LENGTH_LONG).show()

                    val intent = Intent(this@AdminMaintainProductsActivity, SellerProductCategoryActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

    }

    private fun displaySpecificProductInfo() {
        productRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists())
                {
                    var pName=snapshot.child("pname").value.toString()
                    var pPrice=snapshot.child("price").value.toString()
                    var pDescription=snapshot.child("description").value.toString()
                    var pImage=snapshot.child("image").value.toString()

                    name.setText(pName)
                    price.setText(pPrice)
                    description.setText(pDescription)
                    Picasso.get().load(pImage).into(imageView)

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}