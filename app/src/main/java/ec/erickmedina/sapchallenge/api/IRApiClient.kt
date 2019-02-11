package ec.erickmedina.sapchallenge.api

import ec.erickmedina.sapchallenge.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class IRApiClient {

    private val mApi: IRApi
    private var mRetrofit: Retrofit

    init {
        val httpCLient = OkHttpClient.Builder()
                .connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        val client = httpCLient.build()
        mRetrofit = Retrofit.Builder()
                .baseUrl(Constants.IR_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
        mApi = mRetrofit.create(IRApi::class.java)
    }

    fun getApiObject() = mApi


}