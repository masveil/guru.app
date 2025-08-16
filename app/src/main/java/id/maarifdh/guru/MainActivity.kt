package id.maarifdh.guru

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.View

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var swipe: SwipeRefreshLayout

    private val HOME_URL = "https://guru.maarifdh.sch.id"
    private val ALLOWED_HOSTS = setOf(
        "guru.maarifdh.sch.id",
        "absensi.maarifdh.sch.id",
        "kaldik.maarifdh.sch.id",
        "jadwal.maarifdh.sch.id",
        "krisar.maarifdh.sch.id"
    )

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipe = findViewById(R.id.swipe)
        webView = findViewById(R.id.webview)

        with(webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            loadsImagesAutomatically = true
            builtInZoomControls = false
            displayZoomControls = false
            setSupportMultipleWindows(true)
            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                swipe.isRefreshing = false
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val target = request?.url ?: return false
                return handleNavigation(target)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return url?.let { handleNavigation(Uri.parse(it)) } ?: false
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
                val transport = resultMsg?.obj as WebView.WebViewTransport
                transport.webView = webView
                resultMsg.sendToTarget()
                return true
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                // Stop refresh indicator when loaded
                if (newProgress >= 90) swipe.isRefreshing = false
                super.onProgressChanged(view, newProgress)
            }
        }

        swipe.setOnRefreshListener {
            webView.reload()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) webView.goBack() else finish()
            }
        })

        if (savedInstanceState == null) {
            val startUri = intent?.data
            if (startUri != null && isAllowed(startUri)) {
                webView.loadUrl(forceHttps(startUri).toString())
            } else {
                webView.loadUrl(HOME_URL)
            }
        } else {
            webView.restoreState(savedInstanceState)
        }
    }

    private fun isAllowed(uri: Uri): Boolean {
        val host = (uri.host ?: "").lowercase()
        return host in ALLOWED_HOSTS
    }

    private fun forceHttps(uri: Uri): Uri {
        return if ((uri.scheme ?: "").lowercase() == "http" && isAllowed(uri)) {
            uri.buildUpon().scheme("https").build()
        } else uri
    }

    private fun openExternal(uri: Uri) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (_: ActivityNotFoundException) { }
    }

    private fun handleNavigation(uriIn: Uri): Boolean {
        val uri = forceHttps(uriIn)
        val scheme = (uri.scheme ?: "").lowercase()

        if (scheme !in listOf("http", "https")) {
            openExternal(uri)
            return true
        }
        return if (isAllowed(uri)) {
            webView.loadUrl(uri.toString())
            false
        } else {
            openExternal(uri)
            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }
}
