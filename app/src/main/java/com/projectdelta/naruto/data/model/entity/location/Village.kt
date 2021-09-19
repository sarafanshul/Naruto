package com.projectdelta.naruto.data.model.entity.location

data class Village(
	val id: String? = null,
	val name: VillageName? = null,
	val description: String? = null,
	val image: String? = null,
	val data: VillageData? = null,
	val statistic: VillageStatistic? = null,
)