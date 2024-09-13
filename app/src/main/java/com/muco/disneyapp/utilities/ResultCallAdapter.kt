package com.muco.disneyapp.utilities

import com.google.gson.Gson
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.InterruptedIOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.net.UnknownHostException

internal class ResultCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) return null
        check(returnType is ParameterizedType) { "Return type must be a parameterized type." }
        val responseType = getParameterUpperBound(0, returnType)
        if (getRawType(responseType) != Result::class.java) return null
        check(responseType is ParameterizedType) { "Response type must be a parameterized type." }
        val leftType = getParameterUpperBound(0, responseType)
        return ResultCallAdapter<Any>(leftType)
    }
}

private class ResultCallAdapter<R>(
    private val successType: Type
) : CallAdapter<R, Call<Result<R>>> {
    override fun adapt(call: Call<R>): Call<Result<R>> = ResultCall(call, successType)
    override fun responseType(): Type = successType
}

private class ResultCall<R>(
    private val delegate: Call<R>,
    private val successType: Type
) : Call<Result<R>> {
    override fun enqueue(callback: Callback<Result<R>>) = delegate.enqueue(
        object : Callback<R> {
            override fun onResponse(call: Call<R>, response: Response<R>) {
                callback.onResponse(this@ResultCall, Response.success(response.toResult()))
            }

            override fun onFailure(call: Call<R>, t: Throwable) {
                callback.onResponse(
                    this@ResultCall, Response.success(
                        Result.failure(
                            HttpStatus(
                                when (t) {
                                    is InterruptedIOException -> 408
                                    is UnknownHostException -> 421
                                    else -> 500
                                }, null
                            )
                        )
                    )
                )
            }
        }
    )

    override fun isExecuted(): Boolean = delegate.isExecuted
    override fun clone(): Call<Result<R>> = ResultCall(delegate.clone(), successType)
    override fun isCanceled(): Boolean = delegate.isCanceled
    override fun cancel() = delegate.cancel()
    override fun execute(): Response<Result<R>> = throw UnsupportedOperationException()
    override fun request() = delegate.request()
    override fun timeout() = delegate.timeout()

    fun <T> Response<T>.toResult(): Result<T> {
        val body = this.body()
        return if (!this.isSuccessful) Result.failure(
            HttpStatus(
                code = this.code(),
                errorMessage = this.errorBody()?.getErrorObject<Error>()?.error
            )
        )
        else if (body == null) Result.failure(NullBody())
        else Result.success(body)
    }

    inline fun <reified T> ResponseBody.getErrorObject(): T? {
        return try {
            val gson = Gson()
            val jsonObject = JSONObject(charStream().readText())
            gson.fromJson(jsonObject.toString(), T::class.java)
        } catch (e: Exception) {
            // Handle the exception accordingly, e.g., log it or return null
            e.printStackTrace()
            null
        }
    }

    data class Error(
        val error: String?
    )

    class NullBody : Exception()
}

class HttpStatus(val code: Int, val errorMessage: String?) : Exception()