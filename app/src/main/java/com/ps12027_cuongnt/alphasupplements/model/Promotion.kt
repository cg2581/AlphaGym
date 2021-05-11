package com.ps12027_cuongnt.alphasupplements.model

class Promotion {
    var id: Int = 0
    var code: String = ""
    var amount: Int = 0
    var startDate: String = ""
    var endDate: String = ""

    constructor()
    constructor(id: Int, code: String, amount: Int, startDate: String, endDate: String) {
        this.id = id
        this.code = code
        this.amount = amount
        this.startDate = startDate
        this.endDate = endDate
    }
}