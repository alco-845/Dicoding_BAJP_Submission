package com.alcorp.moviecatalogue.ui.tvShow

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.alcorp.moviecatalogue.data.source.local.entity.TvShowEntity
import com.alcorp.moviecatalogue.data.source.AppRepository
import com.alcorp.moviecatalogue.vo.Resource

class TvShowViewModel(private val appRepository: AppRepository) : ViewModel() {

    var tv: LiveData<Resource<PagedList<TvShowEntity>>>? = null
        get() {
            if (field == null) {
                field = getListTv()
            }
            return field
        }
        private set

    fun getListTv(): LiveData<Resource<PagedList<TvShowEntity>>> = appRepository.getListTv()
}