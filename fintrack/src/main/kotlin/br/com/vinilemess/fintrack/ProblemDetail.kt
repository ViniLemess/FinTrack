package br.com.vinilemess.fintrack

import java.net.URI

data class ProblemDetail(
    val title: String,
    val status: Int,
    val detail: String,
    val instance: URI,
    val properties: Map<String, String>
) {
}