package com.example.shopnow.Buyers

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shopnow.R
import com.example.shopnow.prevalent.Prevalent
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.HashMap

class SettingActivity : AppCompatActivity() {

    private lateinit var profileImageView: CircleImageView
    private lateinit var fullNameEditText: EditText
    private lateinit var userPhoneEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var profileChangeTextBtn: TextView
    private lateinit var closeTextBtn: TextView
    private lateinit var saveTextBtn: TextView
    private lateinit var securityQuestionsBtn:Button

    private lateinit var imageUri:Uri

    private lateinit var storageProfilePictureRef: StorageReference
    private var checker=""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        storageProfilePictureRef=FirebaseStorage.getInstance().reference.child("Profile pictures")

        profileImageView=findViewById(R.id.settings_profile_image)
        fullNameEditText=findViewById(R.id.settings_full_name)
        userPhoneEditText=findViewById(R.id.settings_phone_number)
        addressEditText=findViewById(R.id.settings_address)
        profileChangeTextBtn=findViewById(R.id.profile_image_change_btn)
        closeTextBtn=findViewById(R.id.close_settings_btn)
        saveTextBtn=findViewById(R.id.update_account_settings_btn)

        userInfoDisplay(profileImageView, fullNameEditText, userPhoneEditText, addressEditText)

        securityQuestionsBtn=findViewById(R.id.security_questions_btn)
        securityQuestionsBtn.setOnClickListener {

            val intent = Intent(this@SettingActivity, ResetPasswordActivity::class.java)
            intent.putExtra("check","settings")
            startActivity(intent)
            finish()

        }

        closeTextBtn.setOnClickListener{
            finish()
        }

        profileChangeTextBtn.setOnClickListener{
            checker="clicked"


                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(this)



        }

        saveTextBtn.setOnClickListener{
            if (checker == "clicked")
            {
                userInfoSaved()
            }
            else
            {
                updateOnlyUserInfo()
            }
        }



        }


    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode== RESULT_OK && data!=null)
        {
            val result:CropImage.ActivityResult=CropImage.getActivityResult(data)

            this.imageUri =result.uri

            profileImageView.setImageURI(imageUri)


        }

        else
        {
            Toast.makeText(this@SettingActivity, "Error, try again", Toast.LENGTH_LONG).show()
            val intent = Intent(this@SettingActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun updateOnlyUserInfo()
    {
        val ref=FirebaseDatabase.getInstance().reference.child("User")

        val userMap: MutableMap<String, Any> = HashMap()

        userMap["name"] = fullNameEditText.text.toString()
        userMap["address"] = addressEditText.text.toString()
        userMap["phoneOrder"] = userPhoneEditText.text.toString()

        ref.child(Prevalent.currentOnlineUser.phone!!).updateChildren(userMap)

        val intent = Intent(this@SettingActivity, HomeActivity::class.java)
        startActivity(intent)
        Toast.makeText(this@SettingActivity, "Profile Info update successfully.", Toast.LENGTH_LONG).show()
        finish()
    }

    private fun userInfoSaved()
    {
        if (TextUtils.isEmpty(fullNameEditText.text.toString()))
        {
            Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressEditText.text.toString()))
        {
            Toast.makeText(this, "Name is address.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(userPhoneEditText.text.toString()))
        {
            Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if(checker == "clicked")
        {
            uploadImage();
        }
    }


    private fun uploadImage() {
       @Suppress("DEPRECATION")
          var progressDialog: ProgressDialog=ProgressDialog(this)
          progressDialog.setTitle("Update Profile")
          progressDialog.setMessage("Please wait...while we are updating your account information")
          progressDialog.setCanceledOnTouchOutside(false)
          progressDialog.show()

        val fileRef=storageProfilePictureRef.child(Prevalent.currentOnlineUser.phone + ".jpg")   //firebase-storage
        val uploadTask = fileRef.putFile(imageUri!!)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }
            //get url not the link
            // here do not know if image is uploaded successfully
            fileRef.downloadUrl
        }.addOnCompleteListener{
            if(it.isSuccessful)
            {
                 val downloadUrl=it.result         //get url to store in database// it gets from download url as result

                val myUrl=downloadUrl.toString()

                val ref=FirebaseDatabase.getInstance().reference.child("User")

                val userMap: MutableMap<String, Any> = HashMap()

                userMap["name"] = fullNameEditText.text.toString()                                     //firebase-database
                userMap["address"] = addressEditText.text.toString()
                userMap["phoneOrder"] = userPhoneEditText.text.toString()
                userMap["image"] = myUrl

                ref.child(Prevalent.currentOnlineUser.phone!!).updateChildren(userMap)

                progressDialog.dismiss()

                val intent = Intent(this@SettingActivity, HomeActivity::class.java)
                startActivity(intent)
                Toast.makeText(this@SettingActivity, "Profile Info update successfully.", Toast.LENGTH_LONG).show()
                finish()

            }
            else
            {
                progressDialog.dismiss()
                Toast.makeText(this@SettingActivity, "Error.", Toast.LENGTH_LONG).show()
            }
            }
    }



    private fun userInfoDisplay(profileImageView: CircleImageView?, fullNameEditText: EditText?, userPhoneEditText: EditText?, addressEditText: EditText?)
    {
          val usersRef:DatabaseReference=FirebaseDatabase.getInstance().getReference("User").child(Prevalent.currentOnlineUser.phone!!)

        usersRef.addValueEventListener(object : ValueEventListener {


            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    if (snapshot.child("image").exists()) {
                        val image = snapshot.child("image").value.toString()
                        val name = snapshot.child("name").value.toString()
                        val phone = snapshot.child("phone").value.toString()
                        val address = snapshot.child("address").value.toString()

                        Picasso.get().load(image).into(profileImageView)
                        fullNameEditText?.setText(name)
                        userPhoneEditText?.setText(phone)
                        fullNameEditText?.setText(name)
                        addressEditText?.setText(address)


                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}
