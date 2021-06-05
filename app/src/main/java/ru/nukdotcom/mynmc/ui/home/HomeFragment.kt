package ru.nukdotcom.mynmc.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import ru.nukdotcom.mynmc.MainActivity.Companion.authHandler
import ru.nukdotcom.mynmc.R
import ru.nukdotcom.mynmc.helpers.WebViewHelpers.Companion.authenticateWebView
import ru.nukdotcom.mynmc.helpers.WebViewHelpers.Companion.getDomainName

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private lateinit var mView: View
    private lateinit var webView: WebView
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_home, container, false)

        webView = mView.findViewById(R.id.webView)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
        }

        val url = this.getString(R.string.app_url_schema) + this.getString(R.string.app_domain) + this.getString(R.string.app_dir)

        webView.webViewClient = object: WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return getDomainName(url!!) != getString(R.string.app_domain)
            }
        }

        CookieManager.getInstance().setCookie(url, "mobile=true")
        authenticateWebView(authHandler!!, webView, requireContext())
        //webView.loadUrl(url)

        return mView
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}