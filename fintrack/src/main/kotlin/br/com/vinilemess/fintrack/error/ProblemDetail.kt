package br.com.vinilemess.fintrack.error

import kotlinx.serialization.Serializable

@Serializable
data class ProblemDetail(
    val title: String,
    val status: Int,
    val detail: String,
    var instance: String = "about:blank",
    val properties: Map<String, String> = emptyMap()
)