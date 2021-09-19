package com.projectdelta.naruto.ui.main.episode

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.projectdelta.naruto.data.model.entity.chapter.Chapter
import com.projectdelta.naruto.databinding.EpisodeItemBinding

class EpisodeListAdapter :
	PagingDataAdapter<Chapter ,EpisodeListAdapter.LayoutViewHolder>(DIFF_CALLBACK){

	companion object{
		private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Chapter>(){
			override fun areItemsTheSame(oldItem: Chapter, newItem: Chapter): Boolean {
				return oldItem.id == newItem.id
			}

			override fun areContentsTheSame(oldItem: Chapter, newItem: Chapter): Boolean {
				return oldItem == newItem
			}
		}
	}

	inner class LayoutViewHolder(
		private val binding : EpisodeItemBinding
	) : RecyclerView.ViewHolder(binding.root){

		fun bind(episode : Chapter){
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
			EpisodeItemBinding.inflate(
				LayoutInflater.from(parent.context) ,parent ,false
			)
		)
	}
}