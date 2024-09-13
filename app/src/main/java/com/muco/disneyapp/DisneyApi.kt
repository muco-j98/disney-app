package com.muco.disneyapp

import retrofit2.http.GET

interface DisneyApi {

    @GET("character")
    suspend fun getCharacters(): Result<CharactersResponse>
}