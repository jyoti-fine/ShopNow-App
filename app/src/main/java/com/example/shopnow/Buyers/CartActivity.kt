package com.example.shopnow.Buyers


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopnow.R
import com.example.shopnow.model.Cart
import com.example.shopnow.prevalent.Prevalent
import com.example.shopnow.viewHolder.CartViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var textMag1:TextView
    private var overTotalPrice=0
    private lateinit var textTotalAmount :TextView
    private lateinit var nextProgressBtn :Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)


        recyclerView = findViewById(R.id.cart_list)
        textMag1 = findViewById(R.id.msg1)



        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager
        nextProgressBtn = findViewById(R.id.nextProgressBtn)
        textTotalAmount = findViewById(R.id.totalPrice)
        textTotalAmount.text = getString(R.string.overall_Total_price, overTotalPrice.toString())

        nextProgressBtn.setOnClickListener {



            val intent=Intent(this@CartActivity, ConfirmActivity::class.java)
            intent.putExtra("Total Price",overTotalPrice.toString())
            startActivity(intent)
            finish()
        }

    }

    override fun onStart() {
        super.onStart()

        checkOrderState()
        val cartListRef = FirebaseDatabase.getInstance().reference.child("Cart List")
        val options = FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View").child(Prevalent.currentOnlineUser.phone!!).child("Products"), Cart::class.java).build()
        val adapter: FirebaseRecyclerAdapter<Cart, CartViewHolder> = object : FirebaseRecyclerAdapter<Cart, CartViewHolder>(options)
        {
            override fun onBindViewHolder(cartViewHolder: CartViewHolder, i: Int, cart: Cart) {


                cartViewHolder.textProductQuantity.text = getString(R.string.quantityAdapter, cart.quantity)
                cartViewHolder.textProductPrice.text = getString(R.string.priceAdapter, cart.price)
                cartViewHolder.textProductName.text = getString(R.string.nameAdapter, cart.pname)

                val oneTypeProductPrice =cart.price.toInt() * cart.quantity.toInt()

                overTotalPrice += oneTypeProductPrice




                cartViewHolder.itemView.setOnClickListener {

                    var options = arrayOf("Edit","Remove")

                    val builder = AlertDialog.Builder(this@CartActivity)
                    builder.setTitle("Cart Options:")

                    builder.setItems(options,  DialogInterface.OnClickListener()
                    {
                        dialog, which ->
                         if(which==0)
                         {
                             val intent = Intent(this@CartActivity, ProductDetailActivity::class.java)
                             intent.putExtra("pid",cart.pid)
                             startActivity(intent)
                             finish()
                         }
                        if (which==1)
                        {
                            cartListRef.child("User View").child(Prevalent.currentOnlineUser.phone!!).child("Products").child(cart.pid!!)
                                    .removeValue().addOnCompleteListener{

                                        if(it.isSuccessful)
                                        {
                                            Toast.makeText(this@CartActivity, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(this@CartActivity, HomeActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                    }


                        }

                    })
                    builder.show()
                }




            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_items_layout, parent, false)
                return CartViewHolder(view)
            }
        }
        recyclerView!!.adapter = adapter
        adapter.startListening()

    }

    private fun checkOrderState()
    {
        val ordersRef:DatabaseReference=FirebaseDatabase.getInstance().reference.child("Orders").child(Prevalent.currentOnlineUser.phone!!)
        ordersRef.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists())
                {
                    val shippingState = snapshot.child("state").value.toString()
                    val userName = snapshot.child("name").value.toString()

                    if(shippingState == "not shipped")
                    {
                        textTotalAmount.text = getString(R.string.shippedMsg)
                        recyclerView.visibility=View.GONE
                        textMag1.visibility=View.VISIBLE

                        nextProgressBtn.visibility=View.GONE

                        Toast.makeText(this@CartActivity,"you can purchase, once you received your first final order.",Toast.LENGTH_SHORT).show()

                    }
                    else if(shippingState == "shipped")
                    {
                        textTotalAmount.text = getString(R.string.shipped,userName)
                        recyclerView.visibility=View.GONE
                        textMag1.visibility=View.VISIBLE
                        textMag1.text=(getString(R.string.congratulations_your_final_order_has_been_shipped_successfully_soon_you_will_receive_your_order))
                        nextProgressBtn.visibility=View.GONE


                        Toast.makeText(this@CartActivity,"you can purchase, once you received your first final order.",Toast.LENGTH_SHORT).show()
                    }
                }


            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

}