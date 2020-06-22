package com.xently.payment

import android.annotation.SuppressLint
import com.xently.payment.utils.Constants.connectTO
import com.xently.payment.utils.Constants.readTO
import com.xently.payment.utils.Constants.writeTO
import com.xently.payment.utils.JSON_CONVERTER
import com.xently.payment.utils.Log
import com.xently.payment.utils.web.Headers
import com.xently.payment.utils.web.ServerResponse
import com.xently.payment.utils.web.TaskResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import java.util.*
import java.util.concurrent.TimeUnit

object WebServiceBuilder {

    val LOG_TAG = WebServiceBuilder::class.java.simpleName
    private const val BASE_URL: String = "http://10.0.2.2:8000/"

    private val loggingInterceptor = HttpLoggingInterceptor()
        .apply {
            level = (if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else HttpLoggingInterceptor.Level.NONE)
            redactHeader(Headers.AUTHORIZATION)  // Prevents header content logging
            redactHeader(Headers.DEVICE_IP_ADDRESS)
            redactHeader(Headers.DEVICE_ID)
            redactHeader(Headers.DEVICE_TOKEN)
            redactHeader(Headers.COOKIE)
        }

    @SuppressLint("ConstantLocale")
    private val headerInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader(Headers.DEVICE_IP_ADDRESS, "127.0.0.1")
            .addHeader(Headers.ACCEPT_LANGUAGE, Locale.getDefault().language)
            .build()

        return@Interceptor chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(connectTO, TimeUnit.SECONDS)
        .readTimeout(readTO, TimeUnit.SECONDS)
        .writeTimeout(writeTO, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(JSON_CONVERTER))
        .client(okHttpClient)
        .build()

    fun <T> getService(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    /**
     * Sends a network web-service request and returns the response payload bundled in [TaskResult].
     *
     * @param request **Retrofit** network request to be sent
     * @param withResponse Contains the whole server response. Also the default implementation for
     * **204** status code (with no response data). In this scenario, an assumption is made that the
     * network resource was deleted and no response could be returned...
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun <P, M, R : ServerResponse<P, M>> sendWebRequest(
        request: suspend () -> Response<R>,
        withResponse: (suspend (response: R) -> Unit)? = null
    ): TaskResult<P> {
        return try {
            val response = request.invoke() // Do actual network request/call
            val (statusCode, body, errorBody) = Triple(
                response.code(),
                response.body(),
                response.errorBody()
            )
            response.body()?.let { withResponse?.invoke(it) }
            if (response.isSuccessful) {
                val data = if (statusCode == 204) Any() as P else body?.payload ?: Any() as P
                TaskResult.Success(data)
            } else {
                withContext<TaskResult<P>>(Dispatchers.IO) {
                    TaskResult.Error(Exception(errorBody?.string()))
                }
            }
        } catch (ex: Exception) {
            Log.show(LOG_TAG, ex.message, ex, Log.Type.ERROR)
            TaskResult.Error(ex)
        }
    }

    @Throws(Exception::class)
    suspend fun requestFileDownload(
        response: suspend () -> Response<ResponseBody>,
        downloadLocation: String,
        onProgressUpdated: ((downloaded: Long, total: Long) -> Unit)?
    ): TaskResult<File> {
        return try {
            val responseBody = response.invoke().body()
                ?: throw  Exception("Unexpected server response")
            val file = saveDownloadedFile(downloadLocation, responseBody, onProgressUpdated)
            TaskResult.Success(file)
        } catch (ex: Exception) {
            TaskResult.Error(ex)
        }
    }

    private suspend fun saveDownloadedFile(
        downloadLocation: String,
        responseBody: ResponseBody,
        onProgressUpdated: ((downloaded: Long, total: Long) -> Unit)?
    ): File = withContext(Dispatchers.IO) {
        val file = File(downloadLocation)

        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null

        try {
            val fileReader = ByteArray(4096)

            val fileSize = responseBody.contentLength()
            var fileSizeDownloaded: Long = 0

            inputStream = responseBody.byteStream()
            outputStream = FileOutputStream(file)

            while (true) {
                val read = inputStream.read(fileReader)

                if (read == -1) break

                outputStream.write(fileReader, 0, read)

                fileSizeDownloaded += read.toLong()

                onProgressUpdated?.invoke(fileSizeDownloaded, fileSize)

                Log.show(LOG_TAG, "file download: $fileSizeDownloaded of $fileSize")
            }

            outputStream.flush()
        } catch (e: IOException) {
            Log.show(LOG_TAG, e.message, e, Log.Type.ERROR)
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
        file
    }

}