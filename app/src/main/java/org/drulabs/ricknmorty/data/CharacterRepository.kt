package org.drulabs.ricknmorty.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.drulabs.ricknmorty.data.local.database.CharacterData
import org.drulabs.ricknmorty.data.local.database.CharacterDataDao
import org.drulabs.ricknmorty.data.remote.CharacterAPI
import javax.inject.Inject

interface CharacterRepository {
    suspend fun loadNextBatch(pageNo: Int): Flow<Pair<Int, List<CharacterData>>>
    suspend fun add(characterData: CharacterData)
    suspend fun search(query: String, page: Int = 1): Flow<Pair<Int, List<CharacterData>>>
}

class DefaultCharacterRepository @Inject constructor(
    private val characterDataDao: CharacterDataDao,
    private val characterAPI: CharacterAPI
) : CharacterRepository {

    override suspend fun loadNextBatch(pageNo: Int): Flow<Pair<Int, List<CharacterData>>> {
        return flow {
            emit(
                characterAPI.getCharacters(page = pageNo)
            )
        }.map { info ->
            Pair(info.info?.pages ?: 0, info.result?.map { networkCharacter ->
                val characters = CharacterData.from(networkCharacter)
                characterDataDao.insertCharacterData(characters)
                characters
            } ?: emptyList())
        }
    }

    override suspend fun add(characterData: CharacterData) {
        characterDataDao.insertCharacterData(characterData)
    }

    override suspend fun search(query: String, page: Int): Flow<Pair<Int, List<CharacterData>>> {
        return flow {
            emit(characterAPI.searchCharacters(mapOf("name" to query, "page" to "$page")))
        }.map { info ->
            Pair(info.info?.pages ?: 0, info.result?.map { networkCharacter ->
                val characters = CharacterData.from(networkCharacter)
                characters
            } ?: emptyList())
        }
    }
}