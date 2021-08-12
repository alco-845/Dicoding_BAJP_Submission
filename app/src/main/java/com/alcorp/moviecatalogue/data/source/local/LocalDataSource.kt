package com.alcorp.moviecatalogue.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.alcorp.moviecatalogue.data.source.local.entity.MovieEntity
import com.alcorp.moviecatalogue.data.source.local.entity.MovieItem
import com.alcorp.moviecatalogue.data.source.local.entity.TvShowEntity
import com.alcorp.moviecatalogue.data.source.local.entity.TvShowItem
import com.alcorp.moviecatalogue.data.source.local.room.AppDao
import com.alcorp.moviecatalogue.data.source.remote.response.MovieDetail
import com.alcorp.moviecatalogue.data.source.remote.response.TvDetail

class LocalDataSource private constructor(private val mAppDao: AppDao) {

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(appDao: AppDao): LocalDataSource {
            if (INSTANCE == null) {
                INSTANCE = LocalDataSource(appDao)
            }
            return INSTANCE as LocalDataSource
        }
    }

    fun getListMovie(): DataSource.Factory<Int, MovieEntity> = mAppDao.getListMovie()

    fun getFavoriteMovie(): DataSource.Factory<Int, MovieEntity> =
        mAppDao.getFavoriteMovie()

    fun getMovie(id: String): LiveData<MovieDetail> =
        mAppDao.getMovie(id)

    fun getMovieById(id: String): LiveData<MovieItem> =
        mAppDao.getMovieById(id)

    fun insertMovie(movie: List<MovieEntity>) = mAppDao.insertMovie(movie)

    fun setFavoriteMovie(movie: MovieEntity, newState: Boolean) {
        movie.favorite = newState
        mAppDao.updateMovie(movie)
    }

    fun getListTv(): DataSource.Factory<Int, TvShowEntity> = mAppDao.getListTv()

    fun getFavoriteTv(): DataSource.Factory<Int, TvShowEntity> =
        mAppDao.getFavoriteTv()

    fun getTv(id: String): LiveData<TvDetail> =
        mAppDao.getTv(id)

    fun getTvById(id: String): LiveData<TvShowItem> =
        mAppDao.getTvById(id)

    fun insertTv(tv: List<TvShowEntity>) = mAppDao.insertTv(tv)

    fun setFavoriteTv(tv: TvShowEntity, newState: Boolean) {
        tv.favorite = newState
        mAppDao.updateTv(tv)
    }
}