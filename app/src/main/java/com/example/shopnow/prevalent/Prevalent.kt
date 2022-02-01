package com.example.shopnow.prevalent

import com.example.shopnow.model.User

 class Prevalent {       //forget password,current User,remember me data users of users

     constructor() {
     }

     companion object {

         lateinit var currentOnlineUser: User
         val userPhoneKey = "UserPhone"
         val userPasswordKey = "UserPassword"

     }

 }