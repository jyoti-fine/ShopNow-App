package com.example.shopnow.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopnow.R
import com.example.shopnow.`interface`.ItemClickListner


class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var txtProductName: TextView=itemView.findViewById(R.id.product_name)
    var txtProductDescription: TextView=itemView.findViewById(R.id.product_description)
    var txtProductPrice: TextView=itemView.findViewById(R.id.product_price)
    var imageView: ImageView=itemView.findViewById(R.id.product_image)


    var listner: ItemClickListner? = null



    override fun onClick(view: View?) {
        if (view != null) {
            listner?.onClick(view, adapterPosition, false)                     //onclick on post , move to details activity
        }
    }

    fun setItemClickListner(listner: ItemClickListner?) {
        this.listner = listner
    }


//        imageView = findViewById(R.id.product_image)
//        txtProductName = itemView.findViewById(R.id.product_name)
//        txtProductDescription = itemView.findViewById(R.id.product_description)
//        txtProductPrice = itemView.findViewById(R.id.product_price)

}