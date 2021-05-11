package com.ps12027_cuongnt.alphasupplements.model

class InvoiceDetail {
    var invoiceDetailID: Int
    var invoiceID: Int
    var productID: Int
    var amount: Int
    constructor(invoiceDetailID: Int, invoiceID: Int, productID: Int, amount: Int) {
        this.invoiceDetailID = invoiceDetailID
        this.invoiceID = invoiceID
        this.productID = productID
        this.amount = amount
   }
}