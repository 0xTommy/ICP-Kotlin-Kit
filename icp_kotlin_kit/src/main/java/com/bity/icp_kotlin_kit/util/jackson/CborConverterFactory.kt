package com.bity.icp_kotlin_kit.util.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

internal class CborConverterFactory private constructor(
    private val mapper: ObjectMapper
) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, Any> = JacksonResponseBodyConverter(mapper, type)

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<Any, RequestBody> = JacksonRequestBodyConverter(mapper, type)

    companion object {
        fun create(mapper: ObjectMapper) = CborConverterFactory(mapper)
    }
}