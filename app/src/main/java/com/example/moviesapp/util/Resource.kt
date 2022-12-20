package com.example.moviesapp.util

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?, msg: String? = null): Resource<T> {
            return Resource(Status.SUCCESS, data, msg)
        }

//        fun <T> complete(data: T?): Resource<T> {
//            return Resource(Status.COMPLETE, data, null)
//        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

    }

}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    //COMPLETE,
}