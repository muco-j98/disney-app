package com.muco.disneyapp.utilities

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.muco.disneyapp.data.CharacterModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CustomNavType {

    val DisneyCharacterType = object : NavType<CharacterModel>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): CharacterModel? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): CharacterModel {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: CharacterModel): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: CharacterModel) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }
}