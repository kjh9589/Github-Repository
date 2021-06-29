package com.teamnoyes.github_repository

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isGone
import com.teamnoyes.github_repository.DetailRepositoryActivity.Companion.REPOSITORY_NAME_KEY
import com.teamnoyes.github_repository.DetailRepositoryActivity.Companion.REPOSITORY_OWNER_KEY
import com.teamnoyes.github_repository.adapter.RepositoryRecyclerAdapter
import com.teamnoyes.github_repository.data.entity.GithubRepoEntity
import com.teamnoyes.github_repository.databinding.ActivitySearchBinding
import com.teamnoyes.github_repository.utils.RetrofitUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SearchActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivitySearchBinding

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var adapter: RepositoryRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        initViews()
        bindViews()
    }

    private fun initAdapter() {
        adapter = RepositoryRecyclerAdapter {
            startActivity(Intent(this, DetailRepositoryActivity::class.java).apply {
                putExtra(REPOSITORY_OWNER_KEY, it.owner.login)
                putExtra(REPOSITORY_NAME_KEY, it.name)
            })
        }
    }

    private fun initViews() = with(binding) {
        emptyResultTextView.isGone = true
        recyclerView.adapter = adapter
    }

    private fun bindViews() = with(binding) {
        searchButton.setOnClickListener{
            searchKeyword(searchBarInputView.text.toString())
        }
    }

    private fun searchKeyword(keyword: String) = launch {
        withContext(Dispatchers.IO) {
            val response = RetrofitUtil.githubApiService.searchRepositories(keyword)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    response.body()?.let {
                        setData(it.items)
                    }
                }
            }
        }
    }

    private fun setData(items: List<GithubRepoEntity>) {
        adapter.submitList(items)
    }
}