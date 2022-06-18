package com.fstudio.topnews.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fstudio.topnews.NewsWebView
import com.fstudio.topnews.R
import com.fstudio.topnews.databinding.AdLayoutBinding
import com.fstudio.topnews.model.NewsModel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import java.text.SimpleDateFormat
import java.util.*


class NewsAdapter(var context: Context, modelClassArrayList: ArrayList<NewsModel.Article>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var modelClassArrayList: ArrayList<NewsModel.Article>

    private val ITEM_VIEW = 0
    private val AD_VIEW = 1
    private val ITEM_FEED_COUNT = 8


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_VIEW){
            val view: View = LayoutInflater.from(context).inflate(R.layout.news_item_layout, parent, false)
            return MyViewHolder(view)
        }else if (viewType == AD_VIEW){
            val view: View = LayoutInflater.from(context).inflate(R.layout.ad_layout, parent, false)
            return AdviewHolder(view)
        }else{
            val view: View = LayoutInflater.from(context).inflate(R.layout.news_item_layout, parent, false)
            return MyViewHolder(view)
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.itemViewType == ITEM_VIEW) {
            val pos = (position - Math.round((position / ITEM_FEED_COUNT).toDouble())).toInt()
            holder as MyViewHolder
            holder.cardView.setOnClickListener {
                val intent = Intent(context, NewsWebView::class.java)
                intent.putExtra("url", modelClassArrayList[pos].url)
                context.startActivity(intent)
                /*val url: String = modelClassArrayList[position].url
            val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
            val customTabsIntent: CustomTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, Uri.parse(url))*/
            }
            val stockNewsDate = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val date: Date = modelClassArrayList[pos].publishedAt
            val dateTime = stockNewsDate.format(date)
            holder.time.text = dateTime
            holder.mainHeading.text = modelClassArrayList[pos].title
            holder.discription.text = modelClassArrayList[pos].description
            Glide.with(context).load(modelClassArrayList[pos].urlToImage)
                .into(holder.imageView)


        }else if (holder.itemViewType == AD_VIEW){
            (holder as AdviewHolder).adBinding()
        }

    }
    override fun getItemViewType(position: Int): Int {
        return if ((position + 1) % ITEM_FEED_COUNT === 0) {
            AD_VIEW
        } else ITEM_VIEW
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var cardView: CardView
        var mainHeading: TextView
        var discription: TextView
        var time: TextView

        init {
            imageView = itemView.findViewById(R.id.imageViewNews)
            cardView = itemView.findViewById(R.id.cardItem)
            mainHeading = itemView.findViewById(R.id.mainHeading)
            discription = itemView.findViewById(R.id.discription)
            // author = itemView.findViewById(R.id.author);
            time = itemView.findViewById(R.id.time)
        }

    }

    init {
        this.modelClassArrayList = modelClassArrayList
    }

    override fun getItemCount(): Int {
        if (modelClassArrayList.size > 0) {
            return (modelClassArrayList.size + Math.round(((modelClassArrayList.size / ITEM_FEED_COUNT).toDouble()))).toInt()

        }
        return modelClassArrayList.size
    }

    inner class AdviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
       var bd : AdLayoutBinding
        init {
            bd = AdLayoutBinding.bind(itemView)
        }
        fun adBinding(){

            val adLoader = AdLoader.Builder(context, context.getString(R.string.nativeAd))
                .forNativeAd { ad : NativeAd ->
                    val nativeAd : NativeAdView =
                        LayoutInflater.from(context).inflate(R.layout.native_ad_design, null) as NativeAdView
                    populateNativeAd(ad,nativeAd)
                    bd.nativeAdLayout.removeAllViews()
                    bd.nativeAdLayout.addView(nativeAd)
                    // Show the ad.
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(
                    NativeAdOptions.Builder()
                    // Methods in the NativeAdOptions.Builder class can be
                    // used here to specify individual options settings.
                    .build())
                .build()
            adLoader.loadAds(AdRequest.Builder().build(), 2)
        }
    }

    private fun populateNativeAd(nativeAd: NativeAd, adView: NativeAdView) {
// Set the media view.
        // Set the media view.
        adView.mediaView = adView.findViewById(R.id.ad_media)

        // Set other ad assets.

        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline
        adView.mediaView?.setMediaContent(nativeAd.mediaContent!!)

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            with(adView) { bodyView?.setVisibility(View.INVISIBLE) }
        } else {
            adView.bodyView?.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView()?.setVisibility(View.INVISIBLE)
        } else {
            adView.getCallToActionView()?.setVisibility(View.VISIBLE)
            (adView.getCallToActionView() as Button).setText(nativeAd.getCallToAction())
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView()?.setVisibility(View.GONE)
        } else {
            (adView.getIconView() as ImageView).setImageDrawable(
                nativeAd.getIcon()!!.getDrawable()
            )
            (adView.getIconView() as ImageView).setVisibility(View.VISIBLE)
        }

        if (nativeAd.price == null) {
            with(adView) { priceView?.setVisibility(View.INVISIBLE) }
        } else {
            adView.getPriceView()?.setVisibility(View.VISIBLE)
            (adView.getPriceView() as TextView).setText(nativeAd.getPrice())
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView()?.setVisibility(View.INVISIBLE)
        } else {
            adView.getStoreView()?.setVisibility(View.VISIBLE)
            (adView.getStoreView() as TextView).setText(nativeAd.getStore())
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView()?.setVisibility(View.INVISIBLE)
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            (adView.getStarRatingView() as RatingBar).setVisibility(View.VISIBLE)
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView()?.setVisibility(View.INVISIBLE)
        } else {
            (adView.getAdvertiserView() as TextView).setText(nativeAd.getAdvertiser())
            (adView.getAdvertiserView() as TextView).setVisibility(View.VISIBLE)
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)
    }

}