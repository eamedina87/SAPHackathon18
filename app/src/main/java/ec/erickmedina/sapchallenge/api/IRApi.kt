package ec.erickmedina.sapchallenge.api

import ec.erickmedina.sapchallenge.entities.api.IRRequest
import ec.erickmedina.sapchallenge.entities.api.IRResult
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.net.CacheRequest


interface IRApi {

    @POST ("recognition/v2/classify/")
    fun getCategoriesForImage(@Header("Authorization") apiKey : String,
                              @Body request: IRRequest): Call<IRResult>

}