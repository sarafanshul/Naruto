package com.projectdelta.naruto.util.callback

import androidx.cardview.widget.CardView
import com.projectdelta.naruto.data.model.entity.BaseModel

interface BaseModelItemClickCallback {

	fun onItemClick(item: BaseModel, itemCard: CardView)

}
