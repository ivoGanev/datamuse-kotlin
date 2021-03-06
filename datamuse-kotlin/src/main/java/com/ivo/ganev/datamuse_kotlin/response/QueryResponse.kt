/**
 * Copyright (C) 2020 Ivo Ganev
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
package com.ivo.ganev.datamuse_kotlin.response

/**
 * [QueryResponse] returns either a [RemoteFailure] or [Result].
 * */
sealed class QueryResponse<out F : RemoteFailure, out R> {
    data class Failure<out F : RemoteFailure>(val failure: F) : QueryResponse<F, Nothing>()
    data class Result<out R>(val result: R) : QueryResponse<Nothing, R>()

    /**
     * Creates a Failure
     * */
    fun <F : RemoteFailure> failure(failure: F) = Failure(failure)

    /**
     * Creates a Result
     * */
    fun <R> result(result: R) = Result(result)

    val isFailure get() = this is Failure<F>
    val isResult get() = this is Result<R>

    /**
     * Applies fnF if this is a Failure or fnR if this is a Result.
     * */
    fun applyEither(fnF: (F) -> Any, fnR: (R) -> Any): Any =
        when (this) {
            is Failure -> fnF(failure)
            is Result -> fnR(result)
        }
}

