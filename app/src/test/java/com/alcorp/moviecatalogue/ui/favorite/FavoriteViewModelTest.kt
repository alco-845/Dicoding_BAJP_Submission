package com.alcorp.moviecatalogue.ui.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.alcorp.moviecatalogue.data.source.AppRepository
import com.alcorp.moviecatalogue.data.source.local.entity.MovieEntity
import com.alcorp.moviecatalogue.data.source.local.entity.TvShowEntity
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FavoriteViewModelTest {

    private lateinit var viewModel: FavoriteViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var appRepository: AppRepository

    @Mock
    private lateinit var movieObserver: Observer<PagedList<MovieEntity>>

    @Mock
    private lateinit var tvObserver: Observer<PagedList<TvShowEntity>>

    @Mock
    private lateinit var moviePagedList: PagedList<MovieEntity>

    @Mock
    private lateinit var tvPagedList: PagedList<TvShowEntity>

    @Before
    fun setUp(){
        viewModel = FavoriteViewModel(appRepository)
    }

    @Test
    fun getFavoriteMovie() {
        val dummyMovie = moviePagedList
        `when`(dummyMovie.size).thenReturn(5)
        val movie = MutableLiveData<PagedList<MovieEntity>>()
        movie.value = dummyMovie

        `when`(appRepository.getFavoriteMovie()).thenReturn(movie)
        val movieEntities = viewModel.getFavoriteMovie().value
        verify(appRepository).getFavoriteMovie()
        Assert.assertNotNull(movieEntities)
        Assert.assertEquals(5, movieEntities?.size)

        viewModel.getFavoriteMovie().observeForever(movieObserver)
        verify(movieObserver).onChanged(dummyMovie)
    }

    @Test
    fun getFavoriteTv() {
        val dummyTv = tvPagedList
        `when`(dummyTv.size).thenReturn(5)
        val tv = MutableLiveData<PagedList<TvShowEntity>>()
        tv.value = dummyTv

        `when`(appRepository.getFavoriteTv()).thenReturn(tv)
        val tvEntity = viewModel.getFavoriteTv().value
        verify(appRepository).getFavoriteTv()
        Assert.assertNotNull(tvEntity)
        Assert.assertEquals(5, tvEntity?.size)

        viewModel.getFavoriteTv().observeForever(tvObserver)
        verify(tvObserver).onChanged(dummyTv)
    }
}