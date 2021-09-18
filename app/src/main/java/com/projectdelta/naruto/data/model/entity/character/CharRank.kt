package com.projectdelta.naruto.data.model.entity.character

import java.io.Serializable

data class CharRank(
	val ninjaRank: List<String>? = null,
	val ninjaRegistration: String? = null,
	val academyGradAge: Int? = null,
	val chuninPromAge: Int? = null,
) : Serializable