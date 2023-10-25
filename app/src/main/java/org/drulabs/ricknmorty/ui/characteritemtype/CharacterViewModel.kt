package org.drulabs.ricknmorty.ui.characteritemtype

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.drulabs.ricknmorty.data.CharacterRepository
import org.drulabs.ricknmorty.ui.characteritemtype.CharactersUiState.Error
import org.drulabs.ricknmorty.ui.characteritemtype.CharactersUiState.Loading
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private var lastPage = 0
    private var lastSearchPage = 0
    private var searchQuery = ""
    private var searchPageMax = Int.MAX_VALUE
    private var lastPageMax = Int.MAX_VALUE
    private val characterList: MutableList<Character> = mutableListOf()
    private val canLoadMore
        get() = lastPage < lastPageMax

    private val _uiState = MutableStateFlow<CharactersUiState>(Loading)
    val uiStatePublic: StateFlow<CharactersUiState> = _uiState

    init {
        loadNextPage()
    }

    fun addCharacterItemType(character: Character) {
        viewModelScope.launch {
            characterRepository.add(character.toCharacterData())
        }
    }

    fun loadNextPage() {
        if (canLoadMore) {
            viewModelScope.launch {
                _uiState.value = Loading
                delay(3_000)
                characterRepository.loadNextBatch(pageNo = ++lastPage)
                    .catch { err ->
                        _uiState.value = Error(err)
                    }.collect { characterPair ->
                        lastPageMax = characterPair.first
                        characterList += characterPair.second.map { Character.from(it) }
                        _uiState.value = CharactersUiState.Success(characterList)
                    }
            }
        } else {
            _uiState.value = CharactersUiState.Success(characterList)
        }
    }

    fun refresh(shouldLoadNext: Boolean = false) {
        lastPage = 0
        lastSearchPage = 0
        characterList.clear()
        searchPageMax = Int.MAX_VALUE
        lastPageMax = Int.MAX_VALUE
        if (shouldLoadNext) {
            viewModelScope.launch {
                loadNextPage()
            }
        }
    }

    private var searchJob: Job? = null

    fun searchCharacter(query: String) {
        with(searchJob) {
            this?.isActive
            this?.cancel()
        }
        searchJob = viewModelScope.launch {
            _uiState.value = Loading
            if (lastSearchPage == 0 || query != searchQuery) {
                searchQuery = query
                refresh()
            }
            if (lastSearchPage < searchPageMax) {
                characterRepository.search(query = query, page = ++lastSearchPage)
                    .catch { err ->
                        refresh()
                        _uiState.value = Error(err)
                    }.collect { characterPair ->
                        searchPageMax = characterPair.first
                        characterList += characterPair.second.map { Character.from(it) }
                        _uiState.value = CharactersUiState.Success(characterList)
                    }
            } else {
                _uiState.value = CharactersUiState.Success(characterList)
            }
        }
    }

    fun loadNextSearchPage() {
        searchCharacter(searchQuery)
    }
}

sealed interface CharactersUiState {
    data object Loading : CharactersUiState
    data class Error(val throwable: Throwable) : CharactersUiState
    data class Success(val data: List<Character>) : CharactersUiState
}
