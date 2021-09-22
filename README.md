# MyFrame
> Android Frame

## Language
> Kotlin

## Dependencies
> LiveData
> ViewModel
> Hilt
> OkHttp3
> Retrofit2
> Datastore
> Room
> Glide

## Command

> Refresh Dependencies Version

```shell
./gradlew refreshVersions
```

## Method usage example
> Hilt simple
```kotlin
@Module
@InstallIn(ActivityRetainedComponent::class)
class TestModule {

    @Provides
    fun providesAppUpdateService(
        client: OkHttpClient,
        factory: Converter.Factory
    ): AppUpdateService {
        return Retrofit.Builder()
            .baseUrl("https://www.baidu.com")
            .callFactory(client)
            .addConverterFactory(factory)
            .build()
            .create(AppUpdateService::class.java)
    }

    @Provides
    fun providesAppUpdateRepository(
        dataSource: AppUpdateDataSource
    ) = AppUpdateRepository(dataSource)
}

@HiltViewModel
class TestViewModel @Inject constructor(
    private val appUpdateRepository: AppUpdateRepository,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) : ViewModel() {

    private val _testLiveData = MutableLiveData<BaseResponse<AppUpdateInfo>>()
    val testLiveData: LiveData<BaseResponse<AppUpdateInfo>>
        get() = _testLiveData

    fun toTest() {
        viewModelScope.launch(dispatcherProvider.io) {
            val result =
                appUpdateRepository.toAppUpdate("com.langcoo.deviceUpdate", "1.0.0", "official")
            when (result) {
                is RequestResult.Success -> _testLiveData.postValue(result.data)
                else -> {
                }
            }
        }
    }
}

@AndroidEntryPoint
class TestActivity : AppCompatActivity() {

    private val viewModel: TestViewModel by viewModels()

    @Inject
    lateinit var json : Json

    @Inject
    lateinit var factory: Converter.Factory


    @Inject
    lateinit var t: HttpLoggingInterceptor

    @Inject
    lateinit var service: AppUpdateService

    @Inject
    lateinit var sourceData: AppUpdateDataSource

    @Inject
    lateinit var repo: AppUpdateRepository

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Inject
    @ApplicationContext
    lateinit var appContext: Context

    @Inject
    @ActivityContext
    lateinit var context: Context
}
```


> Load image by glide
```kotlin
var requestOptions = RequestOptions.circleCropTransform()
binding.ivGlide.loadImage(
    url = "https://t7.baidu.com/it/u=612028266,626816349&fm=193&f=GIF",
    preload = false,
    requestOptions = requestOptions
){
    object : CustomTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            Timber.i("----------------> onResourceReady")
            binding.ivGlide.setImageBitmap(resource)
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            Timber.i("----------------> onLoadCleared")
        }
    }
}
```

> Create Retrofit2 Request By Normal Mode
```kotlin

    @GET("http://sfcss.langcoo.net:90/rest/v/{applicationId}-{versionName}-{flavor}-android")
        suspend fun toAppUpdate(
            @Path("applicationId") applicationId: String,
            @Path("versionName") versionName: String,
            @Path("flavor") flavor: String
        ): BaseResponse<AppUpdateInfo>

    viewModelScope.launch {
        val service = RequestUtil.builder<AppUpdateService>()
        val result = service.toAppUpdate("com.langcoo.deviceUpdate", "1.0.0", "official")
        _testLiveData.postValue(result)
    }
```

> Change View State
```kotlin
    View.gone()
    View.visible()
    View.inVisible()
```

> Download File Has progress
```kotlin
call = DownloadUtil.loadFile(downloadUrl, filePath,
    object : OnDownloadListener {
        override fun onDownloadStart() {
            TODO("Not yet implemented")
        }

        override fun onDownloadSuccess(filePath: String) {
            TODO("Not yet implemented")
        }

        override fun onDownloading(progress: Int) {
            TODO("Not yet implemented")
        }

        override fun onDownloadFailed() {
            TODO("Not yet implemented")
        }
    })
```

> Upload File Has progress
```kotlin
    viewModel.toImageUpload(File(filePath), object : ProgressCallback {
                override fun onProgress(progress: Int, networkSpeed: Long, done: Boolean) {
                    Timber.e("---------> progress: $progress, networkSpeed: $networkSpeed, done: $done")
                    runOnUiThread {
                        uploadProgressDialogBuilder.setTipWord(
                            getString(
                                R.string.tip_fmt_upload_progress,
                                progress
                            )
                        )
                    }
                }
            })
```

## Theme Switch. Powered By QMUISkinLayoutInflaterFactory In QMUI
> Settable Items
```xml
    <declare-styleable name="QMUISkinDef">
        <attr name="qmui_skin_background" format="string" />
        <attr name="qmui_skin_text_color" format="string" />
        <attr name="qmui_skin_second_text_color" format="string" />
        <attr name="qmui_skin_src" format="string" />
        <attr name="qmui_skin_border" format="string" />
        <attr name="qmui_skin_separator_top" format="string" />
        <attr name="qmui_skin_separator_right" format="string" />
        <attr name="qmui_skin_separator_bottom" format="string" />
        <attr name="qmui_skin_separator_left" format="string" />
        <attr name="qmui_skin_alpha" format="string" />
        <attr name="qmui_skin_tint_color" format="string" />
        <attr name="qmui_skin_bg_tint_color" format="string" />
        <attr name="qmui_skin_progress_color" format="string" />
        <attr name="qmui_skin_underline" format="string"/>
        <attr name="qmui_skin_more_text_color" format="string"/>
        <attr name="qmui_skin_more_bg_color" format="string"/>
        <attr name="qmui_skin_hint_color" format="string"/>
        <attr name="qmui_skin_text_compound_tint_color" format="string"/>
        <attr name="qmui_skin_text_compound_src_left" format="string"/>
        <attr name="qmui_skin_text_compound_src_top" format="string"/>
        <attr name="qmui_skin_text_compound_src_right" format="string"/>
        <attr name="qmui_skin_text_compound_src_bottom" format="string"/>
    </declare-styleable>
```

## todo
1.权限申请
3.Room
4.Navigation
5.startActivityForResult
6.Datastore
7.FileUtil改造

