package com.example.data.remote

data class GitHubErrorDetail(
    val resource: String,
    val field: String,
    val code: String,
) {
    fun errorMessage(): String = "resource: $resource, field: $field, code: $code"
}
