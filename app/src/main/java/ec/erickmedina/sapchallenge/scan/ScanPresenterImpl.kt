package ec.erickmedina.sapchallenge.scan

import android.os.AsyncTask
import android.util.Log
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.services.vision.v1.Vision
import ec.erickmedina.sapchallenge.api.IRApiClient
import ec.erickmedina.sapchallenge.entities.api.IRRequest
import ec.erickmedina.sapchallenge.entities.api.IRRequestBase64
import ec.erickmedina.sapchallenge.entities.api.IRResult
import ec.erickmedina.sapchallenge.utils.Utils
import java.io.IOException
import java.lang.Exception
import java.lang.ref.WeakReference

class ScanPresenterImpl : ScanPresenter{

    private val MAX_LABEL_RESULTS = 10
    var view : ScanView? = null

    override fun callCloudVision(annotation: Vision.Images.Annotate) {
        // Do the real work in an async task, because we need to use the network anyway
        try {
            val labelDetectionTask = LabelDetectionTask(view!!, annotation)
            labelDetectionTask.execute()
        } catch (e: IOException) {
            Log.d("erick", "failed to make API request because of other IOException " + e.message)
        }
    }

    override fun makeIRImageRequest(convertBitmapToBase64: String) {
        try {
            val list = listOf(IRRequestBase64(convertBitmapToBase64))
            val labelDetectionTask = ClassificationTask(view!!, IRRequest(list, Utils.CLASSIFICATION_TASK_ID))
            labelDetectionTask.execute()
        } catch (e: IOException) {
            Log.d("erick", "failed to make API request because of other IOException " + e.message)
        }
    }

    class LabelDetectionTask internal constructor(view: ScanView, private val mRequest: Vision.Images.Annotate) : AsyncTask<Any, Void, String>() {
        private val mActivityWeakReference: WeakReference<ScanView> = WeakReference(view)

        override fun doInBackground(vararg params: Any): String {
            try {
                Log.d("", "created Cloud Vision request object, sending request")
                val response = mRequest.execute()
                return Utils.convertResponseToString(response)

            } catch (e: GoogleJsonResponseException) {
                Log.d("erick", "failed to make API request because " + e.content)
            } catch (e: IOException) {
                Log.d("erick", "failed to make API request because of other IOException " + e.message)
            }

            return "Cloud Vision API request failed. Check logs for details."
        }


        override fun onPostExecute(result: String) {
            Log.d("erick", "result:$result")
            val view = mActivityWeakReference.get()
            view?.onImageProcessingSuccess(result)
        }
    }


    class ClassificationTask internal constructor(view: ScanView, private val mRequest: IRRequest) : AsyncTask<Any, Void, IRResult?>() {
        private val mActivityWeakReference: WeakReference<ScanView> = WeakReference(view)

        override fun doInBackground(vararg params: Any): IRResult? {

            try {
                Log.d("", "created Cloud Vision request object, sending request")
                val response = IRApiClient.getApiObject().getCategoriesForImage(Utils.CLASSIFICATION_API_KEY, mRequest).execute()
                return  when (response.isSuccessful){
                    true -> response.body()
                    else -> null
                }
            } catch (e: Exception) {
                Log.d("erick", "failed to make API request because of other IOException " + e.message)
            }
            return null
        }

        override fun onPostExecute(result: IRResult?) {
            super.onPostExecute(result)
            val view = mActivityWeakReference.get()
            if (result == null){
                view?.onImageProcessingError()
            } else {
                view?.onImageClassificationSuccess(Utils.getResultLabels(result))
            }
        }

    }
}