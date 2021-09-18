package com.projectdelta.naruto.ui.main.character

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.projectdelta.naruto.data.model.entity.character.Character
import com.projectdelta.naruto.databinding.CharacterItemBinding

class CharacterListAdapter :
	PagingDataAdapter<Character ,CharacterListAdapter.LayoutViewHolder>(DIFF_CALLBACK) {

	companion object{
		private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Character>(){
			override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
				return oldItem.id == newItem.id
			}

			override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
				return oldItem == newItem
			}
		}
	}

	inner class LayoutViewHolder(
		private val binding : CharacterItemBinding
	) : RecyclerView.ViewHolder(binding.root){

		fun bind(character: Character){
			with(binding){
				twCharName.text = character.name?.english ?: "test not found" // use isOk here //
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