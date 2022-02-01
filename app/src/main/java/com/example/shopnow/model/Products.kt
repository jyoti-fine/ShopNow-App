package com.example.shopnow.model

class Products
 {
  var pname: String = ""
  var description: String = ""
  var price: String = ""
  var image: String = ""
  var category: String = ""
  var pid: String = ""
  var date: String = ""
  var time: String = ""
  var productState: String = ""


    constructor(){

    }

  constructor(pname: String?, description: String?, price: String?, image: String?, category: String?, pid: String?, date: String?, time: String?,productState: String?) {
   this.pname = pname!!
   this.description = description!!
   this.price = price!!
   this.image = image!!
   this.category = category!!
   this.pid = pid!!
   this.date = date!!
   this.time = time!!
   this.productState=productState!!
  }

 }