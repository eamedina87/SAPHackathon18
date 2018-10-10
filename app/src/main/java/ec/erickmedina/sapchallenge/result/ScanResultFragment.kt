package ec.erickmedina.sapchallenge.result

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ec.erickmedina.sapchallenge.R
import android.graphics.BitmapFactory
import kotlinx.android.synthetic.main.fragment_scan_result.*
import kotlinx.android.synthetic.main.fragment_scan_result.view.*
import kotlinx.android.synthetic.main.layout_resul_detail.*
import java.io.File


class ScanResultFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scan_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imgPath = arguments?.getString("Image")
        val resultString = arguments?.getString("Results")!!
        results.text = resultString
        showTrashInfoForResult(resultString)
        imgPath.let {
            val file = File(imgPath)
            file.let {
                val bitmap = BitmapFactory.decodeFile(it.absolutePath)
                image.setImageBitmap(bitmap)
            }
        }
    }

    private fun showTrashInfoForResult(resultString: String) {
        when {
            resultString.contains(getString(R.string.trash_type_plastic), true) -> showPlasticUI()
            resultString.contains(getString(R.string.trash_type_glass), true) -> showGlassUI()
            resultString.contains(getString(R.string.trash_type_food), true) -> showOrganicUI()
            else -> showNoResultUI()
        }
    }

    private fun showOrganicUI() {
        result_type.text = getString(R.string.trash_type_organic)
        result_trash.text = getString(R.string.trash_color_brown)
        result_description.text = getString(R.string.trash_description_organic)
        image_trash.setImageResource(R.mipmap.trash_bin_brown)
    }

    private fun showGlassUI() {
        result_type.text = getString(R.string.trash_type_glass)
        result_trash.text = getString(R.string.trash_color_green)
        result_description.text = getString(R.string.trash_description_glass)
        image_trash.setImageResource(R.mipmap.trash_bin_green)
    }

    private fun showPlasticUI() {
        result_type.text = getString(R.string.trash_type_plastic)
        result_trash.text = getString(R.string.trash_color_yellow)
        result_description.text = getString(R.string.trash_description_plastic)
        image_trash.setImageResource(R.mipmap.trash_bin_yellow)
    }

    private fun showNoResultUI() {
        result_type.text = "-"
        result_trash.text = "-"
        result_description.text = "-"
    }

}