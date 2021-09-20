package com.projectdelta.naruto.ui.main.episode

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.projectdelta.naruto.data.model.entity.chapter.Chapter
import com.projectdelta.naruto.databinding.EpisodeItemBinding
import com.projectdelta.naruto.util.callback.BaseModelDiffUtilCallback
import com.projectdelta.naruto.util.system.lang.isOk

class EpisodeListAdapter :
	PagingDataAdapter<Chapter ,EpisodeListAdapter.LayoutViewHolder>(BaseModelDiffUtilCallback()){

	inner class LayoutViewHolder(
		private val binding : EpisodeItemBinding
	) : RecyclerView.ViewHolder(binding.root){

		fun bind(episode : Chapter){
			with(binding){
				twName.text = if (episode.name?.english.isOk()) episode.name?.english else "test not found"
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