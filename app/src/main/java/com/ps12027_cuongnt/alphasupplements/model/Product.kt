package com.ps12027_cuongnt.alphasupplements.model

class Product {
    var productID: Int = 0
    var type: Int = 0
    var name: String = ""
    var description: String = ""
    var price: Int = 0
    var image: ByteArray? = null
    var use: Int = 0

    constructor()

    constructor(
        productID: Int,
        type: Int,
        name: String,
        description: String,
        price: Int,
        image: ByteArray?,
        use: Int
    ) {
        this.productID = productID
        this.type = type
        this.name = name
        this.description = description
        this.price = price
        this.image = image
        this.use = use
    }
}