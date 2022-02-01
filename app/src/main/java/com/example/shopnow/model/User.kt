package com.example.shopnow.model


class User {
    var name: String? = null

    var phone: String? = null

    var password: String? = null

    var image: String? = null

    var address:String? = null


    private constructor() {

    }
    constructor(name: String?, phone: String?,password:String?,image:String?,address:String?) {
        this.name = name
        this.phone = phone
        this.password = password
        this.image=image
        this.address=address
    }










}
