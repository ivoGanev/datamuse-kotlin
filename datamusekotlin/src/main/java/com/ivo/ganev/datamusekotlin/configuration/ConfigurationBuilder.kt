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
package com.ivo.ganev.datamusekotlin.configuration

import com.ivo.ganev.datamusekotlin.endpoint.words.*
import com.ivo.ganev.datamusekotlin.exceptions.UnspecifiedHardConstraintException
import java.util.*


/**
 * This is a builder designed specifically for library clients. It will
 * */
class ConfigurationBuilder {
    /**
     * A query element for the hard constraints.
     * @see [HardConstraint]
     * */
    var hardConstraint: HardConstraint? = null

    /**
     * A query element for the topics
     * @see [Topic]
     * */
    var topics: String? = null

    /**
     * A query element for the left context
     * @see  [LeftContext]
     * */
    var leftContext: String? = null

    /**
     * A query element for the right context
     * @see  [RightContext]
     * */
    var rightContext: String? = null

    /**
     * A query element for the max results
     * @see  [MaxResults]
     * */
    var maxResults: Int? = null

    /**
     * @see  [Metadata]
     * */
    var metadata: EnumSet<MetadataFlag>? = null

    internal fun build(): Configuration {
        return Configuration(
            hardConstraint,
            topics?.let { Topic(it) },
            leftContext?.let { LeftContext(it) },
            rightContext?.let { RightContext(it) },
            maxResults?.let { MaxResults(it) },
            metadata?.let { Metadata(it) }
        )
    }
}

/**
 * Builds the URL address for the Datamuse API. In order to be able to build a URL you need to
 * provide at least a hard constraint.
 *
 * @throws UnspecifiedHardConstraintException - when no hard constraint is specified
 * */
fun buildWordsEndpointUrl(wordsConfig: ConfigurationBuilder.() -> Unit):
        ConfigurationBuilder {
    val builder = ConfigurationBuilder()
    builder.wordsConfig()
    if (builder.hardConstraint == null)
        throw UnspecifiedHardConstraintException(
            "You need to provide a hard constraint in order to build a URL for the API"
        )
    return builder
}