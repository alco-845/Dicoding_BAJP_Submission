package com.alcorp.moviecatalogue.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.alcorp.moviecatalogue.data.source.local.entity.MovieEntity
import com.alcorp.moviecatalogue.data.source.local.entity.MovieItem
import com.alcorp.moviecatalogue.data.source.local.entity.TvShowEntity
import com.alcorp.moviecatalogue.data.source.local.entity.TvShowItem
import com.alcorp.moviecatalogue.data.source.remote.response.MovieDetail
import com.alcorp.moviecatalogue.data.source.remote.response.TvDetail

@Dao
interface AppDao {
    @Query("SELECT * FROM movieentities")
    fun getListMovie(): DataSource.Factory<Int, MovieEntity>

    @Query("SELECT * FROM movieentities where favorite = 1")
    fun getFavoriteMovie(): DataSource.Factory<Int, MovieEntity>

    @Transaction
    @Query("SELECT * FROM movieentities WHERE id = :id")
    fun getMovie(id: String): LiveData<MovieDetail>

    @Transaction
    @Query("SELECT * FROM movieentities WHERE id = :id")
    fun getMovieById(id: String): LiveData<MovieItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(courses: List<MovieEntity>)

    @Update
    fun updateMovie(movie: MovieEntity)

    @Query("SELECT * FROM tventities")
    fun getListTv(): DataSource.Factory<Int, TvShowEntity>

    @Query("SELECT * FROM tventities where favorite = 1")
    fun getFavoriteTv(): DataSource.Factory<Int, TvShowEntity>

    @Transaction
    @Query("SELECT * FROM tventities WHERE id = :id")
    fun getTv(id: String): LiveData<TvDetail>

    @Transaction
    @Query("SELECT * FROM tventities WHERE id = :id")
    fun getTvById(id: String): LiveData<TvShowItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTv(courses: List<TvShowEntity>)

    @Update
    fun updateTv(tv: TvShowEntity)
}