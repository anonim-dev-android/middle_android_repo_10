package ru.yandex.buggyweatherapp.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

object ImageLoader {
    
    private val imageCache = ConcurrentHashMap<String, Bitmap>()
    
    
    fun loadImageSync(url: String): Bitmap? {
        return if (imageCache.containsKey(url)) {
            imageCache[url]
        } else {
            try {
                val connection = URL(url).openConnection()
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                connection.connect()
                
                val inputStream = connection.getInputStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                
                imageCache[url] = bitmap
                bitmap
            } catch (e: Exception) {
                null
            }
        }
    }
    
    
    fun loadInto(url: String, imageView: ImageView) {
        if (imageCache.containsKey(url)) {
            imageView.setImageBitmap(imageCache[url])
        } else {
            
            Thread {
                val bitmap = loadImageSync(url)
                
                
                imageView.post {
                    imageView.setImageBitmap(bitmap)
                }
            }.start()
        }
    }
    
    
}