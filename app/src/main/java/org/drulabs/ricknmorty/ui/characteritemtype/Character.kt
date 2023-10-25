package org.drulabs.ricknmorty.ui.characteritemtype

import org.drulabs.ricknmorty.data.local.database.CharacterData
import org.drulabs.ricknmorty.data.local.database.NameWithUrlData

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: NameWithUrl,
    val location: NameWithUrl,
    val image: String,
    val url: String,
    val episodes: List<String> = emptyList()
) {
    companion object {
        fun from(characterData: CharacterData): Character = Character(
            id = characterData.id,
            name = characterData.name,
            gender = characterData.gender,
            image = characterData.image,
            location = NameWithUrl.from(characterData.location),
            origin = NameWithUrl.from(characterData.origin),
            species = characterData.species,
            status = characterData.status,
            type = characterData.type,
            url = characterData.url,
            episodes = characterData.episodes
        )
    }

    fun toCharacterData(): CharacterData = CharacterData(
        id = this.id,
        name = this.name,
        gender = this.gender,
        image = this.image,
        location = this.location.toNameUrlData(),
        origin = this.origin.toNameUrlData(),
        species = this.species,
        status = this.status,
        type = this.type,
        url = this.url,
        episodes = this.episodes
    )
}

data class NameWithUrl(
    val name: String, val url: String
) {
    companion object {
        fun from(nameWithUrlData: NameWithUrlData): NameWithUrl = NameWithUrl(
            name = nameWithUrlData.name, url = nameWithUrlData.url
        )
    }

    fun toNameUrlData(): NameWithUrlData = NameWithUrlData(
        name = this.name, url = this.url
    )
}
