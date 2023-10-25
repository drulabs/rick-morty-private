package org.drulabs.ricknmorty.data.remote

import kotlinx.coroutines.flow.Flow
import org.drulabs.ricknmorty.data.remote.dto.CharacterInfo
import org.drulabs.ricknmorty.data.remote.dto.EpisodeInfo
import org.drulabs.ricknmorty.data.remote.dto.LocationInfo
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface CharacterAPI {

    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int = 1): CharacterInfo

    @GET
    suspend fun getNextSetOfCharacters(@Url url: String): CharacterInfo

    @GET("character")
    suspend fun searchCharacters(@QueryMap queryMap: Map<String, String>): CharacterInfo
}

interface LocationAPI {
    @GET("location")
    suspend fun getLocations(@Query("page") page: Int = 1): LocationInfo

    @GET
    suspend fun getNextSetOfLocations(@Url url: String): LocationInfo

    @GET("location")
    suspend fun searchLocation(@QueryMap queryMap: Map<String, String>): LocationInfo
}

interface EpisodeAPI {
    @GET("episode")
    suspend fun getEpisodes(@Query("page") page: Int = 1): EpisodeInfo

    @GET
    suspend fun getNextSetOfEpisodes(@Url url: String): EpisodeInfo

    @GET("episode")
    suspend fun searchEpisodes(@QueryMap queryMap: Map<String, String>): EpisodeInfo
}