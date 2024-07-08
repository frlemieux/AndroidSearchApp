package com.example.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.RepoEntity

@Dao
interface RepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<RepoEntity>)

    @Query(
        "SELECT * FROM repos",
    )
    fun repoEntityPagingSource(): PagingSource<Int, RepoEntity>

    @Query("DELETE FROM repos")
    suspend fun clearRepos()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'repos'")
    suspend fun resetPrimaryKey()
}
