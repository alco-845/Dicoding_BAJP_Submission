package com.alcorp.moviecatalogue.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alcorp.moviecatalogue.data.source.AppRepository
import com.alcorp.moviecatalogue.di.Injection
import com.alcorp.moviecatalogue.ui.detail.DetailViewModel
import com.alcorp.moviecatalogue.ui.favorite.FavoriteViewModel
import com.alcorp.moviecatalogue.ui.movie.MovieViewModel
import com.alcorp.moviecatalogue.ui.tvShow.TvShowViewModel

class ViewModelFactory private constructor(private val mAppRepository: AppRepository) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context) : ViewModelFactory =
                instance ?: synchronized(this) {
                    instance ?: ViewModelFactory(Injection.provideRepository(context))
                }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MovieViewModel::class.java) -> {
                return MovieViewModel(mAppRepository) as T
            }

            modelClass.isAssignableFrom(TvShowViewModel::class.java) -> {
                return TvShowViewModel(mAppRepository) as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                return DetailViewModel(mAppRepository) as T
            }

            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                return FavoriteViewModel(mAppRepository) as T
            }

            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}