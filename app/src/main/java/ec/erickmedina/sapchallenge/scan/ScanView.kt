package ec.erickmedina.sapchallenge.scan

import ec.erickmedina.sapchallenge.entities.api.IRLabel

interface ScanView {

    fun showProgress()
    fun hideProgress()
    fun onImageProcessingSuccess(results:String)
    fun onImageProcessingError()
    fun onRecycleInfoSuccess()
    fun onRecycleInfoError()
    fun onImageClassificationSuccess(resultLabels: List<IRLabel>?)

}