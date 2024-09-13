package com.muco.disneyapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharactersResponse(
    @SerialName("data")
    val characters: List<CharacterModel>,
    val info: Info
)