package com.example.ad

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.Utils
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.*
import kotlinx.coroutines.suspendCancellableCoroutine


class AdmobBan : AdParent<AdView> {
    override val type: String = "ban"

    override suspend fun loadAd(adId: String, context: Context, after: () -> Unit): AdView? =
        suspendCancellableCoroutine {
            val extras = Bundle()
            extras.putString("collapsible", "top")
            val adView = AdView(context)
            adView.setAdSize(getAdSize(context))
            adView.adUnitId = adId
            val adRequest =
                AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
                    .build()
            adView.loadAd(adRequest)
            adView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    it.resume(adView) {}
                    Log.d(
                        "AdFactory",
                        "拉取横幅成功,id:${adId}"
                    )
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    it.resume(null) {}
                    Log.d(
                        "AdFactory",
                        "拉取横幅失败,id:${adId} ${p0.message}"
                    )
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    Log.d(
                        "AdFactory",
                        "横幅点击"
                    )
                }
            }
        }

    override suspend fun showAd(
        ad: Any,
        context: Activity,
        adContainer: ViewGroup?,
        after: () -> Unit
    ) {
        if (ad is AdView) {
            if (ad.parent != null) {
                (ad.parent as ViewGroup).removeView(ad) // <- fix
            }
            adContainer?.addView(ad)
            Log.d(
                "AdFactory",
                "展示banner成功 "
            )
            after()
        }
    }

    private fun getAdSize(context: Context): AdSize {
        val display = Utils.getApp().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        display.defaultDisplay.getMetrics(outMetrics)
        val density = outMetrics.density
        val adWidthPixels = outMetrics.widthPixels.toFloat() - SizeUtils.dp2px(40f)
        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getInlineAdaptiveBannerAdSize(adWidth, 51)
    }
}