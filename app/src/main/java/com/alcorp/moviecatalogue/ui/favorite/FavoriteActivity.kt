package com.alcorp.moviecatalogue.ui.favorite

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alcorp.moviecatalogue.R
import com.alcorp.moviecatalogue.ui.movie.MovieAdapter
import com.alcorp.moviecatalogue.ui.tvShow.TvShowAdapter
import com.alcorp.moviecatalogue.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        const val EXTRA_TYPE = "extra_type"
    }

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var tvAdapter: TvShowAdapter

    private lateinit var type: String
    private lateinit var prefFav: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        itemTouchHelper.attachToRecyclerView(rec_fav)

        val extras = intent.extras
        if (extras != null) {
            type = extras.getString(EXTRA_TYPE).toString()

            val factory = ViewModelFactory.getInstance(this)
            viewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

            if (type == resources.getString(R.string.movie)) {
                movieAdapter = MovieAdapter()
                progBar_fav.visibility = View.VISIBLE
                viewModel.getFavoriteMovie().observe(this, Observer { movie ->
                    progBar_fav.visibility = View.GONE
                    movieAdapter.submitList(movie)
                    movieAdapter.notifyDataSetChanged()
                })

                rec_fav.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                rec_fav.setHasFixedSize(true)
                rec_fav.adapter = movieAdapter
            } else {
                tvAdapter = TvShowAdapter()
                progBar_fav.visibility = View.VISIBLE
                viewModel.getFavoriteTv().observe(this, Observer { tv ->
                    progBar_fav.visibility = View.GONE
                    tvAdapter.submitList(tv)
                    tvAdapter.notifyDataSetChanged()
                })

                rec_fav.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                rec_fav.setHasFixedSize(true)
                rec_fav.adapter = tvAdapter
            }
        }

        setToolbar()
        refresh_fav.setOnRefreshListener(this)
    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int =
            makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val sharedPreferences = getSharedPreferences("fav", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            if (type == resources.getString(R.string.movie)) {
                val swipedPosition = viewHolder.adapterPosition
                val movieEntity = movieAdapter.getSwipedData(swipedPosition)
                viewModel.setFavoriteMovie(movieEntity, false)

                prefFav = sharedPreferences.getString(movieEntity.title + "fav", "unfav").toString()
                editor.putString(movieEntity.title + "fav", "unfav")
            } else {
                val swipedPosition = viewHolder.adapterPosition
                val tvEntity = tvAdapter.getSwipedData(swipedPosition)
                viewModel.setFavoriteTv(tvEntity, false)

                prefFav = sharedPreferences.getString(tvEntity.name + "fav", "unfav").toString()
                editor.putString(tvEntity.name + "fav", "unfav")
            }

            editor.apply()
            editor.commit()
            Toast.makeText(this@FavoriteActivity, resources.getString(R.string.txt_unfavorite), Toast.LENGTH_SHORT).show()
        }
    })

    private fun setToolbar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = type + " " + resources.getString(R.string.txt_fav)
        supportActionBar?.elevation = 0F
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRefresh() {
        if (type == resources.getString(R.string.movie)) {
            movieAdapter = MovieAdapter()
            progBar_fav.visibility = View.VISIBLE
            viewModel.getFavoriteMovie().observe(this, Observer { movie ->
                progBar_fav.visibility = View.GONE
                movieAdapter.submitList(movie)
                movieAdapter.notifyDataSetChanged()
            })

            rec_fav.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            rec_fav.setHasFixedSize(true)
            rec_fav.adapter = movieAdapter
        } else {
            tvAdapter = TvShowAdapter()
            progBar_fav.visibility = View.VISIBLE
            viewModel.getFavoriteTv().observe(this, Observer { tv ->
                progBar_fav.visibility = View.GONE
                tvAdapter.submitList(tv)
                tvAdapter.notifyDataSetChanged()
            })

            rec_fav.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            rec_fav.setHasFixedSize(true)
            rec_fav.adapter = tvAdapter
        }

        refresh_fav.isRefreshing = false
    }
}