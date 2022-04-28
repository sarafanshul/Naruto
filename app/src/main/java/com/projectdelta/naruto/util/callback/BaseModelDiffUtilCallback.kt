package com.projectdelta.naruto.util.callback

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.projectdelta.naruto.data.model.entity.BaseModel

class BaseModelDiffUtilCallback<T : BaseModel> : DiffUtil.ItemCallback<T>() {
	override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
		return oldItem.id == newItem.id
	}

	@SuppressLint("DiffUtilEquals")
	override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
		return oldItem == newItem
	}
}
