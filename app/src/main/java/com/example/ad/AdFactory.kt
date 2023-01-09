package com.example.ad

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import com.example.m156.ad.AdmobInsert
import kotlinx.coroutines.*

object AdFactory {
    private val ids = mutableListOf(
        AdBean("ca-app-pub-3940256099942544/3419835294", "start", "start"),
        AdBean("ca-app-pub-3940256099942544/1033173712", "finish", "int"),
        AdBean("ca-app-pub-3940256099942544/2247696110", "report", "nav"),
    )
    val result = mutableListOf<AdResult>()
    private val loadingPlace= mutableListOf<String>()
    private val adapterList = listOf(
        AdmobOpen(), AdmobInsert(), AdmobBan(), AdmobNative()
    ).associateBy {
        it.type
    }

    private fun produceAdParent(type: String): AdParent<*>? {
        return adapterList[type]
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun loadAll(context: Context) = withContext(Dispatchers.Main) {
        ids.forEach {
            if (!checkIfNeedLoad(it.loc)) return@forEach
            Log.d("AdFactory","开始拉${it}")
            launch {
                loadingPlace.add(it.loc)
                val job = async {
                    val ad = produceAdParent(it.type)?.loadAd(it.id, context)
                    if (ad != null) return@async ad else return@async null
                }
                job.join()
                val ad = job.getCompleted()
                loadingPlace.remove(it.loc)
                if (job.isCompleted && ad != null) {
                    result.add(AdResult(it.id, it.loc, it.type, ad))
                }
            }
        }
    }
    suspend fun show(context: Activity,type:String,place:String,viewGroup: ViewGroup?,after:()->Unit={}){
        try {
            val  thisType=result.filter { it.type==type }
            if (thisType.isEmpty()){
                after()
                return
            }
            thisType.sortedBy {
                it.loc==place
            }.first().apply {
                produceAdParent(type)?.showAd(ad,context,viewGroup,after)
                result.remove(this)
                loadAll(context)
            }
        }catch (e:Exception){
            after()
            e.printStackTrace()
        }

    }
    private fun checkIfNeedLoad(loc: String):Boolean{
        return result.find { it.loc==loc }==null&& loadingPlace.find { it==loc }==null
    }
}

data class AdBean(val id: String, val loc: String, val type: String)
data class AdResult(val id: String, val loc: String, val type: String, val ad: Any){

}
