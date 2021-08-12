package com.alcorp.moviecatalogue.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.alcorp.moviecatalogue.data.source.AppRepository
import com.alcorp.moviecatalogue.data.source.local.entity.MovieItem
import com.alcorp.moviecatalogue.data.source.local.entity.TvShowItem
import com.alcorp.moviecatalogue.utils.DataDummy
import com.alcorp.moviecatalogue.vo.Resource
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    private lateinit var viewModel: DetailViewModel
    private val movieData = DataDummy.generateDummyMovie()[0]
    private val movieId = movieData.id
    private val tvData = DataDummy.generateDummyTvShow()[9]
    private val tvId = tvData.id

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var appRepository: AppRepository

    @Mock
    private lateinit var movieObserver: Observer<Resource<MovieItem>>

    @Mock
    private lateinit var tvObserver: Observer<Resource<TvShowItem>>

    @Before
    fun setUp(){
        viewModel = DetailViewModel(appRepository)
    }

    @Test
    fun getMovie() {
        viewModel.setSelected(movieId)

        val dummyMovie = Resource.success(DataDummy.generateDummyMovie(movieData, true))
        val movie = MutableLiveData<Resource<MovieItem>>()
        movie.value = dummyMovie

        `when`(appRepository.getMovieById(movieId)).thenReturn(movie)

        viewModel.movieItem.observeForever(movieObserver)

        verify(movieObserver).onChanged(dummyMovie)
    }

    @Test
    fun getTvShow() {
        viewModel.setSelected(tvId)

        val dummyTv = Resource.success(DataDummy.generateDummyTvShow(tvData, true))
        val tv = MutableLiveData<Resource<TvShowItem>>()
        tv.value = dummyTv

        `when`(appRepository.getTvById(tvId)).thenReturn(tv)

        viewModel.tvItem.observeForever(tvObserver)

        verify(tvObserver).onChanged(dummyTv)
    }
}