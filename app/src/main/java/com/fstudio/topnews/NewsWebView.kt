package com.fstudio.topnews

import androidx.appcompat.app.AppCompatActivity
import android.webkit.WebView
import android.os.Bundle
import com.fstudio.topnews.R
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebViewClient

class NewsWebView : AppCompatActivity() {
    private lateinit var webView1: WebView
    var url: String? = null

    // private androidx.appcompat.widget.Toolbar toolbar;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_web_view)
        webView1 = findViewById(R.id.webViewNews)
        // toolbar = findViewById(R.id.toolbar3);
        // setSupportActionBar(toolbar);
        val intent = intent
        url = intent.getStringExtra("url")
        webView1.webViewClient = WebViewClient()
        webView1.loadUrl(url!!)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.webview_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shereNews -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, url)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}