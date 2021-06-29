package com.teamnoyes.github_repository

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isGone
import com.teamnoyes.github_repository.DetailRepositoryActivity.Companion.REPOSITORY_NAME_KEY
import com.teamnoyes.github_repository.DetailRepositoryActivity.Companion.REPOSITORY_OWNER_KEY
import com.teamnoyes.github_repository.adapter.RepositoryRecyclerAdapter
import com.teamnoyes.github_repository.data.database.DataBaseProvider
import com.teamnoyes.github_repository.data.entity.GithubRepoEntity
import com.teamnoyes.github_repository.databinding.ActivityRepositoryBinding
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RepositoryActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivityRepositoryBinding

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val repositoryDao by lazy {
        DataBaseProvider.provideDB(applicationContext).repositoryDao()
    }

    private lateinit var adapter: RepositoryRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepositoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        initView()

    }

    private fun initAdapter() {
        adapter = RepositoryRecyclerAdapter {
            startActivity(
                Intent(this, DetailRepositoryActivity::class.java).apply {
                    putExtra(REPOSITORY_NAME_KEY, it.name)
                    putExtra(REPOSITORY_OWNER_KEY, it.owner.login)
                }
            )
        }
    }

    private fun initView() = with(binding) {
        searchButton.setOnClickListener {
            startActivity(Intent(this@RepositoryActivity, SearchActivity::class.java))
        }
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        launch(coroutineContext) {
            loadLikedRepositoryList()
        }
    }

    private suspend fun loadLikedRepositoryList() = withContext(Dispatchers.IO) {
        val repoList =
            DataBaseProvider.provideDB(this@RepositoryActivity).repositoryDao().getHistory()
        withContext(Dispatchers.Main) {
            setData(repoList)
        }
    }

    private fun setData(repoList: List<GithubRepoEntity>) = with(binding){
        if (repoList.isEmpty()) {
            emptyResultTextView.isGone = false
            recyclerView.isGone = true
        } else {
            emptyResultTextView.isGone = true
            recyclerView.isGone = false
            adapter.submitList(repoList)
        }
    }

}