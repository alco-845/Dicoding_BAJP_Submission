package com.alcorp.moviecatalogue.data.source

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.alcorp.moviecatalogue.data.source.local.entity.MovieEntity
import com.alcorp.moviecatalogue.data.source.local.entity.MovieItem
import com.alcorp.moviecatalogue.data.source.local.entity.TvShowEntity
import com.alcorp.moviecatalogue.data.source.local.entity.TvShowItem
import com.alcorp.moviecatalogue.data.source.remote.response.MovieDetail
import com.alcorp.moviecatalogue.data.source.remote.response.TvDetail
import com.alcorp.moviecatalogue.vo.Resource

interface AppDataSource {

    fun getListMovie() : LiveData<Resource<PagedList<MovieEntity>>>

    fun getMovieById(id: String) : LiveData<Resource<MovieItem>>

    fun getFavoriteMovie() : LiveData<PagedList<MovieEntity>>

    fun getMovie(id: String) : LiveData<Resource<MovieDetail>>

    fun getListTv() : LiveData<Resource<PagedList<TvShowEntity>>>

    fun getTvById(id: String) : LiveData<Resource<TvShowItem>>

    fun getFavoriteTv() : LiveData<PagedList<TvShowEntity>>

    fun getTv(id: String) : LiveData<Resource<TvDetail>>

    fun setMovieFavorite(movie: MovieEntity, state: Boolean)

    fun setTvFavorite(tv: TvShowEntity, state: Boolean)
}