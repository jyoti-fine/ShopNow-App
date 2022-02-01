package com.example.shopnow.Sellers

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopnow.Buyers.MainActivity
import com.example.shopnow.R
import com.example.shopnow.model.Products
import com.example.shopnow.viewHolder.ItemViewHolder
import com.example.shopnow.viewHolder.ProductViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class SellerHomeActivity : AppCompatActivity()  {

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var unverifiedProductRef: DatabaseReference


    private lateinit var navController:NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_home)

        recyclerView=findViewById(R.id.seller_home_recyclerview)
        recyclerView.setHasFixedSize(true)
        layoutManager= LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager

        unverifiedProductRef= FirebaseDatabase.getInstance().reference.child("Products")


        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_add, R.id.navigation_logout
            )
        )
      //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration!!)
        // setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setOnItemSelectedListener {

            val id = it.itemId
            if (id==R.id.navigation_home){

                val intent = Intent(this@SellerHomeActivity, SellerHomeActivity::class.java)
                startActivity(intent)

            }
            if (id==R.id.navigation_add){

                val intent = Intent(this@SellerHomeActivity, SellerProductCategoryActivity::class.java)
                startActivity(intent)


            }
            else if  (id==R.id.navigation_logout){

                val mAuth = FirebaseAuth.getInstance()
                mAuth.signOut()


            val intent = Intent(this@SellerHomeActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()

            }


            true
        }


    }

    override fun onStart() {
        super.onStart()

        val options = FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(unverifiedProductRef.orderByChild("sid").equalTo(FirebaseAuth.getInstance().currentUser?.uid), Products::class.java)
                .build()

        val adapter: FirebaseRecyclerAdapter<Products, ItemViewHolder> = object :
                FirebaseRecyclerAdapter<Products, ItemViewHolder>(options)
        {
            override fun onBindViewHolder(holder: ItemViewHolder, p1: Int, model: Products) {

                holder.txtProductName.text = model.pname
                holder.txtProductDescription.text = model.description
                holder.txtProductPrice.text = getString(R.string.priceAdapter, model.price)
                holder.txtProductState.text = getString(R.string.check_status,model.productState)
                Picasso.get().load(model.image).into(holder.imageView)

                holder.imageView.setOnClickListener{

                    val productsID=model.pid

                    var options = arrayOf("Yes","No")

                    val builder = AlertDialog.Builder(this@SellerHomeActivity)
                    builder.setTitle("Do you want to Delete this product? Are you sure?")

                    builder.setItems(options,  DialogInterface.OnClickListener()
                    {
                        dialog, which ->
                        if(which==0)
                        {
                            deleteProduct(productsID)
                        }
                        if (which==1)
                        {



                        }

                    })
                    builder.show()

                }


            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.seller_item_view, parent, false)
                return ItemViewHolder(view)
            }


        }

        recyclerView.adapter=adapter
        adapter.startListening()
    }

    private fun deleteProduct(productsID: String) {

        unverifiedProductRef.child(productsID).removeValue().addOnCompleteListener{

            Toast.makeText(this,"That item has been deleted successfully", Toast.LENGTH_LONG).show()

        }


    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//
//        val id = item.itemId
//        // Toast.makeText(this,"Please complete the registration form", Toast.LENGTH_LONG).show()
//        if (id == R.id.navigation_home) {
//
//        } else if (id == R.id.navigation_add) {
//
//        } else if (id == R.id.navigation_logout) {
//
//            val mAuth = FirebaseAuth.getInstance()
//            mAuth.signOut()
//            Toast.makeText(this, "Please complete the registration form", Toast.LENGTH_LONG).show()
//
//            val intent = Intent(this@SellerHomeActivity, MainActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            startActivity(intent)
//            finish()
//
//        }
//
//        return NavigationUI.onNavDestinationSelected(item,navController)
//    }
}