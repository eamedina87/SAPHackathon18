package ec.erickmedina.sapchallenge.scan


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import ec.erickmedina.sapchallenge.R
import kotlinx.android.synthetic.main.fragment_scan.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ec.erickmedina.sapchallenge.MainActivity
import ec.erickmedina.sapchallenge.entities.api.IRLabel
import ec.erickmedina.sapchallenge.result.ScanResultFragment
import java.io.IOException
import ec.erickmedina.sapchallenge.utils.Utils
import java.io.File


class ScanViewFragment : Fragment(), ScanView {

    val REQUEST_IMAGE_CAPTURE = 1

    private val CLOUD_VISION_API_KEY = Utils.CLOUD_API_KEY
    private val ANDROID_CERT_HEADER = "X-Android-Cert"
    private val ANDROID_PACKAGE_HEADER = "X-Android-Package"
    private val MAX_LABEL_RESULTS = 10
    private val TAG = MainActivity::class.java.simpleName
    var mCurrentPhotoPath: String = ""

    private var mPresenter: ScanPresenterImpl? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        image.setOnClickListener { scanPicture() }
        mPresenter = ScanPresenterImpl()
        mPresenter?.view = this
    }

    private fun scanPicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity?.packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK
                && data.extras!=null && data.hasExtra("data")) {
            val imageBitmap = data.extras.get("data") as Bitmap
            image.setImageBitmap(imageBitmap)
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                null
            }
            photoFile.let {
                makeIRImageRequest(imageBitmap)
                //callCloudVision(imageBitmap)
                //onImageProcessingSuccess("")
            }
        }
    }

    private fun makeIRImageRequest(imageBitmap: Bitmap) {
        mPresenter?.makeIRImageRequest(Utils.convertBitmapToBase64(imageBitmap))
    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun onImageProcessingSuccess(results:String) {
        image_detail.text = results
        //TODO save to Database
        //mPresenter.saveToDatabase()
        loadResultFragment(results)
    }

    override fun onImageProcessingError() {

    }

    override fun onRecycleInfoSuccess() {

    }

    override fun onRecycleInfoError() {

    }

    private fun loadResultFragment(results:String) {
        activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.fragment_container, getResultFragment(results))
                ?.addToBackStack("")
                ?.commitAllowingStateLoss()
    }


    private fun getResultFragment(results: String): Fragment? {
        val fragment = ScanResultFragment()
        val arguments = Bundle()
        arguments.putString("Image", mCurrentPhotoPath)
        arguments.putString("Results", results)
        fragment.arguments = arguments
        return  fragment
    }

    private fun createImageFile(): File {
        return Utils.createImageFile(context!!).apply {
            mCurrentPhotoPath = absolutePath
        }
    }


    override fun onImageClassificationSuccess(resultLabels: List<IRLabel>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}