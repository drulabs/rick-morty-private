package org.drulabs.ricknmorty.data.remote.dto

data class NetworkInfo(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)
