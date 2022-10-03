package com.star.muniperalillo

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import es.dmoral.toasty.Toasty
import org.json.JSONException

class NoticiasFragment : Fragment() {
    var url = "https://www.facebook.com/mperalillo/"
    var facebook: WebView? = null
    var root: View? = null
    var settings: WebSettings? = null
    var progressBar: ProgressBar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_noticias, container, false)
        progressBar = root!!.findViewById(R.id.progress_circular_country)
        return root
    }

    override fun onStart() {
        super.onStart()
        cargar_pagina_web()
    }

    fun cargar_pagina_web(){
        facebook = root!!.findViewById(R.id.facebook)
        settings = facebook!!.getSettings()
        settings!!.setJavaScriptEnabled(true)
        facebook!!.loadUrl(url)
        facebook!!.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                progressBar!!.visibility = View.GONE
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                super.onReceivedError(view, request, error)
                try{
                    Handler().postDelayed({
                        progressBar!!.visibility = View.VISIBLE
                        cargar_pagina_web()
                    }, 7000)
                }catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }
        })
    }

}