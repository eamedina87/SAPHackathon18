package ec.erickmedina.sapchallenge.api

import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import ec.erickmedina.sapchallenge.BuildConfig
import ec.erickmedina.sapchallenge.utils.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class IRApiClient {
    companion object {
        private val mApi: IRApi
        private var mRetrofit: Retrofit

        init {
            val httpCLient = OkHttpClient.Builder()
                    .connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            httpCLient.addNetworkInterceptor(
                    LoggingInterceptor.Builder()
                    .loggable(true)
                    .setLevel(Level.BASIC)
                    .log(Platform.INFO)
                    .tag("LoggingI")
                    .request("Request")
                    .response("Response")
                    .addHeader("version", BuildConfig.VERSION_NAME)
                    .addHeader("Accept-Encoding", "identity")
                    .build())

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

}