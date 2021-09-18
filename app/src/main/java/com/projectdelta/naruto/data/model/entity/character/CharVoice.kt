package com.projectdelta.naruto.data.model.entity.character

import java.io.Serializable

data class CharVoice(
	val english: List<String>? = null,
	val japanese: List<String>? = null,
) : Serializable