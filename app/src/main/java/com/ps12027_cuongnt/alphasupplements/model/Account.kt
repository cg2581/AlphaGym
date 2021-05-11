package com.ps12027_cuongnt.alphasupplements.model

class Account{
    var accountID: Int
    var password: String
    var userID: Int

    constructor(accountID: Int, password: String, userID: Int) {
        this.accountID = accountID
        this.password = password
        this.userID = userID
    }
}