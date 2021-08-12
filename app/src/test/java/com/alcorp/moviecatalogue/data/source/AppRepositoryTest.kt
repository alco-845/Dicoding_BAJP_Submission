package com.alcorp.moviecatalogue.data.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.DataSource
import com.alcorp.moviecatalogue.data.source.local.LocalDataSource
import com.alcorp.moviecatalogue.data.source.local.entity.MovieEntity
import com.alcorp.moviecatalogue.data.source.local.entity.TvShowEntity
import com.alcorp.moviecatalogue.data.source.remote.RemoteDataSource
import com.alcorp.moviecatalogue.utils.*
import com.alcorp.moviecatalogue.vo.Resource
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

class AppRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private val local = mock(LocalDataSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)

    private val remote = mock(RemoteDataSource::class.java)
    private val appRepository = FakeAppRepository(remote, local, appExecutors)

    private val movieData = DataDummy.generateDummyMovie()
    private val movieId = movieData[0].id
    private val tvData = DataDummy.generateDummyTvShow()
    private val tvId = tvData[9].id

    @Test
    fun getListMovie() {
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, MovieEntity>
        `when`(local.getListMovie()).thenReturn(dataSourceFactory)
        appRepository.getListMovie()

        val movieEntity = Resource.success(PagedListUtil.mockPagedList(DataDummy.generateDummyMovie()))
        verify(local).getListMovie()
        assertNotNull(movieEntity)
        assertEquals(movieData.size.toLong(), movieEntity.data?.size?.toLong())
    }

    @Test
    fun getFavoriteMovie(){
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, MovieEntity>
        `when`(local.getFavoriteMovie()).thenReturn(dataSourceFactory)
        appRepository.getFavoriteMovie()

        val movieEntity = Resource.success(PagedListUtil.mockPagedList(DataDummy.generateDummyMovie()))
        verify(local).getFavoriteMovie()
        assertNotNull(movieEntity)
        assertEquals(movieData.size.toLong(), movieEntity.data?.size?.toLong())
    }

    @Test
    fun getListTv() {
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, TvShowEntity>
        `when`(local.getListTv()).thenReturn(dataSourceFactory)
        appRepository.getListTv()

        val tvEntity = Resource.success(PagedListUtil.mockPagedList(DataDummy.generateDummyTvShow()))
        verify(local).getListTv()
        assertNotNull(tvEntity)
        assertEquals(tvData.size.toLong(), tvEntity.data?.size?.toLong())
    }

    @Test
    fun getFavoriteTv(){
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, TvShowEntity>
        `when`(local.getFavoriteTv()).thenReturn(dataSourceFactory)
        appRepository.getFavoriteTv()

        val tvEntity = Resource.success(PagedListUtil.mockPagedList(DataDummy.generateDummyTvShow()))
        verify(local).getFavoriteTv()
        assertNotNull(tvEntity)
        assertEquals(tvData.size.toLong(), tvEntity.data?.size?.toLong())
    }
}