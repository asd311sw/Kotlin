package com.example.myapplication

import android.graphics.Bitmap
import android.net.wifi.WifiEnterpriseConfig
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.ContentLoadingProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var view:ActivityMainBinding


    private lateinit var addressBar:EditText
    private lateinit var progressBar:ContentLoadingProgressBar
    private lateinit var refrashLayout:SwipeRefreshLayout
    private lateinit var goBackButton:ImageButton
    private lateinit var goForwardButton:ImageButton
    private lateinit var webView:WebView


    companion object{
        const val DEFAULT_URL = "https://www.google.com"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityMainBinding.inflate(layoutInflater)
        setContentView(view.root)

        addressBar = view.addressBar
        progressBar = view.progresBar
        refrashLayout = view.refreshLayout
        goBackButton = view.goBackButton
        goForwardButton = view.goForwardButton
        webView = view.webView

        initViews()
        bindViews()
    }

    private fun initViews(){

        view.webView.apply {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true
            loadUrl(DEFAULT_URL)
        }

    }


    override fun onBackPressed() {

        if (view.webView.canGoBack())
            view.webView.goBack()
        else
            super.onBackPressed()
    }


    private fun bindViews() {

        view.addressBar.setOnEditorActionListener { textView, id, keyEvent ->

            if(id == EditorInfo.IME_ACTION_DONE) {
                val loadUrl = textView.text.toString()
                
                if(URLUtil.isNetworkUrl(loadUrl))
                    view.webView.loadUrl(loadUrl)
                else
                    view.webView.loadUrl("https://$loadUrl")

            }

            return@setOnEditorActionListener false
        }


        view.goBackButton.setOnClickListener {


            view.webView.goBack()

        }

        view.goForwardButton.setOnClickListener {

            view.webView.goForward()
        }


        view.goHomeButton.setOnClickListener {


            view.webView.loadUrl(DEFAULT_URL)
        }


        view.refreshLayout.setOnRefreshListener {
            view.webView.reload()
        }


    }

    inner class WebViewClient:android.webkit.WebViewClient(){
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)


            refrashLayout.isRefreshing = false
            progressBar.hide()
            goBackButton.isEnabled = webView.canGoBack()
            goForwardButton.isEnabled = webView.canGoForward()
            addressBar.setText(url)
        }


        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)

            progressBar.show()
        }

    }




    inner class WebChromeClient:android.webkit.WebChromeClient(){

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)

            progressBar.progress = newProgress

        }


    }



}