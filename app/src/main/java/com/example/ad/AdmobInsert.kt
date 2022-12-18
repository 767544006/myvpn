package com.example.m156.ad

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import com.example.ad.AdParent
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

class AdmobInsert : AdParent<InterstitialAd> {
    override val type: String = "int"

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun loadAd(
        adId: String,
        context: Context,
        after: () -> Unit
    ): InterstitialAd? = suspendCancellableCoroutine { post ->
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, adId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(p0: InterstitialAd) {
                super.onAdLoaded(p0)
                Log.d(
                    "AdFactory",
                    "拉取插页成功,id:${adId}"
                )
                post.resume(p0) {}
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                Log.d(
                    "AdFactory",
                    "拉取插页失败,id:${adId} 原因 ${p0.message}"
                )
                post.resume(null) {}
            }
        })
    }

    override suspend fun showAd(
        ad: Any,
        context: Activity,
        adContainer: ViewGroup?,
        after: () -> Unit
    ) {
        if (ad is InterstitialAd) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdImpression() {
                    super.onAdImpression()
                    Log.d(
                        "AdFactory",
                        "插页广告展示 ${ad.adUnitId}"
                    )
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    Log.d(
                        "AdFactory",
                        "插页广告关闭 ${ad.adUnitId}"
                    )
                    after()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    super.onAdFailedToShowFullScreenContent(p0)
                    after()

                }
            }
            ad.show(context)
        }
    }
}