package com.example.shopnow.Admin

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopnow.Buyers.HomeActivity
import com.example.shopnow.Buyers.ProductDetailActivity
import com.example.shopnow.R
import com.example.shopnow.model.Products
import com.example.shopnow.prevalent.Prevalent
import com.example.shopnow.viewHolder.ProductViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class CheckNewProductsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var unverifiedProductRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_new_products)

        recyclerView=findViewById(R.id.admin_products_checklist)
        recyclerView.setHasFixedSize(true)
        layoutManager= LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager

        unverifiedProductRef=FirebaseDatabase.getInstance().reference.child("Products")

    }

    override fun onStart() {
        super.onStart()

        val options = FirebaseRecyclerOptions.Builder<Products>()
            .setQuery(unverifiedProductRef.orderByChild("productState").equalTo("Not Approved"), Products::class.java)
            .build()

        val adapter: FirebaseRecyclerAdapter<Products, ProductViewHolder> = object :
            FirebaseRecyclerAdapter<Products, ProductViewHolder>(options)
        {
            override fun onBindViewHolder(holder: ProductViewHolder, p1: Int, model: Products) {

                holder.txtProductName.text = model.pname
                holder.txtProductDescription.text = model.description
                holder.txtProductPrice.text = getString(R.string.priceAdapter, model.price)
                Picasso.get().load(model.image).into(holder.imageView)

                holder.imageView.setOnClickListener{

                    val productsID=model.pid

                    var options = arrayOf("Yes","No")

                    val builder = AlertDialog.Builder(this@CheckNewProductsActivity)
                    builder.setTitle("Do you want to Approve this product? Are you sure?")

                    builder.setItems(options,  DialogInterface.OnClickListener()
                    {
                            dialog, which ->
                        if(which==0)
                        {
                            changeProductState(productsID)
                        }
                        if (which==1)
                        {



                        }

                    })
                    builder.show()

                }


            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

                val view:View =LayoutInflater.from(parent.context).inflate(R.layout.products_items_layout, parent, false)
                return ProductViewHolder(view)
            }


        }

        recyclerView.adapter=adapter
        adapter.startListening()

    }

    private fun changeProductState(productsID: String) {

        unverifiedProductRef.child(productsID).child("productState").setValue("Approved").addOnCompleteListener{

            Toast.makeText(this,"That item has been approved, and it is now available for sale from the seller.",Toast.LENGTH_LONG).show()

        }


    }
}