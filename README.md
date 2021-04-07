# MyFrame
> Android Frame

## Language
> Kotlin

## Dependencies
> LiveData
> ViewModel
> Dagger2
> OkHttp3
> Retrofit2
> Room
> Glide

## Method usage example
> Load image by glide
```
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

