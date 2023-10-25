package org.drulabs.ricknmorty.data.remote.dto

data class NetworkLocation(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>
)
