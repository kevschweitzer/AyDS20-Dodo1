package ayds.dodo.movieinfo.moredetails.model

import ayds.dodo.movieinfo.moredetails.model.repository.TmdbRepositoryImp
import ayds.dodo.movieinfo.moredetails.model.repository.external.tmdb.TheMovieDBAPI
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object MoreDetailsModelModule {
    private val TMDB_URL_BASE = "https://api.themoviedb.org/3/"
    private val retrofit = Retrofit.Builder().baseUrl(TMDB_URL_BASE).addConverterFactory(ScalarsConverterFactory.create()).build()

    private fun getTmdbAPI(): TheMovieDBAPI = retrofit.create(TheMovieDBAPI::class.java)

    private val repository = TmdbRepositoryImp()

    val moreDetailsModel: MoreDetailsModel = MoreDetailsModelImpl(repository)
}