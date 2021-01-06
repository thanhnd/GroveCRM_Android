package com.quynhlamryan.crm.utils

sealed class ErrorEntity {

    object Network : ErrorEntity()

    object NotFound : ErrorEntity()

    object AccessDenied : ErrorEntity()

    object ServiceUnavailable : ErrorEntity()

    object Unknown : ErrorEntity()
}

interface ErrorHandler {

    fun getError(throwable: Throwable): ErrorEntity
}