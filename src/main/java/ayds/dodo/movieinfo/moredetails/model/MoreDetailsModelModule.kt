package ayds.dodo.movieinfo.moredetails.model

import ayds.dodo.movieinfo.moredetails.model.repository.TmdbRepositoryImp
import ayds.dodo.movieinfo.moredetails.model.repository.external.tmdb.TheMovieDBAPI
import ayds.dodo.movieinfo.moredetails.model.repository.external.tmdb.TmdbResponseToTmdbMovieImp
import ayds.dodo.movieinfo.moredetails.model.repository.external.tmdb.TmdbService
import ayds.dodo.movieinfo.moredetails.model.repository.local.db.DataBase
import ayds.dodo.movieinfo.moredetails.model.repository.local.db.SqlQueriesImp
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object MoreDetailsModelModule {
    private const val TMDB_URL_BASE = "https://api.themoviedb.org/3/"

    private val retrofit = Retrofit.Builder().baseUrl(TMDB_URL_BASE).addConverterFactory(ScalarsConverterFactory.create()).build()

    private fun getTmdbAPI(): TheMovieDBAPI = retrofit.create(TheMovieDBAPI::class.java)

    private val repository = TmdbRepositoryImp(DataBase(SqlQueriesImp()), TmdbService(getTmdbAPI(), TmdbResponseToTmdbMovieImp()))

    val moreDetailsModel: MoreDetailsModel = MoreDetailsModelImpl(repository)
}