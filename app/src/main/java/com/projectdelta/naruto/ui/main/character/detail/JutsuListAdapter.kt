package com.projectdelta.naruto.ui.main.character.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.projectdelta.naruto.R
import com.projectdelta.naruto.data.model.entity.jutsu.Jutsu
import com.projectdelta.naruto.databinding.JutsuItemBinding
import com.projectdelta.naruto.util.callback.BaseModelDiffUtilCallback

class JutsuListAdapter:
	ListAdapter<Jutsu,JutsuListAdapter.LayoutViewHolder>(BaseModelDiffUtilCallback()){

	override fun onCreateViewHolder(parent1: ViewGroup, viewType1: Int): LayoutViewHolder {
		val binding = JutsuItemBinding.inflate(LayoutInflater.from(parent1.context) ,parent1 ,false)
		return LayoutViewHolder(binding)
	}

	override fun onBindViewHolder(holder: LayoutViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	inner class LayoutViewHolder( private val binding : JutsuItemBinding) :
		RecyclerView.ViewHolder(binding.root){

		fun bind( jutsu : Jutsu ){

			with(binding){

				Glide.with(root.context)
					.load(jutsu.image)
					.thumbnail(0.25f)
					.apply(
						RequestOptions()
							.diskCacheStrategy(DiskCacheStrategy.DATA)
					)
					.transition(withCrossFade())
					.into(itemImage)

				itemName.text = jutsu.name?.english
				itemName.isSelected = true
				itemKanjiValue.text = jutsu.name?.kanji
				itemNatureValue.text = jutsu.nature
				itemRomajiValue.text = jutsu.name?.romaji
				itemRangeValue.text = jutsu.range
				itemTypeValue.text = jutsu.type?.first()
			}

		}
	}
}