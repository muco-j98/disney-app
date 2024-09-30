package com.muco.disneyapp

import com.muco.disneyapp.data.CharacterModel
import com.muco.disneyapp.data.DisneyRepository
import com.muco.disneyapp.viewmodels.CharactersUiState
import com.muco.disneyapp.viewmodels.DisneyCharactersViewmodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class DisneyCharactersViewmodelTest {

    private lateinit var viewModel: DisneyCharactersViewmodel
    private val mockRepository: DisneyRepository = mock()

    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()


    @Before
    fun setup() {
        viewModel = DisneyCharactersViewmodel(mockRepository)
    }

    @Test
    fun `initial state should be Loading`() {
        val uiState = viewModel.charactersStateFlow.value
        assertEquals(CharactersUiState.Loading, uiState)
    }

    @Test
    fun `test success state with characters`() = runTest {
        val data = CharacterModel(
            0,
            0,
            emptyList(),
            "",
            emptyList(),
            emptyList(),
            null,
            "",
            emptyList(),
            emptyList(),
            "",
            emptyList(),
            "",
            "",
            emptyList()
        )
        val mockCharacters = listOf(
            data, data
        )
        whenever(mockRepository.getCharacters()).doReturn(mockCharacters)

        viewModel.charactersStateFlow.first {
            it is CharactersUiState.Success
        }

        val state = viewModel.charactersStateFlow.value

        assertTrue(state is CharactersUiState.Success)
        assertEquals(mockCharacters, (state as CharactersUiState.Success).characters)
    }

    @Test
    fun `test error state when exception thrown`() = runTest {
        whenever(mockRepository.getCharacters()).doThrow(IllegalStateException())

        viewModel.charactersStateFlow.first {
            it is CharactersUiState.Error
        }

        val state = viewModel.charactersStateFlow.value

        assertTrue(state is CharactersUiState.Error)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}