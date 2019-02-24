package ec.erickmedina.sapchallenge.scan

import com.google.api.services.vision.v1.Vision

interface ScanPresenter {
    fun callCloudVision(annotation: Vision.Images.Annotate)
    fun makeIRImageRequest(convertBitmapToBase64: String)
}