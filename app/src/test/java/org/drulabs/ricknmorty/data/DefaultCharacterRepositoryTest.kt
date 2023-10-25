///*
// * Copyright (C) 2022 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package org.drulabs.ricknmorty.data
//
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.test.runTest
//import org.junit.Assert.assertEquals
//import org.junit.Test
//import org.drulabs.ricknmorty.data.local.database.CharacterItemType
//import org.drulabs.ricknmorty.data.local.database.CharacterItemTypeDao
//
///**
// * Unit tests for [DefaultCharacterRepository].
// */
//@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
//class DefaultCharacterRepositoryTest {
//
//    @Test
//    fun characterItemTypes_newItemSaved_itemIsReturned() = runTest {
//        val repository = DefaultCharacterRepository(FakeCharacterItemTypeDao())
//
//        repository.add("Repository")
//
//        assertEquals(repository.characterItemTypes.first().size, 1)
//    }
//
//}
//
//private class FakeCharacterItemTypeDao : CharacterItemTypeDao {
//
//    private val data = mutableListOf<CharacterItemType>()
//
//    override fun getCharacterItemTypes(): Flow<List<CharacterItemType>> = flow {
//        emit(data)
//    }
//
//    override suspend fun insertCharacterItemType(item: CharacterItemType) {
//        data.add(0, item)
//    }
//}
