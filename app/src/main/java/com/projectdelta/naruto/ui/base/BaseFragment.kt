package com.projectdelta.naruto.ui.base

import android.widget.TextView
import androidx.fragment.app.Fragment
import com.projectdelta.naruto.util.callback.TodoCallback
import com.projectdelta.naruto.util.system.lang.fadeIn
import com.projectdelta.naruto.util.system.lang.fadeOut
import com.projectdelta.naruto.util.system.lang.gone
import com.projectdelta.naruto.util.system.lang.visible
import kotlinx.coroutines.Job

abstract class BaseFragment : Fragment(){

	protected val jobs = mutableListOf<Job>()

	protected fun displayToolbarTitle(textView: TextView, title: String?, @Suppress("SameParameterValue") useAnimation: Boolean) {
		if (title != null) {
			showToolbarTitle(textView, title, useAnimation)
		} else {
			hideToolbarTitle(textView, useAnimation)
		}
	}

	private fun hideToolbarTitle(textView: TextView, animation: Boolean) {
		if (animation) {
			textView.fadeOut(
				object : TodoCallback {
					override fun execute() {
						textView.text = ""
					}
				}
			)
		} else {
			textView.text = " "
			textView.gone()
		}
	}

	private fun showToolbarTitle(textView: TextView, title: String, animation: Boolean) {
		textView.text = title
		if (animation) {
			textView.fadeIn()
		} else {
			textView.visible()
		}
	}

	override fun onDestroy() {
		jobs.forEach { it.cancel() }
		super.onDestroy()
	}
}