package com.fstudio.topnews

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.fstudio.topnews.adapter.ViewPagerAdapter
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private var viewPagerAdapter: ViewPagerAdapter? = null
    private lateinit var tabLayout: TabLayout
    private lateinit var tvp: ViewPager2
  //  var bottomNavigationView: BottomNavigationView? = null
  private var toolbar: Toolbar? = null
    private lateinit var countryName: SharedPreferences


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null
        drawerLayout = findViewById(R.id.drawerLayout)
        toggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open,R.string.Close)
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_all_documents -> {
                   // Toast.makeText(this, "working perfectly", Toast.LENGTH_SHORT).show()
                    tvp.currentItem = 0
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
                }
                R.id.vip ->{
                    startActivity(Intent(this,Subscription::class.java))
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
                }
                R.id.nav_rate -> {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)))
                }
                R.id.nav_share->{
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }

                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Check Out This Cool App")
                    intent.putExtra(
                        Intent.EXTRA_TEXT, """https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                    """.trimIndent()
                    )
                    startActivity(intent)
                }
                R.id.nav_about->{
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    dialogAboutApp()
                }
            }
            true
        }



        tabLayout = findViewById(R.id.tabLayout)
        tvp = findViewById(R.id.viewpagertab)
        viewPagerAdapter = ViewPagerAdapter(this)
        countryName = getSharedPreferences("country", MODE_PRIVATE)
        COUNTRY_NAME = countryName.getString("country", "in")
        tvp.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout,tvp) { tab: TabLayout.Tab, position: Int ->
            tab.text = viewPagerAdapter!!.title[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.news_manu, menu)
        when (COUNTRY_NAME) {
            "in" -> menu.getItem(1).icon = ContextCompat.getDrawable(this, R.drawable.india)
            "us" -> menu.getItem(1).icon = ContextCompat.getDrawable(this, R.drawable.united_state)
            "au" -> menu.getItem(1).icon = ContextCompat.getDrawable(this, R.drawable.australia)
            "ru" -> menu.getItem(1).icon = ContextCompat.getDrawable(this, R.drawable.russia)
            "fr" -> menu.getItem(1).icon = ContextCompat.getDrawable(this, R.drawable.france)
            "gb" -> menu.getItem(1).icon =
                ContextCompat.getDrawable(this, R.drawable.united_kingdom)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.moreapps -> startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/developer?id=F-Studio.")
                )
            )
            R.id.blockAds ->startActivity(
                Intent(this,Subscription::class.java)
            )
            R.id.shere -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check Out This Cool App")
                intent.putExtra(
                    Intent.EXTRA_TEXT, """https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                      """.trimIndent()
                )
                startActivity(intent)
            }
            R.id.exit -> systemExit()
            R.id.rate -> {
                //   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
                countryPic()
            }

        }
        return super.onOptionsItemSelected(item)
    }
    private fun dialogAboutApp() {
         var dialog: Dialog? = null
         dialog= Dialog(this@MainActivity)
         dialog.setContentView(R.layout.popup_about_app_lay)
         dialog.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )

        val appVersion: TextView = dialog.findViewById(R.id.appVersion)
        val verName = BuildConfig.VERSION_NAME
        appVersion.text = verName
        dialog.show()
    }

    private fun systemExit() {
        finish()
        finishAffinity()
        exitProcess(0)
    }


    private fun countryPic() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.country_layout)
        dialog.setCancelable(true)
        val window = dialog.window
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setLayout(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
        val cIndia = dialog.findViewById<ConstraintLayout>(R.id.cIndia)
        cIndia.setOnClickListener {
            val editor = countryName.edit()
            editor.putString("country", "in")
            editor.apply()
            dialog.dismiss()
            val intent = intent
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        val cUsa = dialog.findViewById<ConstraintLayout>(R.id.cUsa)
        cUsa.setOnClickListener {
            val editor = countryName.edit()
            editor.putString("country", "us")
            editor.apply()
            dialog.dismiss()
            val intent = intent
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        val cAustralia = dialog.findViewById<ConstraintLayout>(R.id.cAustralia)
        cAustralia.setOnClickListener {
            val editor = countryName.edit()
            editor.putString("country", "au")
            editor.apply()
            dialog.dismiss()
            val intent = intent
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        val cRussia = dialog.findViewById<ConstraintLayout>(R.id.cRussia)
        cRussia.setOnClickListener {
            val editor = countryName.edit()
            editor.putString("country", "ru")
            editor.apply()
            dialog.dismiss()
            val intent = intent
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        val cFrance = dialog.findViewById<ConstraintLayout>(R.id.cFrance)
        cFrance.setOnClickListener {
            val editor = countryName.edit()
            editor.putString("country", "fr")
            editor.apply()
            dialog.dismiss()
            val intent = intent
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        val cUk = dialog.findViewById<ConstraintLayout>(R.id.cUK)
        cUk.setOnClickListener {
            val editor = countryName.edit()
            editor.putString("country", "gb")
            editor.apply()
            dialog.dismiss()
            val intent = intent
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }

    companion object {
       // var newsApiKey = "cd7e01d048942a607555ba8b07c4232d"
        var COUNTRY_NAME: String? = null
    }
}