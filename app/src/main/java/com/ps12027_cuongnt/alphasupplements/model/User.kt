package com.ps12027_cuongnt.alphasupplements.model


class User {
    var userID: Int = 0
    var type: Int = 0
    var fullname: String = ""
    var phoneNumber: String = ""
    var address: String = ""
    var image: ByteArray? = null

    constructor()
    constructor(
        userID: Int,
        type: Int,
        fullname: String,
        phoneNumber: String,
        address: String,
        image: ByteArray?
    ) {
        this.userID = userID
        this.type = type
        this.fullname = fullname
        this.phoneNumber = phoneNumber
        this.address = address
        this.image = image
    }
}