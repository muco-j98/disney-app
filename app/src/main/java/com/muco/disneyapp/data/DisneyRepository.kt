package com.muco.disneyapp.data

import com.muco.disneyapp.api.DisneyApi
import com.muco.disneyapp.viewmodels.CharactersUiState
import javax.inject.Inject

class DisneyRepository @Inject constructor(
    private val disneyApi: DisneyApi
) {
    suspend fun getCharacters(): List<CharacterModel> {
        return try {
            val result = disneyApi.getCharacters()
            result.getOrThrow().characters
        } catch (e: Exception) {
            throw IllegalStateException("Failed to fetch characters", e)
        }
    }
}