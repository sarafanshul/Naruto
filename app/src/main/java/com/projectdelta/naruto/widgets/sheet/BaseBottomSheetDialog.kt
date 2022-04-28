package com.projectdelta.naruto.widgets.sheet

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.projectdelta.naruto.R
import com.projectdelta.naruto.util.system.lang.displayCompat
import com.projectdelta.naruto.util.system.lang.setNavigationBarTransparentCompat

abstract class BaseBottomSheetDialog(context: Context) : BottomSheetDialog(context) {

	abstract fun createView(inflater: LayoutInflater): View

	@SuppressLint("ObsoleteSdkInt")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val rootView = createView(layoutInflater)
		setContentView(rootView)

		// Enforce max width for tablets
		val width = context.resources.getDimensionPixelSize(R.dimen.bottom_sheet_width)
		if (width > 0) {
			behavior.maxWidth = width
		}

		// Set peek height to 50% display height
		context.displayCompat?.let {
			val metrics = DisplayMetrics()
			it.getRealMetrics(metrics)
			behavior.peekHeight = metrics.heightPixels / 2
		}

		// Set navbar color to transparent for edge-to-edge bottom sheet if we can use light navigation bar
		// TODO Replace deprecated systemUiVisibility when material-components uses new API to modify status bar icons
		// TODO when implement dark theme update flags
		@Suppress("DEPRECATION")
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			window?.setNavigationBarTransparentCompat(context)
			val bottomSheet = rootView.parent as ViewGroup
			var flags = bottomSheet.systemUiVisibility
//			flags = if (Injekt.get<PreferenceManager>().isDarkMode()) {
//				flags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
//			} else {
//				flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
//			}
			flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
			bottomSheet.systemUiVisibility = flags
		}
	}
}
