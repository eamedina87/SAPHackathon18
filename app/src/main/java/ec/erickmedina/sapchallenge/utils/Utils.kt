package ec.erickmedina.sapchallenge.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import com.google.common.net.MediaType.PNG
import ec.erickmedina.sapchallenge.entities.api.IRLabel
import ec.erickmedina.sapchallenge.entities.api.IRResult
import java.io.ByteArrayOutputStream




class Utils{

    companion object {
        val CLOUD_API_KEY = "AIzaSyACBaD96tuTicMTUDqxhIBwn6-E-DFX8RA"
        val CLASSIFICATION_API_KEY = "Token ca8d6b93083c25de4655a3204839f935671c7a26"
        val CLASSIFICATION_TASK_ID = "ebbd66bd-8241-45b5-860b-f35aabb2ce9f"
        val BASE64_HEADER = "data:image/jpeg;base64,"


        fun scaleBitmapDown(bitmap: Bitmap, maxDimension: Int): Bitmap {

            val originalWidth = bitmap.getWidth()
            val originalHeight = bitmap.getHeight()
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

        @Throws(IOException::class)
        fun createImageFile(context: Context): File {
            // Create an image file name
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(
                    "JPEG_${timeStamp}_", /* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
            )
        }

        fun convertBitmapToBase64(bitmap: Bitmap): String {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            return BASE64_HEADER+Base64.getEncoder().encodeToString(byteArray)
        }

        fun getResultLabels(response: IRResult): List<IRLabel>? {
            if (response.status.code!=200)
                return null
            if (response.records.isEmpty())
                return null
            for (record in response.records) {
                return record.labels
            }
            return null
        }
    }



}
