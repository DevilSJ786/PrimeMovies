package com.tmdb.movieapi.utils

sealed class Response<T>(val data:T?=null,val message:String?=null){
    class Loading<T>:Response<T>()
    class Success<T>(data: T):Response<T>(data)
    class Error<T>(errorMessage: String?=null):Response<T>()
}
