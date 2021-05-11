package com.ps12027_cuongnt.alphasupplements.model

class ProductType {
    var productTypeID: Int = 0
    var name: String = ""

    constructor()
    constructor(
        productTypeID: Int,
        name: String
    ) {
        this.productTypeID = productTypeID
        this.name = name
    }


}