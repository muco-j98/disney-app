package com.muco.disneyapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisneyCharactersViewmodel @Inject constructor(
    private val repository: DisneyRepository
) : ViewModel() {
    private val _charactersStateFlow = MutableStateFlow<CharactersUiState>(CharactersUiState.Loading)
    val charactersStateFlow = _charactersStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            val result = repository.getCharacters()
            result.onSuccess {
                _charactersStateFlow.value = CharactersUiState.Success(it.characters)
            }.onFailure {
                _charactersStateFlow.value = CharactersUiState.Error
            }
        }
    }
}

sealed class CharactersUiState {
    data object Loading : CharactersUiState()
    data class Success(val characters: List<CharacterModel>) : CharactersUiState()
    data object Error : CharactersUiState()
}