package ec.erickmedina.sapchallenge.scan

interface ScanView {

    fun showProgress()
    fun hideProgress()
    fun onImageProcessingSuccess(results:String)
    fun onImageProcessingError()
    fun onRecycleInfoSuccess()
    fun onRecycleInfoError()

}