package com.example.aop_part3_chapter02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.example.aop_part3_chapter02.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {

    private lateinit var view:ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityMainBinding.inflate(layoutInflater)
        setContentView(view.root)

        initViews()
        initData()
    }


    private fun initViews(){

        view.viewPager.setPageTransformer { page, position ->
            when{
                position.absoluteValue >= 1F -> page.alpha = 0F
                position == 0F -> page.alpha = 1F
                else -> page.alpha = 1F - 2*position.absoluteValue
            }

        }

    }

    private fun initData(){
        val remoteConfig = Firebase.remoteConfig

        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
        )

        remoteConfig.fetchAndActivate().addOnCompleteListener {
            view.progressBar.isVisible = false

            if(it.isSuccessful){
                val quotes = parseQuotesJson(remoteConfig.getString("quotes"))
                val isNameRevealed = remoteConfig.getBoolean("is_named_revealed")

                displayQuotesPager(quotes,isNameRevealed)


            }

        }
    }


    private fun displayQuotesPager(quotes: List<Quote>,isNameRevealed: Boolean){

        val count = QuotesPagerAdapter(quotes,isNameRevealed).itemCount
        view.viewPager.adapter = QuotesPagerAdapter(quotes,isNameRevealed)

        view.viewPager.setCurrentItem(count / 2,false)
    }

    private fun parseQuotesJson(json:String):List<Quote>{

        val jsonArray = JSONArray(json)

        var jsonList = emptyList<JSONObject>()

        for(idx in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(idx)

            jsonObject?.let {
                jsonList = jsonList+it
            }

        }

        return jsonList.map {
            Quote(it.getString("quote"),it.getString("name"))
        }
    }

}