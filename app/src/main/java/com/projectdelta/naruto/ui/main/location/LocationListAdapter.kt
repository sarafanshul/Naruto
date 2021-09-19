package com.projectdelta.naruto.ui.main.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.projectdelta.naruto.data.model.entity.location.Village
import com.projectdelta.naruto.databinding.LocationItemBinding

class LocationListAdapter :
	PagingDataAdapter<Village ,LocationListAdapter.LayoutViewHolder>(DIFF_CALLBACK){

	companion object{
		private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Village>(){
			override fun areItemsTheSame(oldItem: Village, newItem: Village): Boolean {
				return oldItem.id == newItem.id
			}

			override fun areContentsTheSame(oldItem: Village, newItem: Village): Boolean {
				return oldItem == newItem
			}
		}
	}

	inner class LayoutViewHolder(
		private val binding : LocationItemBinding
	) : RecyclerView.ViewHolder(binding.root){

		fun bind(episode : Village){
			with(binding){
				twCharName.text = episode.name?.english ?: "test not found"
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