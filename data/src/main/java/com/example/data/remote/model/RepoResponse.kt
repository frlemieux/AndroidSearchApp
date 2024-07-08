package com.example.data.remote.model

import com.google.gson.annotations.SerializedName

data class RepoResponse(
    @SerializedName("total_count") val total: Int = 0,
    @SerializedName("items") val items: List<RepoData> = emptyList(),
    val nextPage: Int? = null,
)
