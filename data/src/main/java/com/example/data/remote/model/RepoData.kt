package com.example.data.remote.model

import com.example.data.local.entity.RepoEntity
import com.google.gson.annotations.SerializedName

class RepoData(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("full_name") val fullName: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("html_url") val url: String?,
    @SerializedName("stargazers_count") val stars: Int?,
    @SerializedName("forks_count") val forks: Int?,
    @SerializedName("language") val language: String?,
) {
    fun toEntity(): RepoEntity =
        RepoEntity(
            id = id.consolidateValue("id"),
            name = name.consolidateValue("name"),
            fullName = fullName ?: "",
            description = description,
            url = url ?: "",
            stars = stars ?: 0,
            forks = forks ?: 0,
            language = language,
        )
}

fun <VALUE> VALUE?.consolidateValue(fieldName: String): VALUE {
    if (this == null) {
        throw MandatoryFieldException(fieldName)
    } else {
        return this
    }
}

class MandatoryFieldException(
    fieldName: String,
) : Exception("The object cannot be mapped because the field $fieldName is mandatory")
