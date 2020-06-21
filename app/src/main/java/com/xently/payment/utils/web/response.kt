package com.xently.payment.utils.web

import com.xently.payment.utils.IData
import com.xently.payment.utils.JSON_CONVERTER
import com.xently.payment.utils.web.TaskResult.Error

interface OnCompleteListener<T> {
    fun onComplete(task: TaskResult<T>?)
}

/**
 * Model or data class to serialize **WebService** or API response as it's expected in below format
 * ``` json
 *     {
 *      "is_error": false,
 *      "status_code": 200,
 *      "message": ".....",
 *      "debug_message": null,
 *      "payload": Any,
 *      "metadata": Any
 *      }
 * ```
 * @param isError if `true` then API responded with an error else otherwise
 * @param statusCode http status code
 * @param message message received from the API response. Can be used to communicate to user
 * @param debugMessage debug message received from the API response. Can be used logged to get a
 * better understanding of where an error occurred if [isError] is true
 * @param payload **data** part of API response
 * @param metadata additional important payload or control info
 */
data class ServerResponse<P, M>(
    val isError: Boolean = true,
    val statusCode: Int = 200,
    val message: String? = null,
    val debugMessage: Any? = null,
    val payload: P? = null,
    val metadata: M? = null
) {

    override fun toString(): String = JSON_CONVERTER.toJson(this)

    companion object : IData<ServerResponse<*, *>> {
        @JvmStatic
        override fun fromJson(json: String?): ServerResponse<*, *>? =
            com.xently.payment.utils.fromJson(json)
    }
}

sealed class TaskResult<out R> {
    data class Success<out T>(val data: T) : TaskResult<T>() {
        override fun toString(): String = super.toString()
    }

    data class Error(val error: Exception) : TaskResult<Nothing>()

    companion object Loading : TaskResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success -> JSON_CONVERTER.toJson(this)
            is Error -> error.message!!
            is Loading -> "Loading..."
        }
    }

    @ExperimentalStdlibApi
    fun <T> addOnCompleteListener(listener: OnCompleteListener<T>): TaskResult<R> {
        @Suppress("UNCHECKED_CAST")
        listener.onComplete(this as TaskResult<T>?)
        return this
    }
}

inline val TaskResult<*>.isLoading
    get() = this is TaskResult.Loading

inline val TaskResult<*>.isSuccessful
    get() = this is TaskResult.Success && data != null

inline val <T> TaskResult<T>.data: T?
    get() = if (isSuccessful) (this as TaskResult.Success).data else null

inline val <T> TaskResult<T>.dataOrFail: T
    get() = try {
        this.data!!
    } catch (ex: Exception) {
        throw Exception("Invalid ${TaskResult::class.java.simpleName} type! ${ex.message}")
    }

inline val <T> TaskResult<List<T>>.listData: List<T>
    get() = data ?: emptyList()

inline val TaskResult<*>.errorMessage
    get() = if (this is Error) error.message else null

inline val TaskResult<*>.isError
    get() = this is Error

