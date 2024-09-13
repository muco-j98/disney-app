package com.muco.disneyapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Info(
    val count: Int,
    val nextPage: String?,
    val previousPage: String?,
    val totalPages: Int
)