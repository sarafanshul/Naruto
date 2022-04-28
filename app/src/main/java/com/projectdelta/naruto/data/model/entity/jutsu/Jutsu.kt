package com.projectdelta.naruto.data.model.entity.jutsu

import com.projectdelta.naruto.data.model.entity.BaseModel
import java.io.Serializable

data class Jutsu(
	override var id: String,
	val name: JutsuName? = null,
	val description: String? = null,
	val image: String? = null,
	val classification: List<String>? = null,
	val nature: String? = null,
	val rank: String? = null,
	val type: List<String>? = null,
	val range: String? = null,
	val handSeals: String? = null,
	val debut: JutsuDebut? = null,
	val users: List<String>? = null,
) : BaseModel(), Serializable
