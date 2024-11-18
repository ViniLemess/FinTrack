package br.com.vinilemess.fintrack.error

import kotlinx.serialization.Serializable

@Serializable
data class ProblemDetail(
    val title: String,
    val status: Int,
    val detail: String,
    val type: String = "about:blank",
    var instance: String = "",
    val properties: Map<String, String> = emptyMap()
)