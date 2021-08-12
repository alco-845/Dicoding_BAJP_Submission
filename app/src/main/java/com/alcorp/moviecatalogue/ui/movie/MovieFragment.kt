package com.alcorp.moviecatalogue.ui.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alcorp.moviecatalogue.R
import com.alcorp.moviecatalogue.viewmodel.ViewModelFactory
import com.alcorp.moviecatalogue.vo.Status
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var movieAdapter: MovieAdapter
    private lateinit var viewModel: MovieViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity != null){
            movieAdapter = MovieAdapter()

            val factory = ViewModelFactory.getInstance(requireActivity())
            viewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]

            viewModel.movie?.observe(viewLifecycleOwner, Observer { results ->
                if (results != null) {
                    when (results.status) {
                        Status.LOADING -> progBar_movie.visibility = View.VISIBLE
                        Status.SUCCESS -> {
                            progBar_movie.visibility = View.GONE
                            movieAdapter.submitList(results.data!!)
                            movieAdapter.notifyDataSetChanged()
                        }
                        Status.ERROR -> {
                            progBar_movie.visibility = View.GONE
                            Toast.makeText(context, resources.getString(R.string.txt_error), Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                with(rec_movie) {
                    layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                    setHasFixedSize(true)
                    adapter = movieAdapter
                }
            })
        }

        refresh_movie.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        viewModel.getListMovie().observe(viewLifecycleOwner, Observer { results ->
            if (results != null) {
                when (results.status) {
                    Status.LOADING -> progBar_movie.visibility = View.VISIBLE
                    Status.SUCCESS -> {
                        progBar_movie.visibility = View.GONE
                        movieAdapter.submitList(results.data!!)
                        movieAdapter.notifyDataSetChanged()
                    }
                    Status.ERROR -> {
                        progBar_movie.visibility = View.GONE
                        Toast.makeText(context, resources.getString(R.string.txt_error), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            with(rec_movie) {
                layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
                adapter = movieAdapter
            }
        })
        refresh_movie.isRefreshing = false
    }
}