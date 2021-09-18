package com.projectdelta.naruto.data.model.entity.character

import java.io.Serializable

data class CharDatabook(
	val name: String? = null,
	val edition: String? = null,
	val stats: CharDatabookStats? = null,
) : Serializable