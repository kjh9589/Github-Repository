package com.teamnoyes.github_repository.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamnoyes.github_repository.data.entity.GithubRepoEntity
import com.teamnoyes.github_repository.databinding.ItemRepositoryBinding
import com.teamnoyes.github_repository.extensions.loadCenterInside

class RepositoryRecyclerAdapter(private val onItemClicked: (GithubRepoEntity) -> Unit) :
    ListAdapter<GithubRepoEntity, RepositoryRecyclerAdapter.ViewHolder>(diffUtil) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<GithubRepoEntity>() {
            override fun areItemsTheSame(
                oldItem: GithubRepoEntity,
                newItem: GithubRepoEntity
            ): Boolean {
                return oldItem.fullName == newItem.fullName
            }

            override fun areContentsTheSame(
                oldItem: GithubRepoEntity,
                newItem: GithubRepoEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolder(private val binding: ItemRepositoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(githubRepoEntity: GithubRepoEntity) = with(binding) {
            ownerProfileImageView.loadCenterInside(githubRepoEntity.owner.avatarUrl, 24f)
            ownerNameTextView.text = githubRepoEntity.owner.login
            nameTextView.text = githubRepoEntity.fullName
            subtextTextView.text = githubRepoEntity.description
            stargazersCountText.text = githubRepoEntity.stargazersCount.toString()
            githubRepoEntity.language?.let { language ->
                languageText.isGone = false
                languageText.text = language
            } ?: kotlin.run {
                languageText.isGone = true
                languageText.text = ""
            }

            root.setOnClickListener {
                onItemClicked(githubRepoEntity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRepositoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}