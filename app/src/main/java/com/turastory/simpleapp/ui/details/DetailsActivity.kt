package com.turastory.simpleapp.ui.details

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.turastory.simpleapp.R
import com.turastory.simpleapp.util.toast
import com.turastory.simpleapp.vo.Comment
import com.turastory.simpleapp.vo.Post
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity(), DetailsContract.View {
    private val presenter: DetailsContract.Presenter by lazy {
        DetailsPresenter()
    }

    private val commentAdapter: CommentAdapter = CommentAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val postId = intent.getIntExtra("postId", -1)

        if (postId != -1) {
            setupToolbar()

            presenter.setView(this)
            presenter.requestPostDetails(postId)
        } else {
            toast("Error occurred!")
            finish()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(details_toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_delete_post -> {
                presenter.requestDeletePost()
                true
            }
            R.id.menu_edit_post -> {
                presenter.editPost()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showConfirmDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete post")
            .setMessage("Are you sure want to delete the post?")
            .setPositiveButton("Yes") { _, _ ->
                presenter.deletePost()
            }
            .setNegativeButton("No") { _, _ -> }
            .show()
    }

    override fun showPostDetails(post: Post) {
        post_details_title.text = post.title
        post_details_body.text = post.body
    }

    override fun showComments(comments: List<Comment>) {
        comments_list.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(this@DetailsActivity)
            setHasFixedSize(true)

            commentAdapter.setComments(comments)
        }
    }

    override fun showLoadingPage() {
        loading_page.visibility = View.VISIBLE
    }

    override fun hideLoadingPage() {
        loading_page.visibility = View.INVISIBLE
    }

    override fun openEditPostView() {
        // TODO
    }

    override fun showDeletionComplete() {
        toast("Deleting post complete!")
        finish()
    }
}