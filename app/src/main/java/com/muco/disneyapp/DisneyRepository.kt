package com.muco.disneyapp

import javax.inject.Inject

class DisneyRepository @Inject constructor(
    private val disneyApi: DisneyApi
) {
    suspend fun getCharacters() = disneyApi.getCharacters()
}