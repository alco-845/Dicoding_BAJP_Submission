package com.alcorp.moviecatalogue.ui.movie

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.alcorp.moviecatalogue.data.source.local.entity.MovieEntity
import com.alcorp.moviecatalogue.data.source.AppRepository
import com.alcorp.moviecatalogue.vo.Resource

class MovieViewModel(private val appRepository: AppRepository) : ViewModel() {

    var movie: LiveData<Resource<PagedList<MovieEntity>>>? = null
        get() {
            if (field == null) {
                field = getListMovie()
            }
            return field
        }
        private set

    fun getListMovie(): LiveData<Resource<PagedList<MovieEntity>>> = appRepository.getListMovie()
}