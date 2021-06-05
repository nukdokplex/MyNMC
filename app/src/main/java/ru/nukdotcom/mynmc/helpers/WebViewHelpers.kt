package ru.nukdotcom.mynmc.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.webkit.*
import ru.nukdotcom.mynmc.MainActivity
import ru.nukdotcom.mynmc.R
import java.lang.Exception
import java.net.URI
import java.net.URISyntaxException

class WebViewHelpers {
    companion object {
        public fun authenticateWebView(authHandler: AuthHandler, webView: WebView, context: Context){
            authHandler.reload()
            if (!authHandler.isAuthenticated()) return
            webView.loadUrl(context.getString(R.string.app_url_schema)+context.getString(R.string.app_domain)+context.getString(R.string.app_dir)+"mobile_login?token="+authHandler.token!!)
        }
        @Throws(URISyntaxException::class)
        public fun getDomainName(url: String): String {
            val uri = URI(url)
            val domain = uri.host
            if (domain.startsWith("www.")) return domain.substring(4) else return domain
        }

        public fun initializeManagementView(view: View, url: String){
            val webView = view.findViewById<WebView>(R.id.webView)

            webView.settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                databaseEnabled = true
                cacheMode = WebSettings.LOAD_DEFAULT
                loadWithOverviewMode = true

            }

            webView.addJavascriptInterface(JavaScriptInterface(view.context), "Android")

            webView.webViewClient = object: WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    return getDomainName(url!!) != view!!.context.getString(R.string.app_domain)
                }
            }

            webView.setDownloadListener(object: DownloadListener{
                override fun onDownloadStart(
                    url: String?,
                    userAgent: String?,
                    contentDisposition: String?,
                    mimetype: String?,
                    contentLength: Long
                ) {
                    webView.loadUrl(JavaScriptInterface.getBase64StringFromBlobUrl(url))



                }

            })

            CookieManager.getInstance().setCookie(url, "mobile=true")
            webView.loadUrl(url)
        }
    }


}