package com.projectdelta.naruto.ui.main.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.projectdelta.naruto.data.model.entity.location.Village
import com.projectdelta.naruto.databinding.LocationItemBinding
import com.projectdelta.naruto.util.callback.BaseModelDiffUtilCallback
import com.projectdelta.naruto.util.system.lang.isOk

class LocationListAdapter :
	PagingDataAdapter<Village ,LocationListAdapter.LayoutViewHolder>(BaseModelDiffUtilCallback()){

	inner class LayoutViewHolder(
		private val binding : LocationItemBinding
	) : RecyclerView.ViewHolder(binding.root){

		fun bind(location : Village){
			with(binding){
				twName.text = if (location.name?.english.isOk()) location.name?.english else "test not found"
			}
		}

	}

	override fun onBindViewHolder(holder: LayoutViewHolder, position: Int) {
		holder.bind(getItem(position)!!)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LayoutViewHolder {
		return LayoutViewHolder(
			LocationItemBinding.inflate(
				LayoutInflater.from(parent.context) ,parent ,false
			)
		)
	}
}