package com.muco.disneyapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muco.disneyapp.data.CharacterModel
import com.muco.disneyapp.data.DisneyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DisneyCharactersViewmodel @Inject constructor(
    private val repository: DisneyRepository
) : ViewModel() {

    val charactersStateFlow: StateFlow<CharactersUiState> = flow<CharactersUiState> {
            emit(CharactersUiState.Success(repository.getCharacters()))
        }.catch { emit(CharactersUiState.Error) }
            .flowOn(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = CharactersUiState.Loading
            )
}

sealed class CharactersUiState {
    data object Loading : CharactersUiState()
    data class Success(val characters: List<CharacterModel>) : CharactersUiState()
    data object Error : CharactersUiState()
}