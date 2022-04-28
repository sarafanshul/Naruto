package com.projectdelta.naruto.data.model.entity.character

import java.io.Serializable

data class CharPersonal(
	val birthDate: String? = null,
	val sex: String? = null,
	val age: List<String>? = null,
	val specie: String? = null,
	val status: String? = null,
	val height: List<String>? = null,
	val weight: List<String>? = null,
	val bloodType: String? = null,
	val kekkeiGenkai: List<String>? = null,
	val kekkeiMora: List<String>? = null,
	val classification: List<String>? = null,
	val tailedBeast: List<String>? = null,
	val occupation: List<String>? = null,
	val affiliation: List<String>? = null,
	val team: List<String>? = null,
	val partner: List<String>? = null,
	val clan: List<String>? = null,
) : Serializable
