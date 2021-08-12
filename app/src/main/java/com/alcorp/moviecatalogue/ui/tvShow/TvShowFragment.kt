package com.alcorp.moviecatalogue.ui.tvShow

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
import kotlinx.android.synthetic.main.fragment_tv_show.*

class TvShowFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var tvAdapter: TvShowAdapter
    private lateinit var viewModel: TvShowViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tv_show, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity != null){
            tvAdapter = TvShowAdapter()

            val factory = ViewModelFactory.getInstance(requireActivity())
            viewModel = ViewModelProvider(this, factory)[TvShowViewModel::class.java]

            viewModel.tv?.observe(viewLifecycleOwner, Observer { results ->
                if (results != null) {
                    when (results.status) {
                        Status.LOADING -> progBar_tv.visibility = View.VISIBLE
                        Status.SUCCESS -> {
                            progBar_tv.visibility = View.GONE
                            tvAdapter.submitList(results.data!!)
                            tvAdapter.notifyDataSetChanged()
                        }
                        Status.ERROR -> {
                            progBar_tv.visibility = View.GONE
                            Toast.makeText(context, resources.getString(R.string.txt_error), Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                with(rec_tvshow) {
                    layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                    setHasFixedSize(true)
                    adapter = tvAdapter
                }
            })
        }

        refresh_tv.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        viewModel.getListTv().observe(viewLifecycleOwner, Observer { results ->
            if (results != null) {
                when (results.status) {
                    Status.LOADING -> progBar_tv.visibility = View.VISIBLE
                    Status.SUCCESS -> {
                        progBar_tv.visibility = View.GONE
                        tvAdapter.submitList(results.data!!)
                        tvAdapter.notifyDataSetChanged()
                    }
                    Status.ERROR -> {
                        progBar_tv.visibility = View.GONE
                        Toast.makeText(context, resources.getString(R.string.txt_error), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            with(rec_tvshow) {
                layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
                adapter = tvAdapter
            }
        })
        refresh_tv.isRefreshing = false
    }
}