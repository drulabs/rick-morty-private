package org.drulabs.ricknmorty.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.drulabs.ricknmorty.data.CharacterRepository
import org.drulabs.ricknmorty.data.DefaultCharacterRepository
import org.drulabs.ricknmorty.data.local.database.CharacterData
import org.drulabs.ricknmorty.data.local.database.NameWithUrlData
import org.drulabs.ricknmorty.ui.characteritemtype.Character
import org.drulabs.ricknmorty.ui.characteritemtype.NameWithUrl
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsCharacterItemTypeRepository(
        characterItemTypeRepository: DefaultCharacterRepository
    ): CharacterRepository
}

class FakeCharacterDataRepository @Inject constructor() : CharacterRepository {
    //override val characterData: Flow<List<CharacterData>> = flowOf(fakeCharacterItemTypes)
    override suspend fun loadNextBatch(pageNo: Int) =
        flowOf(Pair(1, fakeCharacterItemTypes))

    override suspend fun add(characterData: CharacterData) {
        throw NotImplementedError()
    }

    override suspend fun search(query: String, pageNo: Int): Flow<Pair<Int, List<CharacterData>>> {
        TODO("Not yet implemented")
    }
}

val character = CharacterData(
    id = 3,
    name = "Summer Smith",
    status = "Alive",
    species = "Human",
    type = "",
    gender = "Female",
    origin = NameWithUrlData(
        name = "Earth (Replacement Dimension)",
        url = "https://rickandmortyapi.com/api/location/20"
    ),
    location = NameWithUrlData(
        name = "Earth (Replacement Dimension)",
        url = "https://rickandmortyapi.com/api/location/20"
    ),
    image = "https://rickandmortyapi.com/api/character/avatar/3.jpeg",
    url = "https://rickandmortyapi.com/api/character/3"
)
val fakeCharacterItemTypes = listOf(character)
