package com.projectdelta.naruto.ui.main.location.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.projectdelta.naruto.R
import com.projectdelta.naruto.data.model.entity.location.Village
import com.projectdelta.naruto.databinding.LocationItemBinding
import com.projectdelta.naruto.util.Constants.TRANSITION_LOCATION
import com.projectdelta.naruto.util.NotFound
import com.projectdelta.naruto.util.callback.BaseModelDiffUtilCallback
import com.projectdelta.naruto.util.callback.BaseModelItemClickCallback
import com.projectdelta.naruto.util.system.lang.isOk

class LocationListAdapter(
	private val clickCallback: BaseModelItemClickCallback
) : PagingDataAdapter<Village , LocationListAdapter.LayoutViewHolder>(BaseModelDiffUtilCallback()){

	inner class LayoutViewHolder(
		private val binding : LocationItemBinding
	) : RecyclerView.ViewHolder(binding.root){

		fun bind(village: Village ,clickCallback: BaseModelItemClickCallback){
			with(binding){
				val context = root.context

				root.setOnClickListener {
					clickCallback.onItemClick(village ,locationItem)
				}
				locationItem.transitionName = TRANSITION_LOCATION.plus(village.id)

				if( village.image.isOk() )
					Glide.with(context)
						.load(village.image)
						.apply(
							RequestOptions()
								.placeholder(R.drawable.jinchuriki)
								.diskCacheStrategy(DiskCacheStrategy.DATA)
						)
						.into(itemImage)
				else
					itemImage.setImageResource(R.drawable.jinchuriki)

				itemName.text = village.name?.english
				itemKanjiValue.text = village.name?.kanji
				itemRomajiValue.text = village.name?.romaji
				itemCountryValue.text = village.data?.country ?: NotFound.surpriseMe()
				itemLeaderValue.text = village.data?.leader?.last() ?: NotFound.surpriseMe()
			}
		}

	}

	override fun onBindViewHolder(holder: LayoutViewHolder, position: Int) {
		holder.bind(getItem(position)!! ,clickCallback)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LayoutViewHolder {
		return LayoutViewHolder(
			LocationItemBinding.inflate(
				LayoutInflater.from(parent.context) ,parent ,false
			)
		)
	}
}

