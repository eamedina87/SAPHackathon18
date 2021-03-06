package ec.erickmedina.sapchallenge.scan


import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import ec.erickmedina.sapchallenge.R
import kotlinx.android.synthetic.main.fragment_scan.*
import android.util.Log
import ec.erickmedina.sapchallenge.MainActivity
import com.google.api.services.vision.v1.Vision
import com.google.api.services.vision.v1.VisionRequest
import com.google.api.services.vision.v1.VisionRequestInitializer
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.services.vision.v1.model.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import ec.erickmedina.sapchallenge.utils.PackageManagerUtils
import ec.erickmedina.sapchallenge.utils.Utils


class ScanViewImpl : AppCompatActivity(), ScanView {

    val REQUEST_IMAGE_CAPTURE = 1

    private val CLOUD_VISION_API_KEY = Utils.CLOUD_API_KEY
    private val ANDROID_CERT_HEADER = "X-Android-Cert"
    private val ANDROID_PACKAGE_HEADER = "X-Android-Package"
    private val MAX_LABEL_RESULTS = 10
    private val TAG = MainActivity::class.java.simpleName

    private var mPresenter: ScanPresenterImpl? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_scan)
        image.setOnClickListener { scanPicture() }
        mPresenter = ScanPresenterImpl()
        mPresenter?.view = this
    }

    private fun scanPicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK
                && data.extras!=null && data.hasExtra("data")) {
            val imageBitmap = data.extras.get("data") as Bitmap
            image.setImageBitmap(imageBitmap)
            callCloudVision(imageBitmap)
        }

    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun onImageProcessingSuccess(results:String) {
        image_detail.text = results
    }

    override fun onImageProcessingError() {

    }

    override fun onRecycleInfoSuccess() {

    }

    override fun onRecycleInfoError() {

    }

    private fun callCloudVision(bitmap: Bitmap) {
        mPresenter?.callCloudVision(prepareAnnotationRequest(bitmap))
    }


    @Throws(IOException::class)
    private fun prepareAnnotationRequest(bitmap: Bitmap): Vision.Images.Annotate {
        val httpTransport = AndroidHttp.newCompatibleTransport()
        val jsonFactory = GsonFactory.getDefaultInstance()

        val requestInitializer = object : VisionRequestInitializer(CLOUD_VISION_API_KEY) {
            /**
             * We override this so we can inject important identifying fields into the HTTP
             * headers. This enables use of a restricted cloud platform API key.
             */
            @Throws(IOException::class)
            override fun initializeVisionRequest(visionRequest: VisionRequest<*>?) {
                super.initializeVisionRequest(visionRequest)

                val packageName = packageName
                visionRequest!!.requestHeaders.set(ANDROID_PACKAGE_HEADER, packageName)

                val sig = PackageManagerUtils.getSignature(packageManager, packageName)

                visionRequest.requestHeaders.set(ANDROID_CERT_HEADER, sig)
            }
        }

        val builder = Vision.Builder(httpTransport, jsonFactory, null)
        builder.setVisionRequestInitializer(requestInitializer)

        val vision = builder.build()

        val batchAnnotateImagesRequest = BatchAnnotateImagesRequest()
        batchAnnotateImagesRequest.requests = object : ArrayList<AnnotateImageRequest>() {
            init {
                val annotateImageRequest = AnnotateImageRequest()

                // Add the image
                val base64EncodedImage = Image()
                // Convert the bitmap to a JPEG
                // Just in case it's a format that Android understands but Cloud Vision
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)
                val imageBytes = byteArrayOutputStream.toByteArray()

                // Base64 encode the JPEG
                base64EncodedImage.encodeContent(imageBytes)
                annotateImageRequest.image = base64EncodedImage

                // add the features we want
                annotateImageRequest.features = object : ArrayList<Feature>() {
                    init {
                        val labelDetection = Feature()
                        labelDetection.type = "LABEL_DETECTION"
                        labelDetection.maxResults = MAX_LABEL_RESULTS
                        add(labelDetection)
                    }
                }

                // Add the list of one thing to the request
                add(annotateImageRequest)
            }
        }

        val annotateRequest = vision.images().annotate(batchAnnotateImagesRequest)
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotateRequest.disableGZipContent = true
        Log.d(TAG, "created Cloud Vision request object, sending request")

        return annotateRequest
    }

}