package com.teamnoyes.github_repository.data.response

import com.teamnoyes.github_repository.data.entity.GithubRepoEntity

data class GithubRepoSearchResponse(
    val totalCount: Int,
    val items: List<GithubRepoEntity>
)