package com.muco.disneyapp.data

import com.muco.disneyapp.api.DisneyApi
import javax.inject.Inject

class DisneyRepository @Inject constructor(
    private val disneyApi: DisneyApi
) {
    suspend fun getCharacters() = disneyApi.getCharacters()
}