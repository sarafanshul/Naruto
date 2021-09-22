package com.projectdelta.naruto.ui.main.character

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.projectdelta.naruto.R
import com.projectdelta.naruto.data.model.entity.character.Character
import com.projectdelta.naruto.databinding.CharacterItemBinding
import com.projectdelta.naruto.util.Constants
import com.projectdelta.naruto.util.callback.BaseModelDiffUtilCallback
import com.projectdelta.naruto.util.callback.BaseModelItemClickCallback

class CharacterListAdapter(
	private val clickCallback: BaseModelItemClickCallback
) : PagingDataAdapter<Character ,CharacterListAdapter.LayoutViewHolder>(BaseModelDiffUtilCallback()) {

	inner class LayoutViewHolder(
		private val item_binding : CharacterItemBinding
	) : RecyclerView.ViewHolder(item_binding.root){

		@SuppressLint("SetTextI18n")
		fun bind(character: Character ,clickCallback: BaseModelItemClickCallback){
			with(item_binding){
				val context = root.context

				root.setOnClickListener {
					clickCallback.onItemClick(character ,characterItem)
				}

				characterItem.transitionName = Constants.TRANSITION_CHARACTER.plus(character.id)

				Glide.with(context)
					.load(character.images?.first())
					.apply(
						RequestOptions()
//						.placeholder(R.drawable.character_placeholder)
						.diskCacheStrategy(DiskCacheStrategy.DATA)
					)
					.into(itemImage)

				itemName.text = character.name?.english
				itemKanjiValue.text = character.name?.kanji
				itemGenderValue.text = character.personal?.sex
				val debut = if(character.debut?.anime?.name == "Naruto Shippūden" ) "Shippūden" else character.debut?.anime?.name
				itemDebutValue.text = "$debut #${character.debut?.anime?.episode}"
				itemStatusValue.text = character.personal?.status
				if( character.personal?.status == "Alive" )
					itemStatusValue.setTextColor(context.getColor(R.color.rm_green_600))
				else
					itemStatusValue.setTextColor(context.getColor(R.color.rm_red_add))

				itemAffiliationValue.text = character.personal?.affiliation?.first() ?: "Side Character!"
			}
		}

	}

	override fun onBindViewHolder(holder: LayoutViewHolder, position: Int) {
		holder.bind(getItem(position)!! ,clickCallback)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LayoutViewHolder {
		return LayoutViewHolder(
			CharacterItemBinding.inflate(
				LayoutInflater.from(parent.context) ,parent ,false
			)
		)
	}
}