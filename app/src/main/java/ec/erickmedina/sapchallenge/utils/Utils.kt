package ec.erickmedina.sapchallenge.utils

import android.graphics.Bitmap
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse
import java.util.*


class Utils{

    companion object {
        val CLOUD_API_KEY = "AIzaSyACBaD96tuTicMTUDqxhIBwn6-E-DFX8RA"

        fun scaleBitmapDown(bitmap: Bitmap, maxDimension: Int): Bitmap {

            val originalWidth = bitmap.width
            val originalHeight = bitmap.height
            var resizedWidth = maxDimension
            var resizedHeight = maxDimension

            if (originalHeight > originalWidth) {
                resizedHeight = maxDimension
                resizedWidth = (resizedHeight * originalWidth.toFloat() / originalHeight.toFloat()).toInt()
            } else if (originalWidth > originalHeight) {
                resizedWidth = maxDimension
                resizedHeight = (resizedWidth * originalHeight.toFloat() / originalWidth.toFloat()).toInt()
            } else if (originalHeight == originalWidth) {
                resizedHeight = maxDimension
                resizedWidth = maxDimension
            }
            return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false)
        }


        fun convertResponseToString(response: BatchAnnotateImagesResponse): String {
            val message = StringBuilder("I found these things:\n\n")

            val labels = response.responses[0].labelAnnotations
            if (labels != null) {
                for (label in labels) {
                    message.append(String.format(Locale.US, "%.3f: %s", label.score, label.description))
                    message.append("\n")
                }
            } else {
                message.append("nothing")
            }

            return message.toString()
        }
    }



}
