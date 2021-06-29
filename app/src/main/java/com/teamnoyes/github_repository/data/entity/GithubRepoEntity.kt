package com.teamnoyes.github_repository.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

//@Embedded Entity 내부에서 객체 단위 저장

@Entity(tableName = "GithubRepository")
data class GithubRepoEntity(
    val name: String,
    @PrimaryKey val fullName: String,
    @Embedded val owner: GithubOwner,
    val description: String?,
    val language: String?,
    val updatedAt: String,
    val stargazersCount: Int
)