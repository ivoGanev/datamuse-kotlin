/**
 * Copyright (C) 2020 Ivo Ganev Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ivo.ganev.datamuse_kotlin.client

import com.ivo.ganev.datamuse_kotlin.configuration.EndpointBuilder
import com.ivo.ganev.datamuse_kotlin.configuration.QueryConfig
import com.ivo.ganev.datamuse_kotlin.configuration.WordsEndpointBuilder
import com.ivo.ganev.datamuse_kotlin.configuration.WordsEndpointQueryConfig
import com.ivo.ganev.datamuse_kotlin.endpoint.words.*
import com.ivo.ganev.datamuse_kotlin.response.QueryResponse
import com.ivo.ganev.datamuse_kotlin.response.RemoteFailure
import com.ivo.ganev.datamuse_kotlin.response.RemoteFailure.MalformedJsonBodyFailure
import kotlinx.coroutines.*
import kotlinx.serialization.SerializationException
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * [DatamuseClient]'s purpose is to make queries to it's endpoints and retrieve
 * the results in a [QueryResponse]
 * */
class DatamuseClient  {
    private val wordResponseDecoder = WordResponseDecoder()
    private val httpClient: OkHttpClient = OkHttpClient()

    /**
     * This function will call the async version of it. You can use this one to
     * update live data without await-ing.
     * */
    suspend fun queryWordsEndpoint(builder: EndpointBuilder<WordsEndpointQueryConfig>):
            QueryResponse<RemoteFailure, Set<WordResponse>> =
        queryWordsEndpointAsync(builder).await()

    /**
     * Will query the Datamuse API asynchronously
     * */
    fun queryWordsEndpointAsync(builder: EndpointBuilder<WordsEndpointQueryConfig>) =
        GlobalScope.async(Dispatchers.IO) {
            val request: Request = Request.Builder()
                .url(builder.buildUrl())
                .build()

            httpClient.newCall(request).execute().use {
                if (!it.isSuccessful)
                    return@async QueryResponse.Failure(RemoteFailure.HttpCodeFailure(it.code))

                val jsonBody = it.body?.string() ?: return@async QueryResponse.Failure(MalformedJsonBodyFailure("Json body is null."))
                val jsonBodyResponseSet: Set<WordResponse>

                try {
                    jsonBodyResponseSet = wordResponseDecoder.decode(jsonBody)
                } catch (e: SerializationException) {
                    return@async QueryResponse.Failure(MalformedJsonBodyFailure(e.message))
                }
                return@async QueryResponse.Result(jsonBodyResponseSet)
            }
        }
}