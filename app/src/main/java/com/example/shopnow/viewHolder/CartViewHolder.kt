package com.example.shopnow.viewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopnow.R
import com.example.shopnow.`interface`.ItemClickListner

class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener
{
    var textProductName: TextView=itemView.findViewById(R.id.cart_product_name)
    var textProductPrice: TextView=itemView.findViewById(R.id.cart_product_price)
    var textProductQuantity: TextView=itemView.findViewById(R.id.cart_product_quantity)

    var itemClickListener: ItemClickListner? = null

    override fun onClick(view: View?) {
        if (view != null) {
            itemClickListener?.onClick(view, adapterPosition, false)                     //onclick on post , move to details activity
        }

    }

    fun setItemClickListner(listner: ItemClickListner?) {
        this.itemClickListener = listner
    }

}