package org.drulabs.ricknmorty.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.drulabs.ricknmorty.data.remote.CharacterAPI
import org.drulabs.ricknmorty.data.remote.EpisodeAPI
import org.drulabs.ricknmorty.data.remote.LocationAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    companion object {
        val BASE_URL
            get() = "https://rickandmortyapi.com/api/"
    }

    @Provides
    fun providesCharacterAPI(
        retrofit: Retrofit
    ): CharacterAPI = retrofit.create(CharacterAPI::class.java)

    @Provides
    fun providesLocationAPI(
        retrofit: Retrofit
    ): LocationAPI = retrofit.create(LocationAPI::class.java)

    @Provides
    fun providesEpisodeAPI(
        retrofit: Retrofit
    ): EpisodeAPI = retrofit.create(EpisodeAPI::class.java)

    @Provides
    fun providesRetrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build()
            ).build()
    }

}