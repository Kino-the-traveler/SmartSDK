package com.sdz.smartsdk

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.webkit.*

open class SmartView(context: Context) : SmartBaseView(context) {
    private val COID_TAG = "cust_offer_id"

    init {
        webViewClient = ScreenWorkClient()
        webChromeClient = WebChromeClient()
    }

    fun reloadIfNeeded() {
        if (needsToReload) {
            reload()
            needsToReload = false
        }
    }

    inner class ScreenWorkClient() : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            catchCOIDFromQuery(Uri.parse(url))
            return false
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            catchCOIDfromPreland()
            catchCOIDFromQuery(Uri.parse(url))
            onPageFinishedCall(url)
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            needsToReload = true
        }
    }

    private fun catchCOIDFromQuery(uri: Uri) {
        val coid = uri.getQueryParameter(COID_TAG)
        coid?.let { queryCOID = it }
    }

    private fun catchCOIDfromPreland() {
        val coidCatcher = "document.querySelector('meta[name=\"$COID_TAG\"]').content\n"
        evaluateJavascript(coidCatcher) {
            val formattedCoid = it.replace("\"", "")
            if (formattedCoid.isNotEmpty() && formattedCoid != "null") {
                prelandCOID = formattedCoid
            }
        }
    }

}