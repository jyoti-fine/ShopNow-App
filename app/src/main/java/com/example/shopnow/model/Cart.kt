package com.example.shopnow.model

class Cart {

    var pid: String? = null
    lateinit var pname: String
    lateinit var price: String
    lateinit var quantity: String
   lateinit var discount:String


    constructor()
    constructor(pid:String,pname: String,price:String,quantity: String,discount:String) {
        this.pid = pid
        this.pname = pname
        this.quantity = quantity
        this.discount = discount
        this.price=price
    }

}