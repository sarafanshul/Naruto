@file:Suppress("unused")

package com.projectdelta.naruto.util.system.lang

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginBottom
import androidx.core.view.updatePadding
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.projectdelta.naruto.util.Constants.CLICK_COLOR_CHANGE_TIME
import com.projectdelta.naruto.util.callback.TodoCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Extension functions for starting animation with a function call after end of animation
 * @param animation animation to play
 * @param onEnd lambda to trigger for end of animation
 */
fun View.startAnimation(animation: Animation, onEnd: () -> Unit) {
	animation.setAnimationListener(object : Animation.AnimationListener {
		override fun onAnimationStart(p0: Animation?) {
		}

		override fun onAnimationEnd(p0: Animation?) {
			onEnd()
		}

		override fun onAnimationRepeat(p0: Animation?) {}
	})
	this.startAnimation(animation)
}

fun View.getCoordinates() = Point(
	(left + right) / 2,
	(top + bottom) / 2
)

fun View.visible() {
	visibility = View.VISIBLE
}

fun View.invisible() {
	visibility = View.INVISIBLE
}

fun View.gone() {
	visibility = View.GONE
}

/**
 * A easy View Fade In / Out sourced from [here](https://github.com/mitchtabian/Clean-Notes/blob/ea8f6c95c57685aed42b3b1286aecb33cc2bbf77/app/src/main/java/com/codingwithmitch/cleannotes/framework/presentation/common/ViewExtensions.kt#L47)
 */
fun View.fadeIn() {
	val animationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
	apply {
		visible()
		alpha = 0f
		animate()
			.alpha(1f)
			.setDuration(animationDuration.toLong())
			.setListener(null)
	}
}

/**
 * A easy View Fade In / Out sourced from [here](https://github.com/mitchtabian/Clean-Notes/blob/ea8f6c95c57685aed42b3b1286aecb33cc2bbf77/app/src/main/java/com/codingwithmitch/cleannotes/framework/presentation/common/ViewExtensions.kt#L47)
 * @param todoCallback a callback for doing something when animation ends
 */
fun View.fadeOut(todoCallback: TodoCallback? = null) {
	val animationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
	apply {
		animate()
			.alpha(0f)
			.setDuration(animationDuration.toLong())
			.setListener(object : AnimatorListenerAdapter() {
				override fun onAnimationEnd(animation: Animator) {
					invisible()
					todoCallback?.execute()
				}
			})
	}
}

fun View.onSelectChangeColor(
	lifeCycleScope: CoroutineScope,
	clickColor: Int
) = CoroutineScope(lifeCycleScope.coroutineContext).launch {
	val intialColor = (background as ColorDrawable).color
	setBackgroundColor(
		ContextCompat.getColor(
			context,
			clickColor
		)
	)
	delay(CLICK_COLOR_CHANGE_TIME)
	setBackgroundColor(intialColor)
}

fun View.changeColor(newColor: Int) {
	setBackgroundColor(
		ContextCompat.getColor(
			context,
			newColor
		)
	)
}

fun EditText.disableContentInteraction() {
	keyListener = null
	isFocusable = false
	isFocusableInTouchMode = false
	isCursorVisible = false
	setBackgroundResource(android.R.color.transparent)
	clearFocus()
}

fun EditText.enableContentInteraction() {
	keyListener = EditText(context).keyListener
	isFocusable = true
	isFocusableInTouchMode = true
	isCursorVisible = true
	setBackgroundResource(android.R.color.white)
	requestFocus()
	if (text != null) {
		setSelection(text.length)
	}
}


// Author: https://github.com/sanogueralorenzo/Android-Kotlin-Clean-Architecture
/**
 * Use only from Activities, don't use from Fragment (with getActivity) or from Dialog/DialogFragment
 */
fun Activity.hideKeyboard() {
	val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
	val view = currentFocus ?: View(this)
	imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

/**
 * Use only from Activities, don't use from Fragment (with getActivity) or from Dialog/DialogFragment
 */
fun Activity.showKeyboard() {
	val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
	val view = currentFocus ?: View(this)
	imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)

}

/**
 * Use everywhere except from Activity (Custom View, Fragment, Dialogs, DialogFragments).
 */
fun View.showKeyboard() {
	val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
	imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

/**
 * Use everywhere except from Activity (Custom View, Fragment, Dialogs, DialogFragments).
 */
fun View.hideKeyboard() {
	val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
	imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Removes all item decorations from [RecyclerView]
 *
 * [See More](https://stackoverflow.com/a/59092408/11718077)
 */
fun <T : RecyclerView> T.removeItemDecorations() {
	while (itemDecorationCount > 0) {
		removeItemDecorationAt(0)
	}
}

/**
 * Extension function for setting left drawable in [TextView]
 * uses : `setCompoundDrawablesWithIntrinsicBounds(int left, int top, int right, int bottom)`
 *
 * **set 0 where you don't want images**
 *
 * [Source](https://stackoverflow.com/a/59652513/11718077)
 */
@Suppress("DEPRECATION")
fun TextView.leftDrawable(
	@DrawableRes id: Int = 0,
	@DimenRes sizeRes: Int = 0,
	@ColorInt color: Int = 0,
	@ColorRes colorRes: Int = 0
) {
	val drawable = ContextCompat.getDrawable(context, id)
	if (sizeRes != 0) {
		val size = resources.getDimensionPixelSize(sizeRes)
		drawable?.setBounds(0, 0, size, size)
	}
	if (color != 0) {
		drawable?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
	} else if (colorRes != 0) {
		val colorInt = ContextCompat.getColor(context, colorRes)
		drawable?.setColorFilter(colorInt, PorterDuff.Mode.SRC_ATOP)
	}
	this.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
}

fun View.setTopPaddingForStatusBar() {
	this.setOnApplyWindowInsetsListener { v, insets ->
		val insetsCompat = WindowInsetsCompat.toWindowInsetsCompat(insets)
		val systemWindow = insetsCompat.getInsets(
			WindowInsetsCompat.Type.statusBars()
		)
		v.updatePadding(top = systemWindow.top)
		insets
	}
}

/**
 * Slide Up and Down for bottom nav with animations ,[slideUp] and [slideDown] uses [translationObjectY]
 * to get interpolator object animator.
 * Sourced from [here](https://stackoverflow.com/a/65251040/11718077)
 */
fun View.translationObjectY(
	startY: Float,
	endY: Float,
	duration: Long = 200L
): ObjectAnimator {
	return ObjectAnimator.ofFloat(this, "translationY", startY, endY).apply {
		this.duration = duration
		interpolator = LinearOutSlowInInterpolator()
		start()
	}
}

fun BottomNavigationView.slideDown(todoCallback: TodoCallback? = null) {
	if (translationY == 0f) {
		translationObjectY(0f, height.toFloat() + marginBottom.toFloat()).apply {
			doOnEnd {
				todoCallback?.execute()
			}
		}
	}
}

fun BottomNavigationView.slideUp(todoCallback: TodoCallback? = null) {
	if (translationY == height.toFloat() + marginBottom.toFloat()) {
		translationObjectY(height.toFloat() + marginBottom.toFloat(), 0f).apply {
			doOnEnd {
				todoCallback?.execute()
			}
		}
	}
}
