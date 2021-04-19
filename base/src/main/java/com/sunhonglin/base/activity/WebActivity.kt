package com.sunhonglin.base.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.*
import com.sunhonglin.base.R
import com.sunhonglin.base.StatusBarMode
import com.sunhonglin.base.databinding.ActivityBaseContentBinding
import com.sunhonglin.base.databinding.ActivityWebBinding
import com.sunhonglin.base.databinding.LayoutDefaultToolbarBinding
import com.sunhonglin.base.utils.DialogUtil
import com.sunhonglin.core.util.get
import com.sunhonglin.core.util.gone
import com.sunhonglin.core.util.visible
import timber.log.Timber

@SuppressLint("SetJavaScriptEnabled")
class WebActivity : DefaultToolbarActivity() {
    companion object {
        const val PATH = "path"
        const val CACHE = "cache"
    }

    private lateinit var bindingToolbar: LayoutDefaultToolbarBinding
    private lateinit var binding: ActivityWebBinding

    override fun configureToolbarContent(toolbarBinding: LayoutDefaultToolbarBinding) {
        super.configureToolbarContent(toolbarBinding)
        bindingToolbar = toolbarBinding
    }

    override fun inflateContent(
        parentBinding: ActivityBaseContentBinding,
        savedInstanceState: Bundle?
    ) {
        setStatusBarMode(StatusBarMode.LIGHT)
        binding = ActivityWebBinding.inflate(layoutInflater, parentBinding.content, true)

        initData()
    }


    private fun initData() {
        binding.inWebBase.pbWebBase.max = 100 //设置加载进度最大值

        val webPath = intent.get<String>(PATH) //加载的URL
        if (webPath.isNullOrBlank()) {
            DialogUtil.showTipDialog(mContext, getString(R.string.url_cannot_null))
            return
        }

        var cache = WebSettings.LOAD_CACHE_ELSE_NETWORK// 加载缓存否则网络
        intent.get<Int>(CACHE)?.let {
            cache = it
        }

        with(binding.inWebBase.wvBase) {
            settings.apply {
                cacheMode = cache
                loadsImagesAutomatically = true  //图片自动缩放 打开
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW //https和http共存
                javaScriptEnabled = true // 设置支持javascript脚本
                setSupportZoom(true) // 设置可以支持缩放
                //设置出现缩放工具 是否使用WebView内置的缩放组件，由浮动在窗口上的缩放控制和手势缩放控制组成，默认false
                builtInZoomControls = true
                displayZoomControls = false //隐藏缩放工具
                useWideViewPort = true // 扩大比例的缩放
                loadWithOverviewMode = true
                databaseEnabled = true
                //是否开启本地DOM存储  鉴于它的安全特性（任何人都能读取到它，尽管有相应的限制，将敏感数据存储在这里依然不是明智之举），Android 默认是关闭该功能的。
                domStorageEnabled = true

//                allowContentAccess = true
//                allowFileAccessFromFileURLs = true
//                setAppCacheEnabled(true)
//                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
//                mediaPlaybackRequiresUserGesture = true //是否需要用户手势来播放Media，默认true
//                pluginState = WebSettings.PluginState.ON
//                layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN //自适应屏幕
//                savePassword = true //保存密码
            }

            isSaveEnabled = true
            keepScreenOn = true
            setLayerType(View.LAYER_TYPE_SOFTWARE, null) //软件解码
            setLayerType(View.LAYER_TYPE_HARDWARE, null) //硬件解码

            webChromeClient = object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView, title: String) {
                    super.onReceivedTitle(view, title)
                    bindingToolbar.textTitle.text = title
                }

                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    binding.inWebBase.pbWebBase.progress = newProgress
                }
            }

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    binding.inWebBase.pbWebBase.gone()
                    settings.loadsImagesAutomatically = true
                }

                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    binding.inWebBase.pbWebBase.visible()
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest
                ): Boolean {
                    if (request.url.toString().startsWith("http:") || request.url.toString()
                            .startsWith("https:")
                    ) {
                        if (request.url.toString().startsWith(".apk")) {
                            try {
                                val intent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse(request.url.toString()))
                                startActivity(intent)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            return true
                        }
                    } else {
                        return true
                    }
                    return super.shouldOverrideUrlLoading(view, request)
                }
            }

            setDownloadListener { url, _, _, _, _ ->
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.data = Uri.parse(url)
                startActivity(intent)
            }

            loadUrl(webPath)
        }

        Timber.d("onLoading : $webPath")
    }

    override fun onBackPressed() {
        if (binding.inWebBase.wvBase.canGoBack()) {
            binding.inWebBase.wvBase.goBack()
        } else {
            finish()
        }
    }
}