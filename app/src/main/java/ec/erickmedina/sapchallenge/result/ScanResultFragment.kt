package ec.erickmedina.sapchallenge.result

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ec.erickmedina.sapchallenge.R
import android.graphics.BitmapFactory
import kotlinx.android.synthetic.main.fragment_scan_result.*
import java.io.File


class ScanResultFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scan_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imgPath = arguments?.getString("Image")
        val resultString = arguments?.getString("Results")
        results.text = resultString
        imgPath.let {
            val file = File(imgPath)
            file.let {
                val bitmap = BitmapFactory.decodeFile(it.absolutePath)
                image.setImageBitmap(bitmap)
            }
        }

    }
}