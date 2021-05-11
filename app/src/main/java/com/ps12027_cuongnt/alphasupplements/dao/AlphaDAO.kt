package com.ps12027_cuongnt.alphasupplements.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.ps12027_cuongnt.alphasupplements.database.DBHelper
import com.ps12027_cuongnt.alphasupplements.fragment.manage.StatisticFragment
import com.ps12027_cuongnt.alphasupplements.model.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AlphaDAO(context: Context) {
    private var dbHelper: DBHelper? = null
    private var db: SQLiteDatabase? = null

    init {
        dbHelper = DBHelper(context)
    }

    /*  START PRODUCT  */
    private fun getProductType(
        sql: String?,
        vararg x: String?
    ): ArrayList<ProductType> {
        val productTypeList: ArrayList<ProductType> = ArrayList<ProductType>()
        db = dbHelper!!.readableDatabase
        val cs = db!!.rawQuery(sql, x)
        cs.moveToFirst()
        while (!cs.isAfterLast) {
            try {
                val productID = cs.getString(0).toInt()
                val name = cs.getString(1)
                val productType =
                    ProductType(
                        productID,
                        name
                    )
                productTypeList.add(productType)
                cs.moveToNext()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        cs.close()
        return productTypeList
    }

    fun countProductByProductID(productTypeID: Int): Int {
        db = dbHelper!!.readableDatabase
        val cs =
            db!!.rawQuery(
                "SELECT * FROM PRODUCT WHERE Type=?",
                arrayOf(productTypeID.toString())
            )
        return cs.count
    }

    fun getAllProductType(): ArrayList<ProductType> {
        val sql =
            "SELECT * FROM PRODUCTTYPE ORDER BY PRODUCTTYPEID ASC"
        return getProductType(sql)
    }

    fun getProductTypeByID(id: Int): ProductType {
        val sql =
            "SELECT * FROM PRODUCTTYPE Where PRODUCTTYPEID=?"
        val productTypelist: ArrayList<ProductType> = getProductType(sql, id.toString())
        return productTypelist[0]
    }


    fun addProductType(productType: ProductType): Boolean {
        db = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("Name", productType.name)
        val r = db!!.insert("PRODUCTTYPE", null, contentValues)
        return r > 0
    }

    fun updateProductType(productType: ProductType): Boolean {
        db = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("Name", productType.name)
        var r = db!!.update(
            "PRODUCTTYPE",
            contentValues,
            "ProductTypeID=?",
            arrayOf(productType.productTypeID.toString())
        )
        return r > 0
    }

    fun deleteProductType(productType: ProductType): Boolean {
        db = dbHelper!!.writableDatabase
        val r = db!!.delete(
            "PRODUCTTYPE",
            "ProductTypeID=?",
            arrayOf(productType.productTypeID.toString())
        )
        return r > 0
    }


    fun getProductTypeName(productTypeID: Int): String {
        val sql = "SELECT * FROM PRODUCTTYPE WHERE ProductTypeID=?"
        val categoryArrayList: ArrayList<ProductType>? =
            getProductType(sql, productTypeID.toString())
        return categoryArrayList!![0].name
    }


    // Product

    private fun getProduct(
        sql: String?,
        vararg x: String?
    ): ArrayList<Product> {
        val productList: ArrayList<Product> = ArrayList<Product>()
        db = dbHelper!!.readableDatabase
        val cs = db!!.rawQuery(sql, x)
        cs.moveToFirst()
        while (!cs.isAfterLast) {
            try {
                val productID = cs.getInt(0)
                val type = cs.getInt(1)
                val name = cs.getString(2)
                val description: String = cs.getString(3)
                val price = cs.getInt(4)
                val image = cs.getBlob(5)
                val use = cs.getInt(6)
                val product =
                    Product(
                        productID,
                        type,
                        name,
                        description,
                        price,
                        image,
                        use
                    )
                productList.add(product)
                cs.moveToNext()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        cs.close()
        db!!.close()
        return productList
    }


    fun getAllProduct(): ArrayList<Product>? {
        val sql =
            "SELECT * FROM PRODUCT ORDER BY NAME ASC"
        return getProduct(sql)
    }

    fun getAllProductUse(): ArrayList<Product>? {
        val sql =
            "SELECT * FROM PRODUCT WHERE Use = 1 ORDER BY Name ASC"
        return getProduct(sql)
    }

    fun getProductByID(id: Int): Product {
        val sql =
            "SELECT * FROM PRODUCT WHERE ProductID=?"
        val productList: ArrayList<Product> = getProduct(sql, id.toString())
        return productList[0]
    }


    fun addProduct(product: Product): Boolean {
        db = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("Name", product.name)
        contentValues.put("Type", product.type.toString())
        contentValues.put("Description", product.description)
        contentValues.put("Price", product.price.toString())
        contentValues.put("Image", product.image)
        val r = db!!.insert("PRODUCT", null, contentValues)
        return r > 0
    }

    fun updateProduct(product: Product): Boolean {
        db = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("Name", product.name)
        contentValues.put("Type", product.type.toString())
        contentValues.put("Description", product.description)
        contentValues.put("Price", product.price.toString())
        contentValues.put("Image", product.image)
        contentValues.put("Use", product.use)
        val r = db!!.update(
            "PRODUCT",
            contentValues,
            "ProductID=?",
            arrayOf(product.productID.toString())
        )
        return r > 0
    }

    fun deleteProduct(product: Product): Boolean {
        db = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("Use", 0)
        val r = db!!.update(
            "PRODUCT",
            contentValues,
            "ProductID=?",
            arrayOf(product.productID.toString())
        )
        return r > 0
    }
    /*  END PRODUCT  */

    /*  START USER */

    private fun getUser(
        sql: String?,
        vararg x: String?
    ): ArrayList<User> {
        val userList: ArrayList<User> = ArrayList()
        db = dbHelper!!.readableDatabase
        val cs = db!!.rawQuery(sql, x)
        cs.moveToFirst()
        while (!cs.isAfterLast) {
            try {
                val userID = cs.getString(0).toInt()
                val type = cs.getString(1).toInt()
                val fullName = cs.getString(2)
                val phoneNumber: String = cs.getString(3)
                val address = cs.getString(4)
                val image = cs.getBlob(5)
                val user =
                    User(
                        userID,
                        type,
                        fullName,
                        phoneNumber,
                        address,
                        image
                    )
                userList.add(user)
                cs.moveToNext()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        cs.close()
        return userList
    }


    fun getAllUser(): ArrayList<User>? {
        val sql =
            "SELECT * FROM USER ORDER BY UserID ASC"
        return getUser(sql)
    }

    fun getAllUserByType(type: Int): ArrayList<User>? {
        val sql =
            "SELECT * FROM USER WHERE Type=? ORDER BY UserID ASC"
        return getUser(sql, type.toString())
    }

    fun getUserName(userID: Int): String {
        db = dbHelper!!.readableDatabase
        val cs = db!!.rawQuery("SELECT * FROM USER Where UserID=?", arrayOf(userID.toString()))
        var name = ""
        cs.moveToFirst()
        while (!cs.isAfterLast) {
            try {
                name = cs.getString(2)
                cs.moveToNext()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        cs.close()
        return name
    }

    fun checkDupPhoneNumber(phoneNumber: String): Boolean {
        db = dbHelper!!.readableDatabase
        val cs = db!!.rawQuery("SELECT * FROM USER Where PhoneNumber=?", arrayOf(phoneNumber))
        var check = false
        if (cs.count == 0) {
            check = false
        } else {
            check = true
        }
        return check
    }

    fun checkDupPhoneNumberUpdate(phoneNumber: String, currentPhoneNumber: String): Boolean {
        db = dbHelper!!.readableDatabase
        val cs = db!!.rawQuery(
            "SELECT * FROM USER Where PhoneNumber=? And PhoneNumber <> ?",
            arrayOf(phoneNumber, currentPhoneNumber)
        )
        var check = false
        if (cs.count == 0) {
            check = false
        } else {
            check = true
        }
        return check
    }

    fun getUserByID(id: Int): User {
        val sql =
            "SELECT * FROM USER WHERE UserID=?"
        val userList: ArrayList<User> = getUser(sql, id.toString())
        return userList[0]
    }

    fun getAllCustomer(): ArrayList<User> {
        val sql =
            "SELECT * FROM USER WHERE Type=3 AND UserID>0"
        return getUser(sql)
    }

    fun getAllCustomerSelect(): ArrayList<User> {
        val sql =
            "SELECT * FROM USER WHERE Type=3 ORDER BY UserID ASC"
        return getUser(sql)
    }

    fun getAllCustomerToEmployeeSelect(): ArrayList<User> {
        val sql =
            "SELECT * FROM USER WHERE Type=3 AND UserID <> 0 ORDER BY UserID ASC"
        return getUser(sql)
    }

    fun getAllEmployee(): ArrayList<User> {
        val sql =
            "SELECT * FROM USER WHERE Type=2"
        return getUser(sql)
    }

    fun addUser(user: User): Boolean {
        db = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("Type", user.type)
        contentValues.put("FullName", user.fullname)
        contentValues.put("PhoneNumber", user.phoneNumber)
        contentValues.put("Address", user.address)
        contentValues.put("Image", user.image)
        val r = db!!.insert("USER", null, contentValues)
        return r > 0
    }

    fun updateUser(user: User): Boolean {
        db = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("FullName", user.fullname)
        contentValues.put("Type", user.type)
        contentValues.put("PhoneNumber", user.phoneNumber)
        contentValues.put("Address", user.address)
        contentValues.put("Image", user.image)
        var r = db!!.update(
            "USER",
            contentValues,
            "UserID=?",
            arrayOf(user.userID.toString())
        )
        return r > 0
    }

    fun deleteUser(user: User): Boolean {
        db = dbHelper!!.writableDatabase
        var r = db!!.delete(
            "USER",
            "UserID=?",
            arrayOf(user!!.userID.toString())
        )
        return if (r <= 0) {
            false
        } else true
    }
    /* END USER */

    /* START ACCOUNT */

    fun getAccount(
        sql: String?,
        vararg x: String?
    ): ArrayList<Account> {
        val accountList: ArrayList<Account> = ArrayList<Account>()
        db = dbHelper!!.readableDatabase
        val cs = db!!.rawQuery(sql, x)
        cs.moveToFirst()
        while (!cs.isAfterLast) {
            try {
                val accountID = cs.getString(0).toInt()
                val password = cs.getString(1)
                val userID = cs.getString(2).toInt()
                val account =
                    Account(
                        accountID,
                        password,
                        userID
                    )
                accountList.add(account)
                cs.moveToNext()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        cs.close()
        return accountList
    }

    fun getAccountByUserID(userID: Int): Account? {
        val sql =
            "SELECT * FROM ACCOUNT WHERE UserID=?"
        val accountList: ArrayList<Account> = getAccount(sql, userID.toString())
        return if (accountList.size != 0) {
            accountList[0]
        } else {
            null
        }

    }

    fun addAccount(account: Account): Boolean {
        db = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("Password", account.password)
        contentValues.put("UserID", account.userID)
        val r = db!!.insert("ACCOUNT", null, contentValues)
        return r > 0
    }

    fun deleteAccount(userID: Int): Boolean {
        db = dbHelper!!.writableDatabase
        var r = db!!.delete(
            "ACCOUNT",
            "UserID=?",
            arrayOf(userID.toString())
        )
        return r > 0
    }

    fun login(phoneNumber: String, password: String): User? {
        val sql =
            "SELECT * FROM USER INNER JOIN ACCOUNT ON ACCOUNT.UserID = User.UserID WHERE User.PhoneNumber=? AND Account.Password=?"
        val userList: ArrayList<User>? = getUser(sql, *arrayOf(phoneNumber, password))
        return if (userList!!.size > 0) {
            getUserByID(userList[0].userID)
        } else {
            null
        }
    }

    fun changePassword(userID: Int, oldPass: String, newPass: String): Boolean {
        val sql = "SELECT * FROM ACCOUNT WHERE UserID=? AND Password=?"
        val userList = getAccount(sql, *arrayOf(userID.toString(), oldPass))
        Log.d("changePass", "changePassword: " + userList.size)
        return if (userList.size != 0) {

            db = dbHelper!!.writableDatabase
            val contentValues = ContentValues()
            contentValues.put("Password", newPass)

            val r = db!!.update(
                "ACCOUNT",
                contentValues,
                "UserID=?", arrayOf(userID.toString())
            )
            r > 0
        } else {
            false
        }
    }
    /* END ACCOUNT */


    /*  Invoice  */


    fun getInvoice(
        sql: String?,
        vararg x: String?
    ): ArrayList<Invoice> {
        val invoiceList: ArrayList<Invoice> = ArrayList<Invoice>()
        db = dbHelper!!.readableDatabase
        val cs = db!!.rawQuery(sql, x)
        cs.moveToFirst()
        while (!cs.isAfterLast) {
            try {
                val invoiceID = cs.getString(0).toInt()
                val date = cs.getString(1)
                val promotionCode = cs.getString(2)
                val sellerID = cs.getString(3).toInt()
                val customerID = cs.getString(4).toInt()
                val status = cs.getString(5).toInt()
                val invoice =
                    Invoice(
                        invoiceID, date, promotionCode, sellerID, customerID, status
                    )
                invoiceList.add(invoice)
                cs.moveToNext()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        cs.close()
        return invoiceList
    }


    fun getAllInvoice(): ArrayList<Invoice>? {
        val sql =
            "SELECT * FROM INVOICE WHERE Status = 1 ORDER BY DATEX DESC"
        return getInvoice(sql)
    }


    fun getInvoiceBySellerID(id: Int): Invoice? {
        val sql =
            "SELECT * FROM INVOICE WHERE SellerID=? ORDER BY DATE DESC"
        val invoiceList: ArrayList<Invoice> = getInvoice(sql, id.toString())
        if (invoiceList.size != 0) {
            return invoiceList[0]
        } else {
            return null
        }

    }

    fun getInvoiceByInvoiceID(invoiceID: Int): Invoice? {
        val sql =
            "SELECT * FROM INVOICE WHERE invoiceID=?"
        val invoiceList: ArrayList<Invoice> = getInvoice(sql, invoiceID.toString())
        if (invoiceList.size != 0) {
            return invoiceList[0]
        } else {
            return null
        }

    }

    fun getInvoiceTempPrice(invoice: Invoice): Int {
        db = dbHelper!!.readableDatabase
        val cs =
            db!!.rawQuery(
                // - (SELECT PROMOTION.AMOUNT FROM PROMOTION WHERE PROMOTION.Code=?)
                "SELECT sum(PRODUCT.Price*INVOICEDETAIL.Amount) FROM INVOICEDETAIL INNER JOIN PRODUCT ON PRODUCT.ProductID = INVOICEDETAIL.ProductID WHERE InvoiceID=?",
                arrayOf(invoice.invoiceID.toString())
            )
        var price = 0
        cs.moveToFirst()
        while (!cs.isAfterLast) {
            try {
                price = cs.getInt(0)
                cs.moveToNext()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        cs.close()
        return price
    }

    fun getInvoiceTotal(invoice: Invoice): Int {
        db = dbHelper!!.readableDatabase
        val cs =
            db!!.rawQuery(
                "SELECT (sum(PRODUCT.Price * INVOICEDETAIL.Amount) - (SELECT Amount FROM PROMOTION WHERE PROMOTION.Code= upper(?))) FROM INVOICEDETAIL INNER JOIN PRODUCT ON PRODUCT.ProductID = INVOICEDETAIL.ProductID WHERE InvoiceID=?",
                arrayOf(invoice.promotion, invoice.invoiceID.toString())
            )
        var price = 0
        cs.moveToFirst()
        while (!cs.isAfterLast) {
            try {
                price = cs.getInt(0)
                cs.moveToNext()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        cs.close()
        return price
    }


    fun getInvoiceBySellerIDAndStatus(id: Int, status: Int): Invoice? {
        val sql =
            "SELECT * FROM INVOICE WHERE SellerID=? AND Status=? ORDER BY DATEX DESC"
        val invoiceList: ArrayList<Invoice> = getInvoice(sql, id.toString(), status.toString())
        return if (invoiceList.size != 0) {
            invoiceList[0]
        } else {
            null
        }

    }

    fun getInvoiceCountBySellerID(id: Int): Int? {
        val sql =
            "SELECT * FROM INVOICE WHERE SellerID=?"
        val invoiceList: ArrayList<Invoice> = getInvoice(sql, id.toString())
        return invoiceList.size

    }

    fun addInvoice(invoice: Invoice): Boolean {
        db = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("DateX", invoice.date)
        contentValues.put("Code", invoice.promotion)
        contentValues.put("SellerID", invoice.seller)
        contentValues.put("CustomerID", invoice.customer)
        contentValues.put("Status", invoice.status)
        val r = db!!.insert("INVOICE", null, contentValues)
        return r > 0
    }

    fun deleteInvoiceDetailByID(id: Int): Boolean {
        db = dbHelper!!.writableDatabase
        var r = db!!.delete(
            "INVOICEDETAIL",
            "INVOICEDETAILID=?",
            arrayOf(id.toString())
        )
        return r > 0
    }

    fun updateInvoice(invoice: Invoice): Boolean {
        db = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("Code", invoice.promotion)
        contentValues.put("CustomerID", invoice.customer)
        val r = db!!.update(
            "INVOICE",
            contentValues,
            "InvoiceID=?",
            arrayOf(invoice.invoiceID.toString())
        )
        return r > 0
    }

    fun confirmInvoice(invoice: Invoice): Boolean {
        db = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("DateX", invoice.date)
        contentValues.put("Status", "1")
        val r = db!!.update(
            "INVOICE",
            contentValues,
            "InvoiceID=?",
            arrayOf(invoice.invoiceID.toString())
        )
        return r > 0
    }


    /*  End Invoice  */
    /* INVOICE DETAIL */
    private fun getInvoiceDetail(
        sql: String?,
        vararg x: String?
    ): ArrayList<InvoiceDetail> {
        val invoiceDetailList: ArrayList<InvoiceDetail> = ArrayList<InvoiceDetail>()
        db = dbHelper!!.readableDatabase
        val cs = db!!.rawQuery(sql, x)
        cs.moveToFirst()
        while (!cs.isAfterLast) {
            try {
                val invoiceDetailID = cs.getString(0).toInt()
                val invoiceID = cs.getString(1).toInt()
                val productID = cs.getString(2).toInt()
                val amount = cs.getString(3).toInt()
                val invoiceDetail =
                    InvoiceDetail(invoiceDetailID, invoiceID, productID, amount)
                invoiceDetailList.add(invoiceDetail)
                cs.moveToNext()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        cs.close()
        return invoiceDetailList
    }


    fun getAllInvoiceDetail(): ArrayList<InvoiceDetail>? {
        val sql =
            "SELECT * FROM INVOICEDETAIL"
        return getInvoiceDetail(sql)

    }

    fun getAllInvoiceDetailByInvoiceID(id: Int): ArrayList<InvoiceDetail>? {
        val sql =
            "SELECT * FROM INVOICEDETAIL WHERE InvoiceID=?"
        return getInvoiceDetail(sql, id.toString())

    }

    fun getAllInvoiceDetailByUserIDAndProductID(
        invoiceID: Int,
        productID: Int
    ): ArrayList<InvoiceDetail>? {
        val sql =
            "SELECT * FROM INVOICEDETAIL WHERE InvoiceID=? AND ProductID=?"
        return getInvoiceDetail(sql, invoiceID.toString(), productID.toString())

    }

    fun addInvoiceDetail(invoiceDetail: InvoiceDetail): Boolean {
        db = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("InvoiceID", invoiceDetail.invoiceID)
        contentValues.put("ProductID", invoiceDetail.productID)
        contentValues.put("Amount", invoiceDetail.amount)
        val invoiceDetailListInCart: ArrayList<InvoiceDetail>? =
            getAllInvoiceDetailByUserIDAndProductID(
                invoiceDetail.invoiceID,
                invoiceDetail.productID
            )
        return if (invoiceDetailListInCart!!.size == 0) {
            val r = db!!.insert("InvoiceDetail", null, contentValues)
            r > 0
        } else {
            false;
        }
    }


    fun updateInvoiceDetailAmount(invoiceDetail: InvoiceDetail): Boolean {
        db = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("Amount", invoiceDetail.amount)
        val r = db!!.update(
            "INVOICEDETAIL",
            contentValues,
            "ProductID=?",
            arrayOf(invoiceDetail.productID.toString())
        )
        return r > 0
    }


    /* END INVOICEDETAIL */
    /* PROMOTION */
    private fun getPromotion(
        sql: String?,
        vararg x: String?
    ): ArrayList<Promotion> {
        val promotionList: ArrayList<Promotion> = ArrayList<Promotion>()
        db = dbHelper!!.readableDatabase
        val cs = db!!.rawQuery(sql, x)
        cs.moveToFirst()
        while (!cs.isAfterLast) {
            try {
                val id = cs.getInt(0)
                val code = cs.getString(1)
                val amount = cs.getInt(2)
                val startDate = cs.getString(3)
                val endDate = cs.getString(4)
                val promotion =
                    Promotion(id, code, amount, startDate, endDate)
                promotionList.add(promotion)
                cs.moveToNext()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        cs.close()
        return promotionList
    }

    fun getAllPromotion(): ArrayList<Promotion>? {
        val sql =
            "SELECT * FROM PROMOTION WHERE Code <> ''"
        return getPromotion(sql)
    }

    fun getPromotionByPromotionID(promotionID: Int): Promotion {
        val sql =
            "SELECT * FROM PROMOTION WHERE PromotionID = ?"
        val promotionList = getPromotion(sql, promotionID.toString())
        return promotionList[0]
    }

    fun getPromotionByPromotionCode(promotionCode: String): Promotion? {
        val sql =
            "SELECT * FROM PROMOTION WHERE Code = upper(?)"
        val promotionList = getPromotion(sql, promotionCode)
        return if (promotionList.size > 0) {
            promotionList[0]
        } else {
            null
        }
    }


    fun addPromotion(promotion: Promotion): Boolean {
        db = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("Code", promotion.code)
        contentValues.put("Amount", promotion.amount)
        contentValues.put("StartDate", promotion.startDate)
        contentValues.put("EndDate", promotion.endDate)
        val r = db!!.insert("PROMOTION", null, contentValues)
        return r > 0
    }

    fun updatePromotion(promotion: Promotion): Boolean {
        db = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("Code", promotion.code)
        contentValues.put("Amount", promotion.amount)
        contentValues.put("StartDate", promotion.startDate)
        contentValues.put("EndDate", promotion.endDate)
        val r = db!!.update(
            "PROMOTION",
            contentValues,
            "PromotionID=?",
            arrayOf(promotion.id.toString())
        )
        return r > 0
    }

    fun deletePromotion(promotion: Promotion): Boolean {
        db = dbHelper!!.writableDatabase
        val r = db!!.delete(
            "PROMOTION",
            "PromotionID=?",
            arrayOf(promotion.id.toString())
        )
        return r > 0
    }

    fun checkPromotion(code: String): Int {
        val sql = "SELECT * FROM PROMOTION WHERE CODE=?"
        val promotionList: ArrayList<Promotion> = getPromotion(sql, code)
        return if (promotionList.size != 0) {
            promotionList[0].amount
        } else {
            0
        }
    }


    /* END PROMOTION */
    /* START STATISTIC */

    fun getStatisticTotal(): Int {

        db = dbHelper!!.readableDatabase
        val cs =
            db!!.rawQuery(
                "SELECT SUM(Product.Price * InvoiceDetail.Amount - Promotion.Amount) FROM Invoice INNER JOIN InvoiceDetail on InvoiceDetail.InvoiceID = Invoice.InvoiceID JOIN Product ON Product.ProductID = InvoiceDetail.ProductID JOIN Promotion ON Promotion.Code = Invoice.Code",
                null
            )
        cs.moveToFirst()
        val price = cs.getInt(0)
        cs.close()
        return price

    }

    fun getStatisticTotalToday(): Int {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDateTime.now().format(formatter)
        Log.d("DATE", "$date")
        val from = date.plus(" 00:00:01")
        val to = date.plus(" 23:59:59")
        Log.d("DATE", "$from")
        Log.d("DATE", "$to")
        db = dbHelper!!.readableDatabase
        val cs =
            db!!.rawQuery(
                "SELECT SUM(Product.Price * InvoiceDetail.Amount - Promotion.Amount) FROM Invoice INNER JOIN InvoiceDetail on InvoiceDetail.InvoiceID = Invoice.InvoiceID JOIN Product ON Product.ProductID = InvoiceDetail.ProductID JOIN Promotion ON Promotion.Code = Invoice.Code WHERE Invoice.DateX BETWEEN ? AND ? AND Invoice.Status = 1",
                arrayOf(from, to)
            )
        cs.moveToFirst()
        val price = cs.getInt(0)
        cs.close()
        return price

    }

    fun getStatisticTotal(from: String, to: String): Int {

        db = dbHelper!!.readableDatabase
        val cs =
            db!!.rawQuery(
                "SELECT SUM(Product.Price * InvoiceDetail.Amount - Promotion.Amount) FROM Invoice INNER JOIN InvoiceDetail on InvoiceDetail.InvoiceID = Invoice.InvoiceID JOIN Product ON Product.ProductID = InvoiceDetail.ProductID JOIN Promotion ON Promotion.Code = Invoice.Code WHERE Invoice.DateX BETWEEN ? AND ? AND Invoice.Status = 1",
                arrayOf(from, to)
            )
        cs.moveToFirst()
        val price = cs.getInt(0)
        cs.close()
        return price

    }

    fun getSoldProductCount(from: String, to: String): Int {

        db = dbHelper!!.readableDatabase
        val cs =
            db!!.rawQuery(
                "SELECT sum(InvoiceDetail.Amount) FROM InvoiceDetail INNER JOIN Invoice ON Invoice.InvoiceID = InvoiceDetail.InvoiceID WHERE Invoice.DateX BETWEEN ? AND ?",
                arrayOf(from, to)
            )
        cs.moveToFirst()
        val count = cs.getInt(0)
        cs.close()
        return count

    }

    fun getInvoiceCountByDateRange(from: String, to: String): Int {

        db = dbHelper!!.readableDatabase
        val cs =
            db!!.rawQuery(
                "SELECT InvoiceID FROM INVOICE WHERE DateX BETWEEN ? AND ?",
                arrayOf(from, to)
            )
        return cs.count

    }

    fun getInvoiceCount(): Int {

        db = dbHelper!!.readableDatabase
        val cs =
            db!!.rawQuery(
                "SELECT InvoiceID FROM INVOICE",
                null
            )
        return cs.count

    }

    fun getUserIDByPhoneNumber(phoneNumber: String): Int {
        val sql = "SELECT UserID FROM USER WHERE PhoneNumber=?"
        val userList: ArrayList<User>? =
            getUser(sql, phoneNumber)
        return userList!![0].userID
    }

    fun updateAccount(account: Account): Boolean {
        db = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("Password", account.password)
        var r = db!!.update(
            "Account",
            contentValues,
            "AccountID=?",
            arrayOf(account.accountID.toString())
        )
        return r > 0
    }

    fun getAllProductTypeCount(
        dateFrom: String,
        dateTo: String
    ): ArrayList<StatisticFragment.ChartItem> {
        val list: ArrayList<StatisticFragment.ChartItem> = ArrayList()
        db = dbHelper!!.readableDatabase
        val sql =
            "SELECT ProductType.ProductTypeID, SUM(InvoiceDetail.Amount),SUM(InvoiceDetail.Amount * Product.Price)  FROM InvoiceDetail JOIN Product ON Product.ProductID = InvoiceDetail.ProductID JOIN ProductType ON ProductType.ProductTypeID = Product.Type JOIN Invoice ON Invoice.InvoiceID = InvoiceDetail.InvoiceID WHERE DateX BETWEEN ? AND ? GROUP BY ProductType.Name ORDER BY ProductType.ProductTypeID ASC"
        val cs = db!!.rawQuery(sql, arrayOf(dateFrom, dateTo))
        cs.moveToFirst()
        while (!cs.isAfterLast) {
            try {
                val name = cs.getInt(0)
                val value = cs.getInt(1)
                val total = cs.getInt(2)
                val chartItem = StatisticFragment.ChartItem(name, value, total)
                list.add(chartItem)
                cs.moveToNext()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        cs.close()
        return list
    }

    fun getAllProductTotalByProductID(
        id: Int,
        dateFrom: String,
        dateTo: String
    ): ArrayList<StatisticFragment.ProductData> {
        val list: ArrayList<StatisticFragment.ProductData> = ArrayList()
        db = dbHelper!!.readableDatabase
        val sql =
            "SELECT Product.name,sum(product.price * invoiceDetail.amount) FROM InvoiceDetail JOIN Product ON Product.ProductID = InvoiceDetail.ProductID JOIN ProductType ON ProductType.ProductTypeID = Product.type JOIN Invoice ON Invoice.InvoiceID = InvoiceDetail.InvoiceID WHERE ProductType.productTypeID = ? AND ( DateX BETWEEN ? AND ?) GROUP BY Product.name"
        val cs = db!!.rawQuery(sql, arrayOf(id.toString(), dateFrom, dateTo))
        cs.moveToFirst()
        while (!cs.isAfterLast) {
            try {
                val name = cs.getString(0)
                val total = cs.getInt(1)
                val productDataItem = StatisticFragment.ProductData(name, total)
                list.add(productDataItem)
                cs.moveToNext()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        cs.close()
        return list
    }


    /* END STATISTIC */


}