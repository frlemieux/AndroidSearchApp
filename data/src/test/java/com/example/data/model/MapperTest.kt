package com.example.data.model

import com.example.data.remote.model.MandatoryFieldException
import com.example.data.remote.model.RepoData
import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MapperTest {
    @Test
    fun testToEntity_withValidData() {
        val repoData =
            RepoData(
                id = 1,
                name = "RepoName",
                fullName = "FullRepoName",
                description = "A sample description",
                url = "http://github.com/repo",
                stars = 100,
                forks = 50,
                language = "Kotlin",
            )

        val repoEntity = repoData.toEntity()

        assertEquals(1, repoEntity.id)
        assertEquals("RepoName", repoEntity.name)
        assertEquals("FullRepoName", repoEntity.fullName)
        assertEquals("A sample description", repoEntity.description)
        assertEquals("http://github.com/repo", repoEntity.url)
        assertEquals(100, repoEntity.stars)
        assertEquals(50, repoEntity.forks)
        assertEquals("Kotlin", repoEntity.language)
    }

    @Test
    fun testToEntity_withNullId_throwsException() {
        val repoData =
            RepoData(
                id = null,
                name = "RepoName",
                fullName = "FullRepoName",
                description = "A sample description",
                url = "http://github.com/repo",
                stars = 100,
                forks = 50,
                language = "Kotlin",
            )

        assertThrows(MandatoryFieldException::class.java) {
            repoData.toEntity()
        }
    }

    @Test
    fun testToEntity_withNullName_throwsException() {
        val repoData =
            RepoData(
                id = 1,
                name = null,
                fullName = "FullRepoName",
                description = "A sample description",
                url = "http://github.com/repo",
                stars = 100,
                forks = 50,
                language = "Kotlin",
            )

        assertThrows(MandatoryFieldException::class.java) {
            repoData.toEntity()
        }
    }

    @Test
    fun testToEntity_withNullFullName_defaultsToEmptyString() {
        val repoData =
            RepoData(
                id = 1,
                name = "RepoName",
                fullName = null,
                description = "A sample description",
                url = "http://github.com/repo",
                stars = 100,
                forks = 50,
                language = "Kotlin",
            )

        val repoEntity = repoData.toEntity()

        assertEquals("", repoEntity.fullName)
    }
}
