package com.example.ad

import android.app.Activity
import android.content.Context
import android.view.ViewGroup

interface AdParent<AD> {
    val type: String
    suspend fun loadAd(adId: String, context: Context, after: () -> Unit = {}): AD?
    suspend fun showAd(
        ad: Any,
        context: Activity,
        adContainer: ViewGroup? = null,
        after: () -> Unit = {}
    )
}