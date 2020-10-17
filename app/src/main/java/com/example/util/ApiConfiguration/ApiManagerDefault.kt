package com.example.util.ApiConfiguration

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.controllersystemapp.BuildConfig
import com.example.util.PrefsUtil
import com.example.util.UtilKotlin
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class ApiManagerDefault {
    var apiService: WebService
        private set
    var gson: Gson? = null
        get() {
            if (field == null) {
                field = GsonBuilder().setLenient().create()
            }
            return field
        }
        private set
    private var retrofit: Retrofit? = null
    private var okHttpAuthClient: OkHttpClient? = null
    private var okHttpLocalClient: OkHttpClient? = null

    constructor(authContext: Context) {
        apiService =
            getRetrofit(getHttpAuthClient(authContext)).create(
                WebService::class.java
            )
    }

    constructor(localContextActivity: Activity) {
        apiService =
            getRetrofit(getHttpLocalClient(localContextActivity)).create(
                WebService::class.java
            )
    }

    val loggingInterceptor: HttpLoggingInterceptor
        get() = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
    //a0:08:df:c2:87:9e:eb:ce:97:8f:02:16:27:b7:82:a9:63:5f:8d:24
    //cb:bd:17:8f:ce:34:24:20:9e:19:b1:2c:5d:07:f4:ff:cb:9f:05:9a
    fun getAuthInterceptor(context: Context?): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            Log.d("auth" , "api "+ PrefsUtil.getUserToken(context!!)?:"")
            //PrefsUtil.getUserToken(context!!)?:""
//Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvcGhvdG9uZWN0Lm9yZ1wvYXBpXC92MVwvY2xpZW50XC9hdXRoXC9sb2dpbiIsImlhdCI6MTU5MzQzNzM0OCwiZXhwIjoxOTUzNDM3MzQ4LCJuYmYiOjE1OTM0MzczNDgsImp0aSI6ImZ2QnFwbE1DZW9IOTlRMnIiLCJzdWIiOjE1LCJwcnYiOiI0MWVmYjdiYWQ3ZjZmNjMyZTI0MDViZDNhNzkzYjhhNmJkZWM2Nzc3In0.O2i15F-gb4flIoxHsC6OVhuWViAdQoioC2_GYUBVMeg
            chain.proceed(
                chain.request().newBuilder()
                    // .header("timezone", TimeZone.getDefault().id)
                     .header("locale", UtilKotlin.getLocalLanguage(context!!)?:"en")
                     .header("Authorization", PrefsUtil.getUserToken(context!!)?:"")
                     .method(original.method, original.body)
                     .build()
            )
        }
    }

    fun getLocalInterceptor(context: Context?): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            chain.proceed(
                chain.request().newBuilder()
                   .header("timezone", TimeZone.getDefault().id)
                    .header("locale", UtilKotlin.getLocalLanguage(context!!)?:"en")
                    .method(original.method, original.body)
                    .build()
            )
        }
    }

/*    fun getCustomErrorInterceptor(context: Context): Interceptor {
        return label@ Interceptor { chain: Interceptor.Chain ->
            // forward the request
            val request = chain.request()
            // get the response and check it for response code
            val response = chain.proceed(request)
            when (response.code) {
                200 -> if (isFakeResponse(response)) return@label getCustomResponse(
                    request,
                    context.getString(R.string.html_error),
                    402
                ) else return@label response
                429 -> return@label getCustomResponse(
                    request,
                    context.getString(R.string.html_error),
                    402
                )
                404, 503, 500 ->  // open ServerError Activity
                    MyUtil.showServerErrorDialog(context)
                401 ->  // send user to login page
                    MyUtil.loginAgain(context)
            }
            response
        }
    }*/

  /*  private fun isFakeResponse(response: Response): Boolean {
        val header = response.headers["Content-Type"]
        return header == null || header.contains("html")
    }*/

    fun getHttpAuthClient(context: Context): OkHttpClient {
        if (okHttpAuthClient == null) {
            okHttpAuthClient = OkHttpClient.Builder()
                .followRedirects(false)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(getAuthInterceptor(context))
              //  .addInterceptor(getCustomErrorInterceptor(context))
                .readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .build()
        }
        return okHttpAuthClient!!
    }

    fun getHttpLocalClient(activity: Activity): OkHttpClient {
        if (okHttpLocalClient == null) {
            okHttpLocalClient = OkHttpClient.Builder()
                .followRedirects(false)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(getLocalInterceptor(activity.applicationContext))
                //.addInterceptor(getCustomErrorInterceptor(activity.applicationContext))
                .readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .build()
        }
        return okHttpLocalClient!!
    }

    fun getRetrofit(client: OkHttpClient?): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(/*if (BuildConfig.DEBUG)*/ BASE_URL /*else LIVE_BASE_URL*/)
                .addConverterFactory(GsonConverterFactory.create(gson!!))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client!!)
                .build()
        }
        return retrofit!!
    }

    open fun getGsonApiManagerDefault(): Gson? {
        if (gson == null) {
            gson = GsonBuilder().setLenient().create()
        }
        return gson
    }

    companion object {
     /*   fun getCustomResponse(
            request: Request?,
            errorMessage: String,
            statusCode: Int?
        ): Response {
            val message = ResponseBody.create(
                parse.parse("application/json"),
                "{\n" + "" +
                        "    \"status\": false,\n" +
                        "    \"msg\": [\n" + errorMessage + "\n" +
                        "    ]\n" +
                        "}"
            )
            return Response.Builder().request(request!!)
                .protocol(Protocol.HTTP_1_1)
                .message(errorMessage)
                .body(message)
                .sentRequestAtMillis(-1L)
                .receivedResponseAtMillis(System.currentTimeMillis())
                .code(statusCode!!)
                .build()
        }*/
      val  NETWORK_TIMEOUT :Long = 30

            val BASE_URL = "https://al-aseyl.com/api/"
        val LIVE_BASE_URL = "https://al-aseyl.com/api/"
    }

}