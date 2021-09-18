package com.projectdelta.naruto.data.model.entity.character

import java.io.Serializable

data class CharDatabookStats(
	val ninjutsu: Double? = null,
	val taijutsu: Double? = null,
	val genjutsu: Double? = null,
	val intelligence: Double? = null,
	val strength: Double? = null,
	val speed: Double? = null,
	val stamina: Double? = null,
	val handSeals: Double? = null,
	val total: Double? = null,
) : Serializable
