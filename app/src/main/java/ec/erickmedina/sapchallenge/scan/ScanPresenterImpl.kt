package ec.erickmedina.sapchallenge.scan

import android.os.AsyncTask
import android.util.Log
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.services.vision.v1.Vision
import ec.erickmedina.sapchallenge.utils.Utils
import java.io.IOException
import java.lang.ref.WeakReference

class ScanPresenterImpl : ScanPresenter{

    var view : ScanView? = null

    override fun callCloudVision(annotation: Vision.Images.Annotate) {
        // Do the real work in an async task, because we need to use the network anyway
        try {
            val labelDetectionTask = LableDetectionTask(view!!, annotation)
            labelDetectionTask.execute()
        } catch (e: IOException) {
            Log.d("erick", "failed to make API request because of other IOException " + e.message)
        }
    }

    private val MAX_LABEL_RESULTS = 10



    class LableDetectionTask internal constructor(view: ScanView, private val mRequest: Vision.Images.Annotate) : AsyncTask<Any, Void, String>() {
        private val mActivityWeakReference: WeakReference<ScanView>

        init {
            mActivityWeakReference = WeakReference(view)
        }

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

}