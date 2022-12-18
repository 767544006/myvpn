package com.example.ad

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import kotlinx.coroutines.suspendCancellableCoroutine

class AdmobOpen : AdParent<AppOpenAd> {
    override val type: String = "start"

    override suspend fun loadAd(adId: String, context: Context, after: () -> Unit): AppOpenAd? =
        suspendCancellableCoroutine {
            AppOpenAd.load(
                context,
                adId,
                AdRequest.Builder().build(),
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                object : AppOpenAd.AppOpenAdLoadCallback() {
                    override fun onAdLoaded(p0: AppOpenAd) {
                        super.onAdLoaded(p0)
                        it.resume(p0) {}
                        Log.d(
                            "AdFactory",
                            "拉取开屏成功,id:${adId}"
                        )
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        it.resume(null) {}
                        Log.d(
                            "AdFactory",
                            "拉取开屏失败,id:${adId}"
                        )
                    }
                })
        }

    override suspend fun showAd(
        ad: Any,
        context: Activity,
        adContainer: ViewGroup?,
        after: () -> Unit
    ) {
        if (ad is AppOpenAd) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdImpression() {
                    super.onAdImpression()
                    Log.d(
                        "AdFactory",
                        "开屏广告展示 ${ad.adUnitId}"
                    )
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    Log.d(
                        "AdFactory",
                        "开屏广告关闭 ${ad.adUnitId}"
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