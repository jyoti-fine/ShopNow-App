package com.example.shopnow.Admin

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopnow.R
import com.example.shopnow.model.AdminOrders
import com.example.shopnow.viewHolder.AdminOrdersViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdminNewOrdersActivity : AppCompatActivity() {

    private lateinit var ordersList:RecyclerView
    private lateinit var ordersRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_new_orders)

        ordersRef=FirebaseDatabase.getInstance().reference.child("Orders")
        ordersList = findViewById(R.id.orders_list)
        ordersList.layoutManager=LinearLayoutManager(this)
    }


    override fun onStart() {
        super.onStart()

        val options = FirebaseRecyclerOptions.Builder<AdminOrders>()
            .setQuery(ordersRef, AdminOrders::class.java)
            .build()

        val adapter: FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> = object :FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options)
        {
            override fun onBindViewHolder(holder: AdminOrdersViewHolder, position: Int, model: AdminOrders) {
                holder.userName.text = getString(R.string.order_name, model.name)
                holder.userPhoneNumber.text = getString(R.string.order_phone_number, model.phone)
                holder.userTotalPrice.text = getString(R.string.order_totalPrice, model.totalAmount)
                holder.userDateTime.text = getString(R.string.order_orderAt, model.date, model.time)
                holder.userShippingAddress.text =getString(R.string.order_shipping, model.address, model.city)
                holder.showOrderBtn.setOnClickListener {

                    val uID = getRef(position).key

                    val intent =
                        Intent(this@AdminNewOrdersActivity, AdminUserProductsActivity::class.java)
                    intent.putExtra("uid", uID)
                    startActivity(intent)

                }

                holder.itemView.setOnClickListener {
                    var options = arrayOf("Yes", "No")


                    val builder = AlertDialog.Builder(this@AdminNewOrdersActivity)

                    builder.setTitle("Have you shipped this order products ?")
                    builder.setItems(options, DialogInterface.OnClickListener() {

                            dialog, which ->
                             if(which==0)
                             {
                                 val uID = getRef(position).key
                                 removeOrder(uID)
                             }
                             if(which==1)
                             {

                             }

                    })
                    builder.show()


                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminOrdersViewHolder {

             val view:View=LayoutInflater.from(parent.context).inflate(R.layout.orders_layout,parent,false)
                return AdminOrdersViewHolder(view)
            }


        }

        ordersList.adapter=adapter
        adapter.startListening()
    }

    private fun removeOrder(uID: String?) {

        if (uID != null) {
            ordersRef.child(uID).removeValue()
        }



    }
}