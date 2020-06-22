package com.sdz.smartsdk

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView


abstract class SmartBaseView(context: Context, attributeSet: AttributeSet) :
    WebView(context, attributeSet) {

    var queryCOID: String? = null
        protected set
    var prelandCOID: String? = null
        protected set

    protected var needsToReload = false

    protected var onPageFinishedCall: (String?) -> Unit = {}

    init {
        setSettings()
        setAcceptThirdPartyCookies()
    }

    fun applyJS(code: String) {
        loadUrl("javascript: $code")
    }

    fun executeJSWithResult(code: String, resultCall: (String) -> Unit) {
        evaluateJavascript(code) {
            resultCall(it)
        }
    }

    fun getCurrentLink() = this.url.toString()

    fun open(url: String) {
        loadUrl(url)
    }

    fun setOnPageFinishedListener(func: (String?) -> Unit) {
        onPageFinishedCall = func
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setSettings() {
        settings.javaScriptEnabled = true
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.domStorageEnabled = true
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
    }

    fun getUserString() = settings.userAgentString


    private fun setAcceptThirdPartyCookies() {
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(this, true);
    }
}
