package com.projectdelta.naruto.data.model.entity.location

import com.projectdelta.naruto.data.model.entity.BaseModel
import java.io.Serializable

data class Village(
	override var id: String,
	val name: VillageName? = null,
	val description: String? = null,
	val image: String? = null,
	val data: VillageData? = null,
	val statistic: VillageStatistic? = null,
) : BaseModel(), Serializable
