package com.turastory.simpleapp.ui.main

import android.util.Log
import com.turastory.simpleapp.network.doOnSuccess
import com.turastory.simpleapp.network.postApi
import com.turastory.simpleapp.vo.Post

class MainPresenter : MainContract.Presenter {
    private lateinit var view: MainContract.View

    private val posts = mutableListOf<Post>()

    override fun setView(view: MainContract.View) {
        this.view = view

        view.showLoadingBar()
    }

    override fun requestNewPosts() {
        postApi()
            .getPosts(posts.size, MainContract.POST_LOAD_LIMIT)
            .doOnSuccess {
                it?.let { newPosts ->
                    if (newPosts.isNotEmpty()) {
                        posts += it
                        view.showNewPosts(it)
                    } else {
                        view.hideLoadingBar()
                    }
                }
            }
            .doOnFailed {
                Log.e(MainContract.TAG, "Error while loading posts - $it")
            }
            .done()
    }

    override fun onItemClick(pos: Int) {
        view.openDetailsView(posts[pos].id)
    }
}