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
package com.ivo.ganev.datamusekotlin.endpoint.words

import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.json.Json


internal interface DatamuseJsonResponseDecoder {
    fun decode(jsonString: String): Set<WordResponse>
}

internal class KotlinJsonWordDecoder : DatamuseJsonResponseDecoder {

    /**
     * Decodes a Json body to a set of word responses
     * @throws [SerializationException] if the given JSON string cannot be deserialized
     * */
    override fun decode(jsonString: String): Set<WordResponse> =
        Json { ignoreUnknownKeys = true }.decodeFromString(
            SetSerializer(WordResponse.serializer()), jsonString
        )
}