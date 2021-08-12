package com.alcorp.moviecatalogue.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.alcorp.moviecatalogue.data.source.AppRepository
import com.alcorp.moviecatalogue.data.source.local.entity.MovieItem
import com.alcorp.moviecatalogue.data.source.local.entity.TvShowItem
import com.alcorp.moviecatalogue.vo.Resource

class DetailViewModel(private val appRepository: AppRepository) : ViewModel() {

    val id = MutableLiveData<String>()

    fun setSelected(id: String){
        this.id.value = id
    }
    
    var movieItem: LiveData<Resource<MovieItem>> = Transformations.switchMap(id) { movieId ->
        appRepository.getMovieById(movieId)
    }

    fun setFavoriteMovie(fav: Boolean) {
        val movieResource = movieItem.value
        if (movieResource != null) {
            val movieList = movieResource.data

            if (movieList != null) {
                val movieEntity = movieList.mMovie
                appRepository.setMovieFavorite(movieEntity, fav)
            }
        }
    }

    var tvItem: LiveData<Resource<TvShowItem>> = Transformations.switchMap(id) { tvId ->
        appRepository.getTvById(tvId)
    }

    fun setFavoriteTv(fav: Boolean) {
        val tvResource = tvItem.value
        if (tvResource != null) {
            val tvList = tvResource.data

            if (tvList != null) {
                val tvEntity = tvList.mTv
                appRepository.setTvFavorite(tvEntity, fav)
            }
        }
    }
}