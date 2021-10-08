package com.projectdelta.naruto.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.projectdelta.naruto.R
import com.projectdelta.naruto.util.system.lang.getResourceColor

class ThemedSwipeRefreshLayout @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {
	init {
		// Background
		setProgressBackgroundColorSchemeColor(context.getResourceColor(R.attr.colorPrimary))
		// This updates the progress arrow color
		setColorSchemeColors(context.getResourceColor(R.attr.colorOnPrimary))
	}
}