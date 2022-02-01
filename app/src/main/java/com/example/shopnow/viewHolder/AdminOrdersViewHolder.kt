package com.example.shopnow.viewHolder

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopnow.R
import com.example.shopnow.`interface`.ItemClickListner

class AdminOrdersViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    var userName: TextView =itemView.findViewById(R.id.order_user_Name)
    var userPhoneNumber: TextView=itemView.findViewById(R.id.order_phone_Number)
    var userTotalPrice: TextView=itemView.findViewById(R.id.order_total_price)
    var userDateTime: TextView=itemView.findViewById(R.id.order_date_time)
    var userShippingAddress:TextView=itemView.findViewById(R.id.order_address_city)
    var showOrderBtn: Button=itemView.findViewById(R.id.show_all_products_btn)

    var listener: ItemClickListner? = null

    override fun onClick(view: View?) {
        if (view != null) {
            listener?.onClick(view, adapterPosition, false)                     //onclick on post , move to details activity
        }

    }
}