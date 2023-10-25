package org.drulabs.ricknmorty.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverter
import kotlinx.coroutines.flow.Flow
import org.drulabs.ricknmorty.data.remote.dto.NetworkCharacter
import org.drulabs.ricknmorty.data.remote.dto.NetworkNameWithUrl

@Entity(tableName = "character_data")
data class CharacterData(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "character_name") val name: String,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "species") val species: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "gender") val gender: String,
    @Embedded(prefix = "origin") val origin: NameWithUrlData,
    @Embedded(prefix = "location") val location: NameWithUrlData,
    @ColumnInfo(name = "image_url") val image: String,
    @ColumnInfo(name = "character_url") val url: String,
    @ColumnInfo(name = "episode_list") val episodes: List<String> = emptyList()
) {
    companion object {
        fun from(networkCharacter: NetworkCharacter): CharacterData =
            CharacterData(
                id = networkCharacter.id,
                name = networkCharacter.name,
                gender = networkCharacter.gender,
                species = networkCharacter.species,
                type = networkCharacter.type,
                status = networkCharacter.status,
                image = networkCharacter.image,
                url = networkCharacter.url,
                location = NameWithUrlData.from(networkCharacter.location),
                origin = NameWithUrlData.from(networkCharacter.origin),
                episodes = networkCharacter.episode
            )
    }
}

data class NameWithUrlData(
    val name: String, val url: String
) {
    companion object {
        fun from(networkNameWithUrl: NetworkNameWithUrl) =
            NameWithUrlData(
                name = networkNameWithUrl.name,
                url = networkNameWithUrl.url
            )
    }
}

class StringListTypeConverter {

    @TypeConverter
    fun toStringRep(list: List<String>) =
        list.joinToString("#_#")

    @TypeConverter
    fun fromStringRep(stringRep: String) =
        stringRep.split("#_#").toList()

}

@Dao
interface CharacterDataDao {
    @Query("SELECT * FROM character_data WHERE id >= :startId ORDER BY id LIMIT :limit")
    fun getCharacterData(startId: Int = 0, limit: Int = 10): Flow<List<CharacterData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacterData(vararg item: CharacterData)
}
