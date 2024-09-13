package com.muco.disneyapp.api

import com.muco.disneyapp.data.CharactersResponse
import retrofit2.http.GET

interface DisneyApi {

    @GET("character")
    suspend fun getCharacters(): Result<CharactersResponse>
}