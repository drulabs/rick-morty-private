package org.drulabs.ricknmorty.data.remote.dto

data class NetworkCharacter(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: NetworkNameWithUrl,
    val location: NetworkNameWithUrl,
    val image: String,
    val url: String,
    val episode: List<String> = emptyList()
)

data class NetworkNameWithUrl(
    val name: String,
    val url: String
)
