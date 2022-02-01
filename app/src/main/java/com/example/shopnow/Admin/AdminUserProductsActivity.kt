package com.example.shopnow.Admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopnow.R
import com.example.shopnow.model.Cart
import com.example.shopnow.viewHolder.CartViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdminUserProductsActivity : AppCompatActivity() {

    private lateinit var productsList:RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var cartListRef:DatabaseReference
    private var userID=""

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_user_products)

            userID= intent.getStringExtra("uid")!!
            productsList=findViewById(R.id.products_list)
            productsList.setHasFixedSize(true)
            layoutManager=LinearLayoutManager(this)
            productsList.layoutManager=layoutManager
            cartListRef = FirebaseDatabase.getInstance().reference.child("Cart List") .child("Admin View").child(userID).child("Products")
    }

    override fun onStart() {

        super.onStart()

        val options = FirebaseRecyclerOptions.Builder<Cart>()
            .setQuery(cartListRef, Cart::class.java)
            .build()

        val adapter: FirebaseRecyclerAdapter<Cart, CartViewHolder> = object :
            FirebaseRecyclerAdapter<Cart, CartViewHolder>(options)
        {
            override fun onBindViewHolder(cartViewHolder: CartViewHolder, p1: Int, cart: Cart)
            {

                cartViewHolder.textProductQuantity.text = getString(R.string.quantityAdapter, cart.quantity)
                cartViewHolder.textProductPrice.text = getString(R.string.priceAdapter, cart.price)
                cartViewHolder.textProductName.text = getString(R.string.nameAdapter, cart.pname)

            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {

                val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_items_layout, parent, false)
                return CartViewHolder(view)
            }

        }

        productsList.adapter=adapter
        adapter.startListening()
    }
}