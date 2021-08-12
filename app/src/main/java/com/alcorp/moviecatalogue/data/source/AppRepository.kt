package com.alcorp.moviecatalogue.data.source

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.alcorp.moviecatalogue.data.source.local.LocalDataSource
import com.alcorp.moviecatalogue.data.source.local.entity.MovieEntity
import com.alcorp.moviecatalogue.data.source.local.entity.MovieItem
import com.alcorp.moviecatalogue.data.source.local.entity.TvShowEntity
import com.alcorp.moviecatalogue.data.source.local.entity.TvShowItem
import com.alcorp.moviecatalogue.data.source.remote.ApiResponse
import com.alcorp.moviecatalogue.data.source.remote.RemoteDataSource
import com.alcorp.moviecatalogue.data.source.remote.response.*
import com.alcorp.moviecatalogue.utils.AppExecutors
import com.alcorp.moviecatalogue.vo.Resource

class AppRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors)
    : AppDataSource {

    companion object {
        @Volatile
        private var instance: AppRepository? = null

        fun getInstance(remoteData: RemoteDataSource, localDataSource: LocalDataSource, appExecutors: AppExecutors) : AppRepository =
                instance ?: synchronized(this) {
                    instance ?: AppRepository(remoteData, localDataSource, appExecutors)
                }
    }

    override fun getListMovie(): LiveData<Resource<PagedList<MovieEntity>>> {
        return object : NetworkBoundResource<PagedList<MovieEntity>, MovieResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<PagedList<MovieEntity>> {
                val config = PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(4)
                        .setPageSize(4)
                        .build()
                return LivePagedListBuilder(localDataSource.getListMovie(), config).build()
            }

            override fun shouldFetch(data: PagedList<MovieEntity>?): Boolean =
                data == null || data.isEmpty()

            override fun createCall(): LiveData<ApiResponse<MovieResponse>> =
                remoteDataSource.getListMovie()

            override fun saveCallResult(data: MovieResponse) {
                val movieList = ArrayList<MovieEntity>()
                for (response in data.results) {
                    val movie = MovieEntity(
                        response.id,
                        response.title,
                        response.overview,
                        false,
                        response.release_date,
                        response.poster_path)
                    movieList.add(movie)
                }

                localDataSource.insertMovie(movieList)
            }

        }.asLiveData()
    }

    override fun getMovieById(id: String): LiveData<Resource<MovieItem>> {
        return object : NetworkBoundResource<MovieItem, MovieResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<MovieItem> =
                    localDataSource.getMovieById(id)

            override fun shouldFetch(data: MovieItem?): Boolean =
                    data?.mMovie == null

            override fun createCall(): LiveData<ApiResponse<MovieResponse>> =
                    remoteDataSource.getMovie(id)

            override fun saveCallResult(data: MovieResponse) {
                val movieList = ArrayList<MovieEntity>()
                val movie = MovieEntity(
                        data.results[0].id,
                        data.results[2].title,
                        data.results[3].overview,
                        false,
                        data.results[4].release_date,
                        data.results[1].poster_path)
                movieList.add(movie)

                localDataSource.insertMovie(movieList)
            }

        }.asLiveData()
    }

    override fun getFavoriteMovie(): LiveData<PagedList<MovieEntity>> {
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(4)
                .setPageSize(4)
                .build()
        return LivePagedListBuilder(localDataSource.getFavoriteMovie(), config).build()
    }

    override fun getMovie(id: String): LiveData<Resource<MovieDetail>> {
        return object : NetworkBoundResource<MovieDetail, MovieResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<MovieDetail> =
                    localDataSource.getMovie(id)


            override fun shouldFetch(data: MovieDetail?): Boolean =
                    data == null

            override fun createCall(): LiveData<ApiResponse<MovieResponse>> =
                    remoteDataSource.getMovie(id)

            override fun saveCallResult(data: MovieResponse) {
                val movieList = ArrayList<MovieEntity>()
                val movie = MovieEntity(
                        data.results[0].id,
                        data.results[2].title,
                        data.results[3].overview,
                        false,
                        data.results[4].release_date,
                        data.results[1].poster_path)
                movieList.add(movie)

                localDataSource.insertMovie(movieList)
            }

        }.asLiveData()
    }

    override fun getListTv(): LiveData<Resource<PagedList<TvShowEntity>>> {
        return object : NetworkBoundResource<PagedList<TvShowEntity>, TvResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<PagedList<TvShowEntity>> {
                val config = PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(4)
                        .setPageSize(4)
                        .build()
                return LivePagedListBuilder(localDataSource.getListTv(), config).build()
            }


            override fun shouldFetch(data: PagedList<TvShowEntity>?): Boolean =
                    data == null || data.isEmpty()

            override fun createCall(): LiveData<ApiResponse<TvResponse>> =
                    remoteDataSource.getListTv()

            override fun saveCallResult(data: TvResponse) {
                val tvList = ArrayList<TvShowEntity>()
                for (response in data.results) {
                    val tv = TvShowEntity(
                            response.id,
                            response.name,
                            response.overview,
                            false,
                            response.first_air_date,
                            response.poster_path)
                    tvList.add(tv)
                }

                localDataSource.insertTv(tvList)
            }

        }.asLiveData()
    }

    override fun getTvById(id: String): LiveData<Resource<TvShowItem>> {
        return object : NetworkBoundResource<TvShowItem, TvResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<TvShowItem> =
                localDataSource.getTvById(id)

            override fun shouldFetch(data: TvShowItem?): Boolean =
                data?.mTv == null

            override fun createCall(): LiveData<ApiResponse<TvResponse>> =
                remoteDataSource.getTv(id)

            override fun saveCallResult(data: TvResponse) {
                val movieList = ArrayList<MovieEntity>()
                val movie = MovieEntity(
                    data.results[0].id,
                    data.results[2].name,
                    data.results[3].overview,
                    false,
                    data.results[4].first_air_date,
                    data.results[1].poster_path)
                movieList.add(movie)

                localDataSource.insertMovie(movieList)
            }

        }.asLiveData()
    }

    override fun getFavoriteTv(): LiveData<PagedList<TvShowEntity>> {
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(4)
                .setPageSize(4)
                .build()
        return LivePagedListBuilder(localDataSource.getFavoriteTv(), config).build()
    }

    override fun getTv(id: String): LiveData<Resource<TvDetail>> {
        return object : NetworkBoundResource<TvDetail, TvResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<TvDetail> =
                    localDataSource.getTv(id)


            override fun shouldFetch(data: TvDetail?): Boolean =
                    data == null

            override fun createCall(): LiveData<ApiResponse<TvResponse>> =
                    remoteDataSource.getTv(id)

            override fun saveCallResult(data: TvResponse) {
                val movieList = ArrayList<TvShowEntity>()
                val tv = TvShowEntity(
                        data.results[0].id,
                        data.results[2].name,
                        data.results[3].overview,
                        false,
                        data.results[4].first_air_date,
                        data.results[1].poster_path)
                movieList.add(tv)

                localDataSource.insertTv(movieList)
            }

        }.asLiveData()
    }

    override fun setMovieFavorite(movie: MovieEntity, state: Boolean) {
        appExecutors.diskIO().execute { localDataSource.setFavoriteMovie(movie, state) }
    }

    override fun setTvFavorite(tv: TvShowEntity, state: Boolean) {
        appExecutors.diskIO().execute { localDataSource.setFavoriteTv(tv, state) }
    }
}