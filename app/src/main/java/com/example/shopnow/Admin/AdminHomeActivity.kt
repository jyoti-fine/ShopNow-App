package com.example.shopnow.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.shopnow.Buyers.HomeActivity
import com.example.shopnow.Buyers.MainActivity
import com.example.shopnow.R

class AdminHomeActivity : AppCompatActivity() {

    private lateinit var logOutBtn: Button
    private lateinit var checkOrdersBtn: Button
    private lateinit var maintainOrdersBtn: Button
    private lateinit var checkApproveProductsBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        logOutBtn=findViewById(R.id.admin_logout_btn)
        checkOrdersBtn=findViewById(R.id.check_orders_btn)
        maintainOrdersBtn=findViewById(R.id.maintain_btn)
        checkApproveProductsBtn=findViewById(R.id.check_approve_orders_btn)

        maintainOrdersBtn.setOnClickListener {

            val intent = Intent(this@AdminHomeActivity, HomeActivity::class.java)
            intent.putExtra("Admin","Admin")
            startActivity(intent)


        }

        logOutBtn.setOnClickListener {
            val intent = Intent(this@AdminHomeActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()


        }

        checkOrdersBtn.setOnClickListener {
            val intent = Intent(this@AdminHomeActivity, AdminNewOrdersActivity::class.java)

            startActivity(intent)


        }

        checkApproveProductsBtn.setOnClickListener {
            val intent = Intent(this@AdminHomeActivity, CheckNewProductsActivity::class.java)

            startActivity(intent)


        }
    }
}