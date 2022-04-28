package com.projectdelta.naruto.widgets.sheet

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.projectdelta.naruto.databinding.CommonTabbedSheetBinding
import com.projectdelta.naruto.widgets.ViewPagerAdapter

abstract class TabbedBottomSheetDialog(private val activity: Activity) :
	BaseBottomSheetDialog(activity) {

	lateinit var binding: CommonTabbedSheetBinding

	override fun createView(inflater: LayoutInflater): View {
		binding = CommonTabbedSheetBinding.inflate(activity.layoutInflater)

		val adapter = LibrarySettingsSheetAdapter()
		binding.pager.offscreenPageLimit = 2
		binding.pager.adapter = adapter
		binding.tabs.setupWithViewPager(binding.pager)

		return binding.root
	}

	abstract fun getTabViews(): List<View>

	abstract fun getTabTitles(): List<Int>

	private inner class LibrarySettingsSheetAdapter : ViewPagerAdapter() {

		override fun createView(container: ViewGroup, position: Int): View {
			return getTabViews()[position]
		}

		override fun getCount(): Int {
			return getTabViews().size
		}

		override fun getPageTitle(position: Int): CharSequence {
			return activity.resources!!.getString(getTabTitles()[position])
		}
	}
}
