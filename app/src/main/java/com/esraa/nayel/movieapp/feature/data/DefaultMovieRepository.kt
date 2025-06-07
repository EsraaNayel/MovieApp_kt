import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.RoomDatabase
import com.esraa.nayel.movieapp.feature.data.DefaultMovieMediator
import com.esraa.nayel.movieapp.feature.data.cache.MovieCacheDataSource
import com.esraa.nayel.movieapp.feature.data.models.toMovie
import com.esraa.nayel.movieapp.feature.data.remote.MovieRemoteDataSource
import com.esraa.nayel.movieapp.feature.data.remote.NetworkErrorHandler
import com.esraa.nayel.movieapp.feature.data.SearchMoviesPagingSource
import com.esraa.nayel.movieapp.feature.domain.models.Movie
import com.esraa.nayel.movieapp.feature.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultMovieRepository(
    private val db: RoomDatabase,
    private val cacheDataSource: MovieCacheDataSource,
    private val remoteDataSource: MovieRemoteDataSource,
    private val networkErrorHandler: NetworkErrorHandler
) : MovieRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = DefaultMovieMediator(
                db,
                cacheDataSource,
                remoteDataSource,
                networkErrorHandler
            ),
            pagingSourceFactory = { cacheDataSource.getNowPlayingMovies() }
        ).flow.map { pagingData ->
            pagingData.map {
                it.toMovie()
            }
        }
    }

    override fun searchMovies(query: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { SearchMoviesPagingSource(remoteDataSource, query) }
        ).flow.map { pagingData ->
            pagingData.map {
                it.toMovie()
            }
        }
    }

    override suspend fun getMovie(id: Int): Result<Movie> {
        return networkErrorHandler.handle {
            remoteDataSource.getMovie(id).toMovie()
        }
    }

    override suspend fun getSearchSuggestions(query: String): Result<List<String>> {
        return networkErrorHandler.handle {
            val cachedSuggestions = cacheDataSource.getMovieTitles(query)
            if (cachedSuggestions.isNotEmpty()) {
                return@handle cachedSuggestions
            }
            val searchResponse = remoteDataSource.searchMovies(query, 1)
            searchResponse.results.mapNotNull { it.title }.take(10)

        }
    }
}
