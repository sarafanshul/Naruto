package com.projectdelta.naruto.data.model.entity.character

import com.projectdelta.naruto.data.model.entity.BaseModel
import java.io.Serializable

data class Character(
	override var id: String,
	val name: CharName? = null,
	val description: String? = null,
	val images: List<String>? = null,
	val debut: CharDebut? = null,
	val voices: CharVoice? = null,
	val personal: CharPersonal? = null,
	val charRank: CharRank? = null,
	val family: List<String>? = null,
	val natureTypes: List<String>? = null,
	val uniqueTraits: List<String>? = null,
	val jutsus: List<String>? = null,
	val tools: List<String>? = null,
	val databooks: List<CharDatabook>? = null,
) : BaseModel(), Serializable{

	companion object{

		enum class SortCharacter(val value : String){
			BY_NAME_ASC("name.english,asc"),
			BY_NAME_DESC("name.english,desc"),
			BY_DEBUT_ASC("debut.anime,asc"),
			BY_DEBUT_DESC("debut.anime,desc"),
		}

		enum class CharacterStatus(val value :String){
			ALIVE("Alive"),
			DEAD("Deceased")
		}

		enum class CharacterSex(val value : String){
			MALE("Male"),
			FEMALE("Female"),
			BI("Various")
		}
	}
}