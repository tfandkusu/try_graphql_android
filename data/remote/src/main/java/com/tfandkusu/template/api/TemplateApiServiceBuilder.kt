package com.tfandkusu.template.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import java.util.Date
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object TemplateApiServiceBuilder {
    fun build(): TemplateApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(Date::class.java, Rfc3339DateJsonAdapter()).build()
                )
            )
            .build()
        return retrofit.create(TemplateApiService::class.java)
    }
}
