package com.example.data.remote

data class GitHubErrorResponse(
    val message: String,
    val errors: List<GitHubErrorDetail>?,
    val documentation_url: String,
    val status: String,
) {
    fun errorMessage(): String =
        "http error: $status" + " ($message : \n${errors?.firstOrNull()
            ?.errorMessage()})"
}
