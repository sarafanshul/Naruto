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
) : BaseModel(), Serializable