package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.Repo

@Entity(tableName = "repos")
data class RepoEntity(
    @PrimaryKey(autoGenerate = true) val index: Int = 0,
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val url: String,
    val stars: Int,
    val forks: Int,
    val language: String?,
) {
    fun toDomain() =
        Repo(
            id = id,
            name = name,
            fullName = fullName,
            description = description,
            url = url,
            stars = stars,
            forks = forks,
            language = language,
        )
}
