package com.alcorp.moviecatalogue.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.alcorp.moviecatalogue.data.source.AppRepository
import com.alcorp.moviecatalogue.data.source.local.entity.MovieEntity
import com.alcorp.moviecatalogue.data.source.local.entity.TvShowEntity

class FavoriteViewModel(private val appRepository: AppRepository) : ViewModel() {
    fun getFavoriteMovie(): LiveData<PagedList<MovieEntity>> = appRepository.getFavoriteMovie()

    fun setFavoriteMovie(movieEntity: MovieEntity, fav: Boolean) {
        appRepository.setMovieFavorite(movieEntity, fav)
    }

    fun getFavoriteTv(): LiveData<PagedList<TvShowEntity>> = appRepository.getFavoriteTv()

    fun setFavoriteTv(tvEntity: TvShowEntity, fav: Boolean) {
        appRepository.setTvFavorite(tvEntity, fav)
    }
}