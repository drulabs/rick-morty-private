package org.drulabs.ricknmorty.data.remote.dto

import com.google.gson.annotations.SerializedName

data class EpisodeInfo(
    val info: NetworkInfo? = null,
    val result: List<NetworkEpisode>? = null,
    val error: String? = null
)

data class LocationInfo(
    val info: NetworkInfo? = null,
    val result: List<LocationInfo>? = null,
    val error: String? = null
)

data class CharacterInfo(
    @SerializedName("info")
    val info: NetworkInfo? = null,
    @SerializedName("results")
    val result: List<NetworkCharacter>? = null,
    @SerializedName("error")
    val error: String? = null
)
