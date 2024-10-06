package br.com.vinilemess.fintrack

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.net.URI

@Serializable
data class ProblemDetail(
    val title: String,
    val status: Int,
    val detail: String,
    @Contextual
    val instance: URI,
    val properties: Map<String, String> = emptyMap()
) {
}