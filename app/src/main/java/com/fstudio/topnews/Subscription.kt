package com.fstudio.topnews

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.BillingProcessor.IBillingHandler
import com.anjlab.android.iab.v3.PurchaseInfo


class Subscription : AppCompatActivity(), IBillingHandler {
    private lateinit var buyNow: AppCompatButton
    private lateinit var bp: BillingProcessor
    private lateinit var purchaseInfo: PurchaseInfo
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var premiumUserName: TextView
    private lateinit var backArrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premium)

        backArrow = findViewById(R.id.back_arrow)
        backArrow.setOnClickListener{
            finish()
        }

        preferences = getSharedPreferences("subs", MODE_PRIVATE)
        editor = preferences.edit()


        buyNow = findViewById(R.id.MonthlyPrice)
        premiumUserName = findViewById(R.id.premiumUserName)

        editor = preferences.edit()

        if (!preferences.getBoolean("isPremium", false)) {
         //   Toast.makeText(this, "Show ads", Toast.LENGTH_SHORT).show()
            premiumUserName.setText("Not a Premium User")
        } else {
            premiumUserName.setText("Premium User")
        }


        bp = BillingProcessor(
            this,
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAo2G7mRNmbpLDQARdDLAQHBUSst47bKwPTI4pfGfYqr7/arQrWqakQffKYjaek3YHQ7IR2xmc/xiLi7dkYl8eeTGHTyIhcPW5f9Vt3P3TVQ9T4wu77c9wdTbsUmY9XcJYLFuBTASAyg0k9Hrz6SXxSixMUyNvSEU3sM2EXX0pb5lYwBUCyXItVlUY3ChZIBbceqZbGzhe1l5LOZcZEEgk/7XTL0ovodr4GZ23j/XwijJpUFYMJAsge73YaKS4VkV59p5z1Qne9Fh6lSyyFjz+jSQv3EcqaK6D2XoywFMcxV7Ano2CDQPPF/tpfbcV3wmFJf325itO2dgtAjWs1OPCUwIDAQAB",
            this
        )
        bp.initialize()

        buyNow.setOnClickListener{
            bp.subscribe(this, "sub_premium")
        }
    }

    override fun onProductPurchased(productId: String, details: PurchaseInfo?) {
        editor.putBoolean("isPremium", true)
        editor.apply()
        Toast.makeText(this, "Successfull", Toast.LENGTH_SHORT).show()
    }

    override fun onPurchaseHistoryRestored() {
//        TODO("Not yet implemented")
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
//        TODO("Not yet implemented")
    }

    override fun onBillingInitialized() {
//        TODO("Not yet implemented")

        bp.loadOwnedPurchasesFromGoogleAsync(object : BillingProcessor.IPurchasesResponseListener {
            override fun onPurchasesSuccess() {
//                TODO("Not yet implemented")
            }

            override fun onPurchasesError() {
//                TODO("Not yet implemented")
            }

        })

        if (bp.getSubscriptionPurchaseInfo("sub_premium") != null) {

            purchaseInfo = bp.getSubscriptionPurchaseInfo("sub_premium")!!


            if (purchaseInfo != null) {
                if (purchaseInfo.purchaseData.autoRenewing) {
                    editor.putBoolean("isPremium", true)
                    editor.apply()
                    Toast.makeText(this, "Already subscribe", Toast.LENGTH_SHORT).show()
                } else {
                    editor.putBoolean("isPremium", false)
                    editor.apply()
                    Toast.makeText(this, "Not subscribed", Toast.LENGTH_SHORT).show()
                }
            } else {

                editor.putBoolean("isPremium", false)
                editor.apply()
                Toast.makeText(this, "Expired", Toast.LENGTH_SHORT).show()
            }
        }
    }

        override fun onDestroy() {
            if (bp != null) {
                bp.release()
            }
            super.onDestroy()
        }

}
