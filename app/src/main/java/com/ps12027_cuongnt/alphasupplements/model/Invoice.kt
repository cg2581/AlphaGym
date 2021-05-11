package com.ps12027_cuongnt.alphasupplements.model

class Invoice {
    var invoiceID: Int
    var date: String
    var promotion: String
    var seller: Int
    var customer: Int
    var status: Int

    constructor(
        invoiceID: Int,
        date: String,
        promotion: String,
        seller: Int,
        customer: Int,
        status: Int
    ) {
        this.invoiceID = invoiceID
        this.date = date
        this.promotion = promotion
        this.seller = seller
        this.customer = customer
        this.status = status
    }
}