package com.ps12027_cuongnt.alphasupplements.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBHelper(context: Context?) : SQLiteOpenHelper(context, "AlphaGym.sqlite", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        /* CREATE TABLE */
        // Account
        db!!.execSQL(
            "CREATE TABLE Account(AccountID INTEGER primary key autoincrement, Password CHAR,UserID INTEGER references User(UserID))"
        )
        // User Type
        db.execSQL(
            "CREATE TABLE UserType(UserTypeID INTEGER primary key autoincrement, Name CHAR)"
        )
        // User
        db.execSQL(
            "CREATE TABLE User(UserID INTEGER primary key autoincrement,Type INTEGER references UserType(UserTypeID),FullName CHAR,PhoneNumber CHAR,Address CHAR, Image BLOB)"
        )
        // ProductType
        db.execSQL(
            "CREATE TABLE ProductType(ProductTypeID INTEGER primary key autoincrement, Name CHAR)"
        )
        // Product
        db.execSQL(
            "CREATE TABLE Product(ProductID INTEGER primary key autoincrement,Type INTEGER references ProductType(ProductTypeID) ,Name CHAR,Description CHAR, Price INTEGER, Image BLOB, Use INTEGER)"
        )
        // Invoice
        db.execSQL(
            "CREATE TABLE Invoice(InvoiceID INTEGER primary key autoincrement,DateX CHAR,Code CHAR references Promotion(Code), SellerID INTEGER references User(UserID),CustomerID INTEGER references User(UserID), Status INTEGER)"
        )
        // InvoiceDetail
        db.execSQL(
            "CREATE TABLE InvoiceDetail(InvoiceDetailID INTEGER primary key autoincrement, InvoiceID INTEGER references Invoice(InvoiceID), ProductID INTEGER references Product(ProductID),Amount INTEGER)"
        )
        // Promotion

        db.execSQL("CREATE TABLE Promotion(PromotionID INTEGER primary key autoincrement, Code CHAR,Amount INTEGER,StartDate CHAR,EndDate CHAR)")

        /* INSERT DATA TO TABLE */

        // UserType
        db.execSQL("INSERT INTO UserType VALUES(1,'Quản lí')")
        db.execSQL("INSERT INTO UserType VALUES(2,'Nhân viên')")
        db.execSQL("INSERT INTO UserType VALUES(3,'Khách hàng')")
        // User
        db.execSQL("INSERT INTO User(UserID,Type,FullName,PhoneNumber,Address) VALUES(0,3,'Khách vãng lai','','')")
        db.execSQL("INSERT INTO User(Type,FullName,PhoneNumber,Address) VALUES(1,'Nguyễn Tấn Cường','0862090010','Bà Rịa - Vũng Tàu')")
        db.execSQL("INSERT INTO User(Type,FullName,PhoneNumber,Address) VALUES(2,'Trần Văn Nam','0909565854','TP.HCM')")
        db.execSQL("INSERT INTO User(Type,FullName,PhoneNumber,Address) VALUES(3,'Lê Văn Long','0987654321','Quảng Ngãi')")
        db.execSQL("INSERT INTO User(Type,FullName,PhoneNumber,Address) VALUES(3,'Mai Thanh Nghĩa','0125478963','An Giang')")
        db.execSQL("INSERT INTO User(Type,FullName,PhoneNumber,Address) VALUES(3,'Võ Anh Khoa','0932145687','Bình Phước')")
        db.execSQL("INSERT INTO User(Type,FullName,PhoneNumber,Address) VALUES(3,'Nguyễn Mai Lan','0125634789','Quảng Trị')")
        db.execSQL("INSERT INTO User(Type,FullName,PhoneNumber,Address) VALUES(3,'Trần Thị Huệ','0145236987','Hà Nội')")
        db.execSQL("INSERT INTO User(Type,FullName,PhoneNumber,Address) VALUES(3,'Lê Văn Tuấn','0962354178','Hà Nội')")
        db.execSQL("INSERT INTO User(Type,FullName,PhoneNumber,Address) VALUES(3,'Trần Thị Huệ','0962345178','Hà Nội')")
        db.execSQL("INSERT INTO User(Type,FullName,PhoneNumber,Address) VALUES(3,'Nguyễn Hoàng Minh','0987412563','Hà Nội')")
        db.execSQL("INSERT INTO User(Type,FullName,PhoneNumber,Address) VALUES(3,'Thái Văn Anh','0123456789','Hà Nội')")
        db.execSQL("INSERT INTO User(Type,FullName,PhoneNumber,Address) VALUES(3,'Phạm Minh Nhật','0932654178','Hà Nội')")
        //Account
        db.execSQL(
            "INSERT INTO Account(Password,UserID) VALUES('admin',1)"
        )
        db.execSQL(
            "INSERT INTO Account(Password,UserID) VALUES('nv1',2)"
        )
        // ProductType
        db.execSQL("INSERT INTO ProductType(Name) VALUES('Whey Protein')")
        db.execSQL("INSERT INTO ProductType(Name) VALUES('Sữa tăng cân')")
        db.execSQL("INSERT INTO ProductType(Name) VALUES('Tăng sức mạnh')")
        db.execSQL("INSERT INTO ProductType(Name) VALUES('Dầu cá')")
        db.execSQL("INSERT INTO ProductType(Name) VALUES('Animo, BCAA, Glutamin')")
        db.execSQL("INSERT INTO ProductType(Name) VALUES('Giảm cân, đốt mỡ')")
        db.execSQL("INSERT INTO ProductType(Name) VALUES('Vitamin, khoáng chất')")
        db.execSQL("INSERT INTO ProductType(Name) VALUES('Tăng hormone')")
        db.execSQL("INSERT INTO ProductType(Name) VALUES('Diet, Cooking')")
        db.execSQL("INSERT INTO ProductType(Name) VALUES('Phụ kiện')")
        db.execSQL("INSERT INTO ProductType(Name) VALUES('Yến mạch, Carb, Fiber')")
        db.execSQL("INSERT INTO ProductType(Name) VALUES('Đồ tập Gym')")
        // Product
        db.execSQL("INSERT INTO Product(Type,Name,Description, Price, Use) VALUES(1,'S.A.N Titanium Isolate Supreme 5Lbs (75 Servings)','Hydrolyzed Isolate Protein!\nZero Fat, Zero Sugar And Lactose Free!\nSupported Goal: Build Muscle\nMain Ingredient: Whey Protein',1850000,1)")
        db.execSQL("INSERT INTO Product(Type,Name,Description, Price, Use) VALUES(1,'BioTechUSA Hydro Whey Zero, 4 Lbs (1,816 Kg)','92% Whey Protein Hydrolyzed\n35G Protein\n15.2G EAAs\n7G BCAAs\n8G Glutamine',1900000,1)")
        db.execSQL("INSERT INTO Product(Type,Name,Description, Price, Use) VALUES(1,'Labrada Lean Body MRP 4.63 Lbs, 30 serving (2.1 KG)','35G Protein cao cấp\n6G BCAAs & Glutamine\n7G EFA-PLEX & FIBER PLEX\nGluten Free',1650000,1)")
        db.execSQL("INSERT INTO Product(Type,Name,Description, Price, Use) VALUES(2,'EliteLab Mass Muscle Gainer, 5 Lbs (2.3 kg)','1000 Calories\n60g Protein xây dựng phát triển cơ nạc\n174G Carb chất lượng cao\n5g Creatine Monohydradate\n5G BCAA\n100 mg Elite Enzyme',1090000,1)")
        db.execSQL("INSERT INTO Product(Type,Name,Description, Price, Use) VALUES(2,'ON Serious Mass 6 Lbs (2,72 kg)','Cung cấp 1250 Calo/liều dùng\n50G Protein chất lượng cao giúp tăng cơ bắp tốt hơn \n254G Carbohydrate giúp hấp thụ nhanh \nChứa 25 các loại Vitamin và Khoáng chất thiết yếu giúp tăng cân dễ dàng \nDùng đúng hướng dẫn chỉ định để tăng cân tốt hơn',750000,1)")
        db.execSQL("INSERT INTO Product(Type,Name,Description, Price, Use) VALUES(3,'Evogen EVP Xtreme, 24 Servings','Pre-Workout Powder For Maximum Pump And Focus\nFormula Includes Caffeine Anyhdrous, Choline Bitartrate And Piper Nigrum Extract',1400000,1)")
        db.execSQL("INSERT INTO Product(Type,Name,Description, Price, Use) VALUES(3,'GAT Sport Creatine Monohydrate Powder, 1000 Gram (200 Servings)','',750000,1)")
        db.execSQL("INSERT INTO Product(Type,Name,Description, Price, Use) VALUES(4,'Spring Valley Omega-3 Fish Oil Proactive Support, 1000 mg (645 EPA/310 DHA)','1000mg Omega-3\n645mg EPA \n310mg DHA \nHương chanh tự nhiên',300000,1)")
        db.execSQL("INSERT INTO Product(Type,Name,Description, Price, Use) VALUES(4,'NOW Omega-3, 200 Softgels','Screened for Purity\nOmega-3 Fatty Acids\nSupported Goal: Cardiovascular Health\nMain Ingredient: Omega-3',390000,1)")
        db.execSQL("INSERT INTO Product(Type,Name,Description, Price, Use) VALUES(5,'ProSupps HydroBCAA, 90 Servings','7G Bcaa\n3G EAA\n1G Citrulline\n0G SUGAR',1200000,1)")
        db.execSQL("INSERT INTO Product(Type,Name,Description, Price, Use) VALUES(5,'Cellucor Alpha Amino, 30 Servings','',550000,1)")
        db.execSQL("INSERT INTO Product(Type,Name,Description, Price, Use) VALUES(6,'Nutrabolics Semtex 60 Capsules, (30 lần dùng)','Tăng khả năng tập trung\nSinh nhiệt tối đa\nNgăn chặn thèm ăn hiệu quả\nHỗ trợ giảm kích thước tế bào mỡ\nCải thiện mức cholesterol',650000,1)")
        db.execSQL("INSERT INTO Product(Type,Name,Description, Price, Use) VALUES(6,'Nutrex Lipo-6 Black Stim-Free, 60 Black-Caps','Sử dụng linh hoạt không lo mất ngủ \nGiảm mỡ an toàn hiệu quả\nKhông chất kích thích',750000,1)")
        db.execSQL("INSERT INTO Product(Type,Name,Description, Price, Use) VALUES(7,'Applied Nutrition Multi-Vitamin Complex, 90 Tablets','',350000,1)")
        db.execSQL("INSERT INTO Product(Type,Name,Description, Price, Use) VALUES(7,'Bronson Vitamin K2 MK-7 + Vitamin D3, 250 Capsules','Bronson Vitamin K2 + D3 cung cấp đúng lượng Vitamin D3 và Vitamin K2 cần thiết để tối đa hóa lợi ích của chúng đối với xương và tim của bạn.',850000,1)")
        db.execSQL("INSERT INTO Product(Type,Name,Description, Price, Use) VALUES(8,'GAT Sport Testrol Gold ES, 60 Tablets','A GAT Sport Product\nFormulated With PrimaVie For Test Boost and Estrogen Support!\nHelp build lean tissue,body mass!\nSupports enhanced training performance\nContains PrimaVie for increased Testosterone',800000,1)")
        db.execSQL("INSERT INTO Product(Type,Name,Description, Price, Use) VALUES(8,'Universal Nutrition ZMA Pro™, 180 Capsules','High Quality ZMA for Optimal Testosterone Support\nSuggested to Increase Strength in Athletes and Increase Growth and Recovery',700000,1)")
        db.execSQL("INSERT INTO Product(Type,Name,Description, Price, Use) VALUES(9,'Quaker Simply Granola Oats, Honey, Raisins & Almonds 2Lbs (978 Gram)','',280000,1)")
        db.execSQL("INSERT INTO Product(Type,Name,Description, Price, Use) VALUES(10,'BlenderBottle ProStak 22oz (660 ml)','',400000,1)")
        db.execSQL("INSERT INTO Product(Type,Name,Description, Price, Use) VALUES(11,'Chất xơ dạng viên Now Psyllium Husk Caps, 500mg (500 Capsules)','Pure Bulk Fiber\nA Convenient Way To Increase Fiber And Maintain Regularity!\nSupported Goal: Digestive Health\nMain Ingredient: Fiber',560000,1)")
        db.execSQL("INSERT INTO Product(Type,Name,Description, Price, Use) VALUES(12,'Gymshark Legacy Drop Armhole Tank - Dark Grey','',190000,1)")
        // Promotion
        db.execSQL("INSERT INTO Promotion(Code,Amount,StartDate,EndDate) VALUES('','','1790-01-01 00:00:01','2789-12-31 23:59:59')")
        db.execSQL("INSERT INTO Promotion(Code,Amount,StartDate,EndDate) VALUES('GIAMNGAY20K',20000,'2020-10-10 00:00:01','2020-10-25 23:59:59')")
        db.execSQL("INSERT INTO Promotion(Code,Amount,StartDate,EndDate) VALUES('99K',99000,'2020-10-15 00:00:01','2020-10-30 23:59:00')")
        // Invoice
        db.execSQL("INSERT INTO Invoice(DateX,Code , SellerID, CustomerID, Status) VALUES('2020-10-13 15:03:01','GIAMNGAY20K',1,3,1)")
        db.execSQL("INSERT INTO Invoice(DateX,Code, SellerID, CustomerID, Status) VALUES('2020-09-22 13:02:15','GIAMNGAY20K',1,4,1)")
        db.execSQL("INSERT INTO Invoice(DateX,Code, SellerID, CustomerID, Status) VALUES('2020-10-15 07:22:13','GIAMNGAY20K',1,5,1)")
        db.execSQL("INSERT INTO Invoice(DateX,Code, SellerID, CustomerID, Status) VALUES('2020-07-22 20:15:49','GIAMNGAY20K',1,6,1)")
        db.execSQL("INSERT INTO Invoice(DateX,Code, SellerID, CustomerID, Status) VALUES('2020-04-11 13:08:02','GIAMNGAY20K',1,7,1)")
        db.execSQL("INSERT INTO Invoice(DateX,Code, SellerID, CustomerID, Status) VALUES('2020-12-13 08:33:10','GIAMNGAY20K',1,8,1)")
        db.execSQL("INSERT INTO Invoice(DateX,Code, SellerID, CustomerID, Status) VALUES('2020-10-13 13:22:11','GIAMNGAY20K',1,9,1)")
        db.execSQL("INSERT INTO Invoice(DateX,Code, SellerID, CustomerID, Status) VALUES('2020-11-22 11:16:22','GIAMNGAY20K',1,10,1)")

        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(1,1,1)")
        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(1,6,1)")
        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(1,7,1)")

        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(2,13,1)")
        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(2,11,1)")
        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(2,2,1)")

        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(3,7,1)")
        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(3,4,1)")
        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(3,1,1)")

        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(4,11,1)")
        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(4,15,1)")
        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(4,20,1)")

        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(5,12,1)")
        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(5,6,1)")
        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(5,1,1)")

        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(6,3,1)")
        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(6,2,1)")
        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(6,10,1)")

        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(7,7,1)")
        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(7,6,1)")
        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(7,5,1)")

        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(8,4,1)")
        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(8,17,1)")
        db.execSQL("INSERT INTO InvoiceDetail(InvoiceID,ProductID,Amount) VALUES(8,13,1)")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db!!.execSQL("DROP TABLE IF EXISTS ACCOUNT")
        db.execSQL("DROP TABLE IF EXISTS USER")
        db.execSQL("DROP TABLE IF EXISTS USERTYPE")
        db.execSQL("DROP TABLE IF EXISTS PRODUCT")
        db.execSQL("DROP TABLE IF EXISTS PRODUCTTYPE")
        db.execSQL("DROP TABLE IF EXISTS INVOICE")
        db.execSQL("DROP TABLE IF EXISTS INVOICEDETAIL")
        db.execSQL("DROP TABLE IF EXISTS PROMOTION")
    }

}