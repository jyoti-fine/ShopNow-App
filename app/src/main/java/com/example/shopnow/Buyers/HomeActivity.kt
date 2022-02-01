package com.example.shopnow.Buyers

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopnow.*
import com.example.shopnow.Admin.AdminMaintainProductsActivity
import com.example.shopnow.model.Products
import com.example.shopnow.prevalent.Prevalent
import com.example.shopnow.viewHolder.ProductViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import io.paperdb.Paper


class HomeActivity : AppCompatActivity()//, NavigationView.OnNavigationItemSelectedListener
{


    private lateinit var productRefs: DatabaseReference
    private lateinit var recyclerView:RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private var type=""


    private var mAppBarConfiguration: AppBarConfiguration? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = "Home"

        Paper.init(this)

        val intent= intent
        var bundle=intent.extras



        if(bundle!=null)
        {
            type= intent.extras!!.get("Admin").toString()
        }



        productRefs=FirebaseDatabase.getInstance().reference.child("Products")


        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view ->

            if(type!="Admin"){
                val intent = Intent(this@HomeActivity, CartActivity::class.java)
                startActivity(intent)

            }

        }


        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)


        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration!!)
        NavigationUI.setupWithNavController(navigationView, navController)
        navigationView.setNavigationItemSelectedListener {
            val id = it.itemId


            if (id == R.id.nav_cart) {

                if(type!="Admin"){
                    val intent = Intent(this@HomeActivity, CartActivity::class.java)
                    startActivity(intent)


                }


            }
            else if (id == R.id.nav_search) {

                if(type!="Admin"){
                    val intent = Intent(this@HomeActivity, SearchProductsActivity::class.java)
                    startActivity(intent)

                }



            }
            else if (id == R.id.nav_categories) {


            }
            else if (id == R.id.nav_settings) {

                if(type!="Admin"){

                    val intent = Intent(this@HomeActivity, SettingActivity::class.java)
                    startActivity(intent)


                }

            }
            else if (id == R.id.nav_logout) {

                if(type!="Admin"){

                    Paper.book().destroy()
                    val intent = Intent(this@HomeActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)

                }




            }

            val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
            drawer.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }
        val headerView = navigationView.getHeaderView(0)


        val userNameTextView = headerView.findViewById<TextView>(R.id.user_profile_name)
        val profileImageView: CircleImageView = headerView.findViewById(R.id.user_profile_image)


        if(type!="Admin"){
            userNameTextView.text = Prevalent.currentOnlineUser.name

            Picasso.get().load(Prevalent.currentOnlineUser.image).placeholder(R.drawable.profile).into(profileImageView)

        }


        recyclerView = findViewById(R.id.recycler_menu)
        recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return (NavigationUI.navigateUp(navController, mAppBarConfiguration!!)
                || super.onSupportNavigateUp())
    }



    override fun onStart() {
        super.onStart()

        val options = FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productRefs.orderByChild("productState").equalTo("Approved"), Products::class.java)
                .build()

       val adapter: FirebaseRecyclerAdapter<Products, ProductViewHolder> = object :FirebaseRecyclerAdapter<Products, ProductViewHolder>(options)
       {
           override fun onBindViewHolder(holder: ProductViewHolder, p1: Int, model: Products)
           {
               holder.txtProductName.text = model.pname
               holder.txtProductDescription.text = model.description
               holder.txtProductPrice.text = getString(R.string.priceAdapter, model.price)
               Picasso.get().load(model.image).into(holder.imageView)

               holder.itemView.setOnClickListener {


                   if(type=="Admin"){
                       val intent = Intent(this@HomeActivity, AdminMaintainProductsActivity::class.java)
                       intent.putExtra("pid",model.pid)
                       startActivity(intent)
                   }
                   else{
                       val intent = Intent(this@HomeActivity, ProductDetailActivity::class.java)
                       intent.putExtra("pid",model.pid)
                       startActivity(intent)

                   }


               }
           }

           override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder
           {
                val view:View =LayoutInflater.from(parent.context).inflate(R.layout.products_items_layout, parent, false)
                return ProductViewHolder(view)

           }

       }

        recyclerView.adapter = adapter
        adapter.startListening()

    }

    override fun onStop() {
        super.onStop()

    }


//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        // Handle navigation view item clicks here.
//        val id = item.itemId
//
//
//        if (id == R.id.nav_cart) {
//
//        }
//        else if (id == R.id.nav_orders) {
//
//
//        }
//        else if (id == R.id.nav_categories) {
//
//
//        }
//        else if (id == R.id.nav_settings) {
//
//            val intent = Intent(this@HomeActivity, SettingActivity::class.java)
//            startActivity(intent)
//        }
//        else if (id == R.id.nav_logout) {
//
//            Paper.book().destroy()
//            val intent = Intent(this@HomeActivity, MainActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            startActivity(intent)
//            finish()
//        }
//
//        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
//        drawer.closeDrawer(GravityCompat.START)
//        return true
//    }


}