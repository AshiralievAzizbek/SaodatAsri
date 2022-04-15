package me.owapps.saodatasri.util

import me.owapps.saodatasri.data.entities.ApiError
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException

enum class ErrorCodes(val code: Int) {
    SocketTimeOut(-1)
}

class ResponseHandler {

    private fun parseError(response: Response<*>): ApiError {
        if (response.errorBody() != null) {
            val jsonObject = JSONObject(response.errorBody()!!.string())
            val errorObject = jsonObject.getJSONObject("error")
            val code = errorObject.getInt("code")
            val message = errorObject.getString("message")

            return ApiError(code, message)
        }
        return ApiError(400, "Unknown Error 400")
    }

    private fun getErrorMessage(code: Int, message: String?): String {
        return when (code) {
            ErrorCodes.SocketTimeOut.code -> "Timeout"
            401 -> "Unauthorised"
            404 -> "Page not found"
            else -> "Code: $code;\nMessage: $message"
        }
    }

    fun <T> handleSuccess(data: T): Resource<T> {
        return Resource.Success(data)
    }

    fun <T: Any> handleException(e: Exception): Resource<T> {
        return when (e) {
            is HttpException -> {
                val parseError = parseError(e.response()!!)
                Resource.Error(message = parseError.message, data = null)
            }
            is SocketTimeoutException -> {
                val code = ErrorCodes.SocketTimeOut.code
                Resource.Error(message = getErrorMessage(code, e.message), data = null)
            }
            else -> Resource.Error(
                message = getErrorMessage(Int.MAX_VALUE, e.message),
                data = null
            )
        }
    }


}