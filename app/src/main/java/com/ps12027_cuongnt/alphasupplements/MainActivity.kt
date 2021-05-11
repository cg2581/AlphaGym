package com.ps12027_cuongnt.alphasupplements

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.ps12027_cuongnt.alphasupplements.fragment.LoginFragment
import com.ps12027_cuongnt.alphasupplements.fragment.customer.CustomerFragment
import com.ps12027_cuongnt.alphasupplements.fragment.employee.EmployeeFragment
import com.ps12027_cuongnt.alphasupplements.fragment.manage.CartManageFragment
import com.ps12027_cuongnt.alphasupplements.fragment.manage.ChangePasswordFragment
import com.ps12027_cuongnt.alphasupplements.fragment.manage.ProductManageFragment
import com.ps12027_cuongnt.alphasupplements.fragment.manage.StatisticFragment
import com.ps12027_cuongnt.alphasupplements.fragment.promotion.PromotionFragment
import com.ps12027_cuongnt.alphasupplements.fragment.user.UserSettingsFragment
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var currentUserID: Int = 0
    var currentUserType: Int? = null
    lateinit var drawerLayout: DrawerLayout
    var navigationView: NavigationView? = null
    var currentInvoiceID: Int? = null
    var currentMenu: Int = R.menu.menu_blank
    var imgByteArray: ByteArray? = null

    var permissions: Array<String>? = null
    var request_code: Int? = null


    override

    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        toolbar = findViewById(R.id.toolbar)




        setSupportActionBar(toolbar)
        toolbar!!.setTitleTextColor(Color.BLUE)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar!!.setNavigationOnClickListener {
            if (getFragmentCount() != 0) {
                super.onBackPressed()
            } else {
                // Chua xu li nut
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        navigationView!!.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.promotion -> {
                    switchFragment(PromotionFragment(), "Replace", "Promotion")
                }
                R.id.product -> {
                    switchFragment(ProductManageFragment(), "Replace", "ProductManage")
                }
                R.id.invoice -> {
                    switchFragment(CartManageFragment(), "Replace", "CartManage")
                }
                R.id.customer -> {
                    switchFragment(CustomerFragment(), "Replace", "Customer")
                }
                R.id.statistic -> {
                    switchFragment(StatisticFragment(), "Replace", "Statistic")
                }
                R.id.employee -> {
                    switchFragment(EmployeeFragment(), "Replace", "Employee")
                }
                R.id.logout -> {
                    val prefAccount =
                        this.getSharedPreferences("SaveAccount", Context.MODE_PRIVATE)
                    val editorAccount = prefAccount!!.edit()
                    editorAccount.putBoolean("logged", false)
                    editorAccount.apply()
                    clearFragmentBackStack()
                    switchFragment(LoginFragment(), "ReplaceN", "Login")
                }
            }

            true
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, LoginFragment())
            .disallowAddToBackStack()
            .commit()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(currentMenu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Main
            R.id.changePassword -> {
                switchFragment(ChangePasswordFragment(), "Replace", "ChangePassword")
            }
            R.id.logout -> {
                val prefAccount =
                    this.getSharedPreferences("SaveAccount", Context.MODE_PRIVATE)
                val editorAccount = prefAccount!!.edit()
                editorAccount.putBoolean("logged", false)
                editorAccount.apply()
                clearFragmentBackStack()
                switchFragment(LoginFragment(), "ReplaceN", "Login")
            }
            R.id.userSetting -> {
                switchFragment(UserSettingsFragment(), "Replace", "UserSettings")
            }

        }
        return true
    }


    override fun onBackPressed() {
        imgByteArray = null
        if (getFragmentCount() == 0) {
            super.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }


    fun showUI() {

        //changeToolbarColor("White")

        supportActionBar!!.show()
        toolbar!!.visibility = View.VISIBLE
    }

    fun hideUI() {
        supportActionBar!!.hide()
        toolbar!!.visibility = View.GONE
    }


    fun switchFragment(fragment: Fragment, type: String, tag: String) {
        //
        val frm = supportFragmentManager
        when (type) {
            "Replace" -> {
                frm.beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .setCustomAnimations(R.anim.left, R.anim.right)
                    .addToBackStack(tag)
                    .commit()
            }
            "ReplaceN" -> {
                frm.beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .setCustomAnimations(R.anim.left, R.anim.right)
                    .commit()

            }
        }
        hideKeyboard()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
/*        drawerLayout.closeDrawer(GravityCompat.START)
        navigationView!!.clearFocus()
        drawerLayout.clearFocus()*/


    }

    fun back() {
        super.onBackPressed()
    }

    fun getFragmentCount(): Int {
        return supportFragmentManager.backStackEntryCount
    }

    fun clearFragmentBackStack() {
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
    }


    fun hideKeyboard() {
        val imm: InputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = this.currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun changeToolbarTitle(title: String) {
        toolbar!!.findViewById<TextView>(R.id.toolbar_title)!!.text = title
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == request_code) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, request_code!!)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    permissions, request_code!!
                )
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == request_code && resultCode == RESULT_OK && data != null) {
            val uri = data.data
            try {
                val inputStream: InputStream? = this!!.contentResolver.openInputStream(uri!!)
                var bitmap = BitmapFactory.decodeStream(inputStream)
                bitmap = getResizedBitmap(bitmap, 1024)
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()
                imgByteArray = byteArray
                Log.d("ASd", "onActivityResult: $imgByteArray")

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun openActionPicker(perm: Array<String>, code: Int) {
        permissions = perm
        request_code = code
        ActivityCompat.requestPermissions(this, permissions!!, request_code!!)
    }

    fun imageViewToByte(image: ImageView?): ByteArray {
        var bitmap = (image!!.drawable as BitmapDrawable).bitmap
        bitmap = getResizedBitmap(bitmap, 1024)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun getResizedBitmap(bitmap: Bitmap, maxSize: Int): Bitmap? {

        var width = bitmap.width
        var height = bitmap.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }


    fun randomAnim(): Int {
        val num = (1..2).random()
        if (num == 1) {
            return R.anim.left
        } else {
            return R.anim.right
        }
    }

}
