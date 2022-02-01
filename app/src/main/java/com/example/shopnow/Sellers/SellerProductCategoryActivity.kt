package com.example.shopnow.Sellers

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.shopnow.R

class SellerProductCategoryActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_add_category)



        val tShirts: ImageView=findViewById(R.id.t_shirts)
        tShirts.setOnClickListener {
            val intent = Intent(this@SellerProductCategoryActivity, SellerAddNewProductActivity::class.java)
            intent.putExtra("category", "tShirts")
            startActivity(intent)
        }

        val sportsTShirts: ImageView=findViewById(R.id.sports_t_shirts)
        sportsTShirts.setOnClickListener {
            val intent = Intent(this@SellerProductCategoryActivity, SellerAddNewProductActivity::class.java)
            intent.putExtra("category", "Sports tShirts")
            startActivity(intent)
        }

        val femaleDresses: ImageView=findViewById(R.id.female_dresses)
        femaleDresses.setOnClickListener {
            val intent = Intent(this@SellerProductCategoryActivity, SellerAddNewProductActivity::class.java)
            intent.putExtra("category", "Female Dresses")
            startActivity(intent)
        }

        val sweathers: ImageView=findViewById(R.id.sweathers)
        sweathers.setOnClickListener {
            val intent = Intent(this@SellerProductCategoryActivity, SellerAddNewProductActivity::class.java)
            intent.putExtra("category", "Sweathers")
            startActivity(intent)
        }

        val glasses: ImageView=findViewById(R.id.glasses)
        glasses.setOnClickListener {
            val intent = Intent(this@SellerProductCategoryActivity, SellerAddNewProductActivity::class.java)
            intent.putExtra("category", "Glasses")
            startActivity(intent)
        }

        val hatsCaps: ImageView=findViewById(R.id.hats_caps)
        hatsCaps.setOnClickListener {
            val intent = Intent(this@SellerProductCategoryActivity, SellerAddNewProductActivity::class.java)
            intent.putExtra("category", "Hats Caps")
            startActivity(intent)
        }

        val walletsBagsPurses: ImageView=findViewById(R.id.purses_bags_wallets)
        walletsBagsPurses.setOnClickListener {
            val intent = Intent(this@SellerProductCategoryActivity, SellerAddNewProductActivity::class.java)
            intent.putExtra("category", "Wallets Bags Purses")
            startActivity(intent)
        }

        val shoes: ImageView=findViewById(R.id.shoes)
        shoes.setOnClickListener {
            val intent = Intent(this@SellerProductCategoryActivity, SellerAddNewProductActivity::class.java)
            intent.putExtra("category", "Shoes")
            startActivity(intent)
        }

        val headPhonesHandFree: ImageView=findViewById(R.id.headphones_handfree)
        headPhonesHandFree.setOnClickListener {
            val intent = Intent(this@SellerProductCategoryActivity, SellerAddNewProductActivity::class.java)
            intent.putExtra("category", "HeadPhones HandFree")
            startActivity(intent)
        }

        val laptops :ImageView=findViewById(R.id.laptop_pc)
        laptops.setOnClickListener {
            val intent = Intent(this@SellerProductCategoryActivity, SellerAddNewProductActivity::class.java)
            intent.putExtra("category", "Laptops")
            startActivity(intent)
        }

        val watches :ImageView=findViewById(R.id.watches)
        watches.setOnClickListener {
            val intent = Intent(this@SellerProductCategoryActivity, SellerAddNewProductActivity::class.java)
            intent.putExtra("category", "Watches")
            startActivity(intent)
        }



        val mobilePhones :ImageView=findViewById(R.id.mobilephones)
        mobilePhones.setOnClickListener {
            val intent = Intent(this@SellerProductCategoryActivity, SellerAddNewProductActivity::class.java)
            intent.putExtra("category", "Mobile Phones")
            startActivity(intent)
        }


    }
}