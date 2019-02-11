package ec.erickmedina.sapchallenge.api

import ec.erickmedina.sapchallenge.entities.api.IRResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface IRApi {

    @POST ("recognition/v2/classify/")
    fun listRepos(@Body image: String): Call<IRResult>

}