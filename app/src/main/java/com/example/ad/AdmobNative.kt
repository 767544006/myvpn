package com.example.ad

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPStaticUtils
import com.example.vpn.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.suspendCancellableCoroutine

class AdmobNative : AdParent<NativeAd> {
    override val type: String = "nav"

    override suspend fun loadAd(adId: String, context: Context, after: () -> Unit): NativeAd? =
        suspendCancellableCoroutine { post ->
            val adLoader = AdLoader.Builder(
                context,
                adId
            ).forNativeAd {
                post.resume(it) {}
                Log.d(
                    "AdFactory",
                    "拉取原生成功,id:${adId}"
                )
            }.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    post.resume(null) {}
                    Log.d(
                        "AdFactory",
                        "拉取原生失败,id:${adId} 原因 ${p0.message}"
                    )
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    SPStaticUtils.put("todayTimes", SPStaticUtils.getInt("todayTimes", 0) + 1)
                    LogUtils.d("todayTimes", SPStaticUtils.getInt("todayTimes", 0))
                }
            })

            adLoader.build().loadAd(AdRequest.Builder().build())
        }



    override suspend fun showAd(
        ad: Any,
        context: Activity,
        adContainer: ViewGroup?,
        after: () -> Unit
    ) {
        if (ad is NativeAd) {
            Log.d("AdFactory", "开始展示原生")
            val adview = LayoutInflater.from(context).inflate(
                R.layout.admob_native,
                null
            ) as NativeAdView
            adContainer?.removeAllViews()
            adContainer?.addView(adview)
            Log.d(
                "AdFactory",
                "展示原生成功 "
            )
            after()
            bindAdView(ad, adview)
        }
    }

    fun bindAdView(nativeAd: NativeAd, adView: NativeAdView) {
        adView.mediaView = adView.findViewById<MediaView>(R.id.media_view_container)
        adView.mediaView?.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
        adView.headlineView = adView.findViewById(R.id.title_text_view)
        adView.bodyView = adView.findViewById(R.id.body_text_view)
        adView.callToActionView = adView.findViewById(R.id.cta_button)
        adView.iconView = adView.findViewById(R.id.icon_image_view)
        (adView.headlineView as TextView).text = nativeAd.headline
        nativeAd.mediaContent?.let { adView.mediaView?.setMediaContent(it) }
        if (nativeAd.body == null) {
            adView.bodyView?.visibility = View.INVISIBLE
        } else {
            adView.bodyView?.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }
        if (nativeAd.callToAction == null) {
            adView.callToActionView?.visibility = View.INVISIBLE
        } else {
            adView.callToActionView?.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }
        if (nativeAd.icon == null) {
            adView.iconView?.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon?.drawable
            )
            adView.iconView?.visibility = View.VISIBLE
        }
        adView.setNativeAd(nativeAd)
    }
}
