package com.ivo.ganev.datamusekotlin

import androidx.lifecycle.LiveData
import com.ivo.ganev.datamusekotlin.UiConstraintElement.RelatedWordsElement
import com.ivo.ganev.datamusekotlin.api.*
import com.ivo.ganev.datamusekotlin.api.HardConstraint.RelatedWords.Code.APPROXIMATE_RHYMES
import com.ivo.ganev.datamusekotlin.core.WordResponse
import com.ivo.ganev.datamusekotlin.core.WordsEndpointConfig
import java.util.*

abstract class DatamuseActivityData()  {
    /**
     *
     * */
    abstract fun getConstraint(): HardConstraint

    protected open fun getTopics(): String = ""

    protected open fun getLeftContext(): String = ""

    protected open fun getRightContext(): String = ""

    protected open fun getMaxResults(): Int = 100

    protected open fun getMetadata(): EnumSet<MetadataFlag>?  = null

    fun toUrlString(): String {
        return buildWordsEndpointUrl {
            hardConstraint = getConstraint()
            topics = getTopics()
            leftContext = getLeftContext()
            rightContext = getRightContext()
            maxResults = getMaxResults()
            metadata = getMetadata()
        }
    }
}

