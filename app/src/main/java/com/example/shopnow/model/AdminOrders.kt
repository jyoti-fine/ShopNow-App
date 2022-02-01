package com.example.shopnow.model

class AdminOrders {

    lateinit var address:String
    var name: String? = null
    lateinit var phone:String
    lateinit var city:String
    lateinit var state:String
    lateinit var time:String
    lateinit var date:String
    lateinit var totalAmount:String

    constructor(){

    }

    constructor(name:String,phone:String,address:String,city:String,state:String,date:String,time:String,totalAmount:String)
    {
        this.name=name
        this.phone=phone
        this.address=address
        this.city=city
        this.state=state
        this.date=date
        this.time=time
        this.totalAmount=totalAmount


    }

}