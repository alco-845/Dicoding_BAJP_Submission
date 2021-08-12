package com.alcorp.moviecatalogue.ui.detail

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alcorp.moviecatalogue.R
import com.alcorp.moviecatalogue.data.source.local.entity.MovieEntity
import com.alcorp.moviecatalogue.data.source.local.entity.TvShowEntity
import com.alcorp.moviecatalogue.viewmodel.ViewModelFactory
import com.alcorp.moviecatalogue.vo.Status
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_TYPE = "extra_type"
    }

    private lateinit var viewModel: DetailViewModel

    private lateinit var id: String
    private lateinit var title: String
    private lateinit var type: String

    private lateinit var prefFav: String
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setToolbar()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        val extras = intent.extras
        if (extras != null) {
            id = extras.getString(EXTRA_ID).toString()
            title = extras.getString(EXTRA_TITLE).toString()
            type = extras.getString(EXTRA_TYPE).toString()

            sharedPreferences = this.getSharedPreferences("fav", Context.MODE_PRIVATE)
            prefFav = sharedPreferences.getString(title + "fav", "unfav").toString()

            if (prefFav.equals("fav")) {
                Glide.with(this@DetailActivity)
                        .load(resources.getDrawable(R.drawable.ic_baseline_favorite_24))
                        .into(btn_fav)
            } else {
                Glide.with(this@DetailActivity)
                        .load(resources.getDrawable(R.drawable.ic_baseline_favorite_border_24))
                        .into(btn_fav)
            }

            if (id != null) {
                viewModel.setSelected(id)

                if (type == resources.getString(R.string.movie)) {
                    viewModel.movieItem.observe(this, Observer { movieItem ->
                        when (movieItem.status) {
                            Status.LOADING -> progBar_detail.visibility = View.VISIBLE
                            Status.SUCCESS -> if (movieItem.data != null) {
                                progBar_detail.visibility = View.GONE
                                setMovie(movieItem.data.mMovie)
                            }
                            Status.ERROR -> {
                                progBar_detail.visibility = View.GONE
                                Toast.makeText(this, resources.getString(R.string.txt_error), Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                } else {
                    viewModel.tvItem.observe(this, Observer { tvItem ->
                        when (tvItem.status) {
                            Status.LOADING -> progBar_detail.visibility = View.VISIBLE
                            Status.SUCCESS -> if (tvItem.data != null) {
                                progBar_detail.visibility = View.GONE
                                setTvShow(tvItem.data.mTv)
                            }
                            Status.ERROR -> {
                                progBar_detail.visibility = View.GONE
                                Toast.makeText(this, resources.getString(R.string.txt_error), Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }
            }
        }

        refresh_detail.setOnRefreshListener(this)
        btn_fav.setOnClickListener(this)
    }

    override fun onRefresh() {
        if (type == resources.getString(R.string.movie)) {
            viewModel.movieItem.observe(this, Observer { movieItem ->
                when (movieItem.status) {
                    Status.LOADING -> progBar_detail.visibility = View.VISIBLE
                    Status.SUCCESS -> if (movieItem.data != null) {
                        progBar_detail.visibility = View.GONE
                        setMovie(movieItem.data.mMovie)
                    }
                    Status.ERROR -> {
                        progBar_detail.visibility = View.GONE
                        Toast.makeText(this, resources.getString(R.string.txt_error), Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } else {
            viewModel.tvItem.observe(this, Observer { tvItem ->
                when (tvItem.status) {
                    Status.LOADING -> progBar_detail.visibility = View.VISIBLE
                    Status.SUCCESS -> if (tvItem.data != null) {
                        progBar_detail.visibility = View.GONE
                        setTvShow(tvItem.data.mTv)
                    }
                    Status.ERROR -> {
                        progBar_detail.visibility = View.GONE
                        Toast.makeText(this, resources.getString(R.string.txt_error), Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
        refresh_detail.isRefreshing = false
    }

    private fun setToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.txt_detail)
        supportActionBar?.elevation = 0F
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        menu.findItem(R.id.menu_fav).isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.menu_share) {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            share.putExtra(Intent.EXTRA_TEXT, "Hey, I recommended you a $type named $title")
            startActivity(Intent.createChooser(share, resources.getString(R.string.txt_share)))
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setMovie(movieEntity: MovieEntity) {
        tvTitle.text = movieEntity.title
        tvYear.text = movieEntity.release_date
        tvOview.text = movieEntity.overview

        Glide.with(this)
                .load(resources.getString(R.string.txt_image_url) + movieEntity.poster_path)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .override(250, 350)
                        .error(R.drawable.ic_error))
                .into(img_poster)
    }

    private fun setTvShow(tvShowEntity: TvShowEntity) {
        tvTitle.text = tvShowEntity.name
        tvYear.text = tvShowEntity.first_air_date
        tvOview.text = tvShowEntity.overview

        Glide.with(this)
                .load(resources.getString(R.string.txt_image_url) + tvShowEntity.poster_path)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .override(250, 350)
                        .error(R.drawable.ic_error))
                .into(img_poster)
    }

    private fun setFavoriteState(state: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        if (state.equals("fav")) {
            Glide.with(this@DetailActivity)
                    .load(resources.getDrawable(R.drawable.ic_baseline_favorite_24))
                    .into(btn_fav)
            editor.putString(title + "fav", "fav")
            Toast.makeText(this, resources.getString(R.string.txt_favorited), Toast.LENGTH_SHORT).show()
        } else {
            Glide.with(this@DetailActivity)
                    .load(resources.getDrawable(R.drawable.ic_baseline_favorite_border_24))
                    .into(btn_fav)
            editor.putString(title + "fav", "unfav")
            Toast.makeText(this, resources.getString(R.string.txt_unfavorite), Toast.LENGTH_SHORT).show()
        }
        editor.apply()
        editor.commit()
        onRefresh()
    }

    override fun onClick(view: View) {
        if (type == resources.getString(R.string.movie)) {
            if (prefFav.equals("fav")) {
                setFavoriteState("unfav")
                viewModel.setFavoriteMovie(false)
            } else {
                setFavoriteState("fav")
                viewModel.setFavoriteMovie(true)
            }
        } else {
            if (prefFav.equals("fav")) {
                setFavoriteState("unfav")
                viewModel.setFavoriteTv(false)
            } else {
                setFavoriteState("fav")
                viewModel.setFavoriteTv(true)
            }
        }
    }
}