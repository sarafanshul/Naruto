package com.projectdelta.naruto.ui.main.character

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.projectdelta.naruto.data.model.entity.character.Character
import com.projectdelta.naruto.databinding.CharacterItemBinding
import com.projectdelta.naruto.util.callback.BaseModelDiffUtilCallback
import com.projectdelta.naruto.util.system.lang.isOk

class CharacterListAdapter :
	PagingDataAdapter<Character ,CharacterListAdapter.LayoutViewHolder>(BaseModelDiffUtilCallback()) {

	inner class LayoutViewHolder(
		private val binding : CharacterItemBinding
	) : RecyclerView.ViewHolder(binding.root){

		fun bind(character: Character){
			with(binding){
				twName.text = if (character.name?.english.isOk()) character.name?.english else "test not found"
			}
		}

	}

	override fun onBindViewHolder(holder: LayoutViewHolder, position: Int) {
		holder.bind(getItem(position)!!)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LayoutViewHolder {
		return LayoutViewHolder(
			CharacterItemBinding.inflate(
				LayoutInflater.from(parent.context) ,parent ,false
			)
		)
	}
}