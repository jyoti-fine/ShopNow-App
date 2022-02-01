package com.example.shopnow.Buyers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopnow.R
import com.example.shopnow.model.Products
import com.example.shopnow.viewHolder.ProductViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class SearchProductsActivity : AppCompatActivity() {

    private lateinit var searchBtn:Button
    private lateinit var inputText:EditText
    private lateinit var searchList:RecyclerView
    private var searchInput=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_products)

        inputText=findViewById(R.id.search_product_name)
        searchBtn=findViewById(R.id.search_btn)
        searchList=findViewById(R.id.search_list)
        searchList.layoutManager=LinearLayoutManager(this)

        searchBtn.setOnClickListener {
            searchInput=inputText.text.toString()
            onStart()


        }


    }

    override fun onStart() {
        super.onStart()

        val reference=FirebaseDatabase.getInstance().reference.child("Products")
        val options = FirebaseRecyclerOptions.Builder<Products>()
            .setQuery(reference.orderByChild("pname").startAt(searchInput), Products::class.java)
            .build()

        val adapter: FirebaseRecyclerAdapter<Products, ProductViewHolder> = object :
            FirebaseRecyclerAdapter<Products, ProductViewHolder>(options)
        {
            override fun onBindViewHolder(holder: ProductViewHolder, p1: Int, model: Products) {

                holder.txtProductName.text = model.pname
                holder.txtProductDescription.text = model.description
                holder.txtProductPrice.text = getString(R.string.priceAdapter, model.price)
                Picasso.get().load(model.image).into(holder.imageView)

                holder.itemView.setOnClickListener {
                    val intent = Intent(this@SearchProductsActivity, ProductDetailActivity::class.java)
                    intent.putExtra("pid",model.pid)
                    startActivity(intent)


                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.products_items_layout, parent, false)
                return ProductViewHolder(view)
            }

        }
        searchList.adapter=adapter
        adapter.startListening()
    }
}