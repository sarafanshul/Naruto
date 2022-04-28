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
import com.projectdelta.naruto.util.Constants.TRANSITION_EPISODE
import com.projectdelta.naruto.util.NotFound
import com.projectdelta.naruto.util.callback.BaseModelDiffUtilCallback
import com.projectdelta.naruto.util.callback.BaseModelItemClickCallback
import java.text.SimpleDateFormat
import java.util.Locale

class EpisodeListAdapter(
	private val clickCallback: BaseModelItemClickCallback
) :
	PagingDataAdapter<Chapter, EpisodeListAdapter.LayoutViewHolder>(BaseModelDiffUtilCallback()) {

	val dateFormat = SimpleDateFormat("dd MMMM yy", Locale.ENGLISH)

	inner class LayoutViewHolder(
		private val binding: EpisodeItemBinding
	) : RecyclerView.ViewHolder(binding.root) {

		@SuppressLint("SetTextI18n")
		fun bind(chapter: Chapter, clickCallback: BaseModelItemClickCallback) {
			with(binding) {

				val context = root.context

				root.setOnClickListener {
					clickCallback.onItemClick(chapter, episodeItem)
				}

				episodeItem.transitionName = TRANSITION_EPISODE.plus(chapter.id)

				Glide.with(context)
					.load(chapter.images?.first())
					.apply(
						RequestOptions()
							.placeholder(R.drawable.placeholder_naruto_ep)
							.diskCacheStrategy(DiskCacheStrategy.DATA)
					)
					.into(itemImage)

				itemName.text = "${
					chapter.episode?.series?.split(" ")?.last()
				} #${chapter.episode?.episode?.toInt()}"
				itemName.isSelected = true

				itemKanjiValue.text = chapter.name?.kanji
				itemKanjiValue.isSelected = true
				itemOpValue.text = chapter.music?.opening
				itemOpValue.isSelected = true
				itemArcValue.text = chapter.arc
				itemArcValue.isSelected = true
				itemDebutValue.text =
					if (chapter.date?.japanese != null) dateFormat.format(chapter.date.japanese) else NotFound.surpriseMe()
				itemEpisodeNameValue.text = chapter.name?.english
				itemEpisodeNameValue.isSelected = true

				itemFiller.visibility =
					if (!chapter.manga?.chapters.isNullOrEmpty()) View.GONE else View.VISIBLE
			}
		}

	}

	override fun onBindViewHolder(holder: LayoutViewHolder, position: Int) {
		holder.bind(getItem(position)!!, clickCallback)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LayoutViewHolder {
		return LayoutViewHolder(
			EpisodeItemBinding.inflate(
				LayoutInflater.from(parent.context), parent, false
			)
		)
	}
}
