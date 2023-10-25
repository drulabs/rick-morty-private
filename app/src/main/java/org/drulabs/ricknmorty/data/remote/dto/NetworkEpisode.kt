package org.drulabs.ricknmorty.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NetworkEpisode(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("air_date")
    val airDate: String,
    @SerializedName("episode")
    val episode: String
)