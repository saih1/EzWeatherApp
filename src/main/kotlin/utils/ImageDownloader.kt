package utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import org.jetbrains.skia.Image

object ImageDownloader {
    private val imageClient: HttpClient = HttpClient(CIO)
    suspend fun downloadImage(url: String): ImageBitmap {
        val image: ByteArray = imageClient.get(url)
        // convert to ImageBitmap and return
        return Image.makeFromEncoded(image).toComposeImageBitmap()
    }
}