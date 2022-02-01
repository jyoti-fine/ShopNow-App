package com.example.shopnow.Sellers

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shopnow.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class SellerAddNewProductActivity : AppCompatActivity() {

    private lateinit var categoryName: String
    private lateinit var description: String
    private lateinit var price: String
    private lateinit var pName: String
    private lateinit var addNewProductButton: Button
    private lateinit var inputProductImage: ImageView
    private lateinit var inputProductName:EditText
    private lateinit var inputProductDescription:EditText
    private lateinit var inputProductPrice:EditText
    private var galleryPick:Int=1
    private lateinit var imageUri:Uri
    private lateinit var saveCurrentDate:String
    private lateinit var saveCurrentTime:String
    private lateinit var productRandomKey:String
    private lateinit var downloadImageUrl:String
    @Suppress("DEPRECATION")
    private lateinit var progressDialog: ProgressDialog
    private lateinit var productImagesRef:StorageReference
    private lateinit var productsRef:DatabaseReference
    private lateinit var sName:String
    private lateinit var sAddress:String
    private lateinit var sPhone:String
    private lateinit var sEmail:String
    private lateinit var sID:String
    private lateinit var sellerRef:DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_add_new_product)

        @Suppress("DEPRECATION")
        progressDialog = ProgressDialog(this)

        categoryName= intent.extras?.get("category").toString()

        productsRef=FirebaseDatabase.getInstance()
                .reference.child("Products")
        sellerRef=FirebaseDatabase.getInstance()
                .reference.child("Sellers")

        addNewProductButton=findViewById(R.id.add_new_product)
        inputProductImage=findViewById(R.id.select_product_image)
        inputProductName=findViewById(R.id.product_name)
        inputProductDescription=findViewById(R.id.product_description)
        inputProductPrice=findViewById(R.id.product_price)
        productImagesRef=FirebaseStorage.getInstance().reference.child("Product Images")


        inputProductImage.setOnClickListener{
            openGallery()
        }

        addNewProductButton.setOnClickListener{
            validateProductData()
        }

        sellerRef.child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    sName=snapshot.child("name").value.toString()
                    sAddress=snapshot.child("address").value.toString()
                    sPhone=snapshot.child("phone").value.toString()
                    sID=snapshot.child("sid").value.toString()
                    sEmail=snapshot.child("email").value.toString()

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

    private fun validateProductData() {

        description=inputProductDescription.text.toString()
        price=inputProductPrice.text.toString()
        pName=inputProductName.text.toString()

        when {
            imageUri == null -> {
                Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_LONG).show()
            }
            TextUtils.isEmpty(description) -> {
                Toast.makeText(this, "Please write product description...", Toast.LENGTH_LONG).show()
            }
            TextUtils.isEmpty(price) -> {
                Toast.makeText(this, "Please write product price...", Toast.LENGTH_LONG).show()
            }
            TextUtils.isEmpty(pName) -> {
                Toast.makeText(this, "Please write product name...", Toast.LENGTH_LONG).show()
            }
            else -> {
               storeProductInformation()
            }
        }


    }

    private fun storeProductInformation() {

        progressDialog.setTitle("Add New Product");
        @Suppress("DEPRECATION")
        progressDialog.setMessage("Dear Seller, please wait while we are adding the new product.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        val calendar = Calendar.getInstance()

        val currentDate = SimpleDateFormat("MMM dd, yyyy")
        saveCurrentDate = currentDate.format(calendar.time)

        val currentTime = SimpleDateFormat("HH:mm:ss a")
        saveCurrentTime = currentTime.format(calendar.time)

        productRandomKey=saveCurrentDate+saveCurrentTime


        val filePath :StorageReference= productImagesRef.child(imageUri.lastPathSegment + productRandomKey + ".jpg");

                     //first store the image in firebase to get its url to store url in firebase

        val uploadTask = filePath.putFile(imageUri)


        uploadTask.addOnFailureListener { e ->
            val message = e.toString()
            Toast.makeText(this@SellerAddNewProductActivity, "Error: $message", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
        }.addOnSuccessListener {
            // Toast.makeText(this@SellerAddNewProductActivity, "Product image uploaded successfully...", Toast.LENGTH_SHORT).show()
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                downloadImageUrl = filePath.downloadUrl.toString()               //get url not the link
                                                                              // here do not know if image is uploaded successfully
                filePath.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    downloadImageUrl = task.result.toString()          //if successfully upload, to get the link/url
                   // Toast.makeText(this@SellerAddNewProductActivity, "Got the product image Url successfully...", Toast.LENGTH_SHORT).show()
                    saveProductInfoToDatabase()
                }
            }
        }


    }

    private fun saveProductInfoToDatabase()
    {

        val productMap: MutableMap<String, Any> = HashMap()
        productMap["pid"] = productRandomKey
        productMap["date"] = saveCurrentDate
        productMap["time"] = saveCurrentTime
        productMap["description"] = description
        productMap["image"] = downloadImageUrl
        productMap["categoryName"] = categoryName
        productMap["price"] = price
        productMap["pname"] = pName
        productMap["productState"] = "Not Approved"
        productMap["sellerName"] = sName
        productMap["sellerAddress"]=sAddress
        productMap["sellerPhone"] = sPhone
        productMap["sellerEmail"] = sEmail
        productMap["sid"] = sID

        productsRef.child(productRandomKey).updateChildren(productMap).
                addOnCompleteListener { task->
                     if(task.isSuccessful){
                         val intent = Intent(this@SellerAddNewProductActivity, SellerHomeActivity::class.java)
                         startActivity(intent)
                         finish()

                         progressDialog.dismiss()
                         Toast.makeText(this@SellerAddNewProductActivity, "Product is added successfully..", Toast.LENGTH_SHORT).show()
                     }
                     else
                     {
                         progressDialog.dismiss();
                         val message = task.exception.toString();
                         Toast.makeText(this, "Error:  $message", Toast.LENGTH_SHORT).show();
                     }

                }






    }

    @Suppress("DEPRECATION")
    private fun openGallery() {
        val galleryIntent = Intent()
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, galleryPick)
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if(requestCode==galleryPick && resultCode== RESULT_OK && data!=null)
        {
            imageUri= data.data!!
            inputProductImage.setImageURI(imageUri)
        }
    }

}