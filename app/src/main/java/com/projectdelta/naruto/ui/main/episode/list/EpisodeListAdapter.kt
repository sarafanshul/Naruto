package com.projectdelta.naruto.ui.main.episode.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.projectdelta.naruto.R
import com.projectdelta.naruto.data.model.entity.chapter.Chapter
import com.projectdelta.naruto.databinding.EpisodeItemBinding
import com.projectdelta.naruto.util.Constants
import com.projectdelta.naruto.util.callback.BaseModelDiffUtilCallback

class EpisodeListAdapter :
	PagingDataAdapter<Chapter , EpisodeListAdapter.LayoutViewHolder>(BaseModelDiffUtilCallback()){

	inner class LayoutViewHolder(
		private val binding : EpisodeItemBinding
	) : RecyclerView.ViewHolder(binding.root){

		@SuppressLint("SetTextI18n")
		fun bind(chapter : Chapter){
			with(binding){

				val context = root.context

				root.setOnClickListener {
//					clickCallback.onItemClick(character ,characterItem)
				}

				episodeItem.transitionName = Constants.TRANSITION_EPISODE.plus(chapter.id)

				Glide.with(context)
					.load(chapter.images?.first())
					.apply(
						RequestOptions()
							.placeholder(R.drawable.placeholder_white_leaf)
							.diskCacheStrategy(DiskCacheStrategy.DATA)
					)
					.into(itemImage)

				itemName.text = "${chapter.episode?.series} #${chapter.episode?.episode}"

				itemKanjiValue.text = chapter.name?.kanji
				itemOpValue.text = chapter.music?.opening
				itemArcValue.text = chapter.arc
				itemDebutValue.text = chapter.date?.japanese.toString()
				itemEpisodeNameValue.text = chapter.name?.english

				itemFiller.visibility = if( ! chapter.manga?.chapters.isNullOrEmpty() ) View.GONE else View.VISIBLE
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