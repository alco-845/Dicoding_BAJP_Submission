package com.alcorp.moviecatalogue.ui.tvShow

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.moviecatalogue.R
import com.alcorp.moviecatalogue.data.source.local.entity.TvShowEntity
import com.alcorp.moviecatalogue.ui.detail.DetailActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_list.view.*

class TvShowAdapter internal constructor() : PagedListAdapter<TvShowEntity, TvShowAdapter.TvShowViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TvShowEntity>() {
            override fun areItemsTheSame(oldItem: TvShowEntity, newItem: TvShowEntity): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: TvShowEntity, newItem: TvShowEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return TvShowViewHolder(view)
    }

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
        val tvShow = getItem(position) as TvShowEntity
        holder.bind(tvShow)
    }

    fun getSwipedData(swipedPosition: Int): TvShowEntity = getItem(swipedPosition) as TvShowEntity

    class TvShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tvShow: TvShowEntity){
            with(itemView){
                tvItemTitle.text = tvShow.name
                tvItemYear.text = tvShow.first_air_date

                Glide.with(context)
                        .load(resources.getString(R.string.txt_image_url) + tvShow.poster_path)
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_loading)
                                .override(120, 150)
                                .error(R.drawable.ic_error))
                        .into(img_poster)

                setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_ID, tvShow.id)
                    intent.putExtra(DetailActivity.EXTRA_TITLE, tvShow.name)
                    intent.putExtra(DetailActivity.EXTRA_TYPE, resources.getString(R.string.tvshow))
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}