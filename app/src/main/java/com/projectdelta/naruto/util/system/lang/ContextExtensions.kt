@file:Suppress("unused")

package com.projectdelta.naruto.util.system.lang

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.KeyguardManager
import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.util.TypedValue
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import com.projectdelta.naruto.R
import kotlin.math.roundToInt

/**
 * Display a toast in this context.
 *
 * @param resource the text resource.
 * @param duration the duration of the toast. Defaults to short.
 */
fun Context.toast(
	@StringRes resource: Int,
	duration: Int = Toast.LENGTH_SHORT,
	block: (Toast) -> Unit = {}
): Toast {
	return toast(getString(resource), duration, block)
}

/**
 * Display a toast in this context.
 *
 * @param text the text to display.
 * @param duration the duration of the toast. Defaults to short.
 */
fun Context.toast(
	text: String?,
	duration: Int = Toast.LENGTH_SHORT,
	block: (Toast) -> Unit = {}
): Toast {
	return Toast.makeText(this, text.orEmpty(), duration).also {
		block(it)
		it.show()
	}
}

/**
 * Display a Dark toast in this context.
 *
 * @param text the text to display.
 * @param duration the duration of the toast. Defaults to short.
 */
@Suppress("DEPRECATION")
@SuppressLint("ResourceAsColor")
fun Context.darkToast(
	text: String?,
	duration: Int = Toast.LENGTH_SHORT,
	block: (Toast) -> Unit = {}
): Toast {
	return Toast.makeText(this, text.orEmpty(), duration).apply {
		view?.background?.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN)
		view?.findViewById<TextView>(android.R.id.message)?.setTextColor(Color.WHITE)
	}.also {
		block(it)
		it.show()
	}
}

/**
 * Display a Customizable toast in this context.
 *
 * @param text the text to display.
 * @param duration the duration of the toast. Defaults to short.
 */
@Suppress("DEPRECATION")
fun Context.customToast(
	text: String?,
	duration: Int = Toast.LENGTH_SHORT,
	background: Int = Color.GREEN,
	textColor: Int = Color.RED,
	block: (Toast) -> Unit = {}
): Toast {
	return Toast.makeText(this, text.orEmpty(), duration).apply {
		view?.background?.setColorFilter(background, PorterDuff.Mode.SRC_IN)
		view?.findViewById<TextView>(android.R.id.message)?.setTextColor(textColor)
	}.also {
		block(it)
		it.show()
	}
}

/**
 * Helper method to create a notification builder.
 *
 * @param channelId the channel id.
 * @param block the function that will execute inside the builder.
 * @return a notification to be displayed or updated.
 */
fun Context.notificationBuilder(
	channelId: String,
	block: (NotificationCompat.Builder.() -> Unit)? = null
): NotificationCompat.Builder {
	val builder = NotificationCompat.Builder(this, channelId)
	if (block != null) {
		builder.block()
	}
	return builder
}

/**
 * Helper method to create a notification.
 *
 * @param channelId the channel id.
 * @param block the function that will execute inside the builder.
 * @return a notification to be displayed or updated.
 */
fun Context.notification(
	channelId: String,
	block: (NotificationCompat.Builder.() -> Unit)?
): Notification {
	val builder = notificationBuilder(channelId, block)
	return builder.build()
}

/**
 * Checks if the give permission is granted.
 *
 * @param permission the permission to check.
 * @return true if it has permissions.
 */
fun Context.hasPermission(permission: String) =
	ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

/**
 * Converts px to dp.
 */
val Int.pxToDp: Int
	get() = (this / Resources.getSystem().displayMetrics.density).toInt()

/**
 * Converts Int to dp
 */
val Int.dp: Int
	get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

/**
 * Converts dp to px.
 */
val Int.dpToPx: Float
	get() = (this * Resources.getSystem().displayMetrics.density)

/**
 * Converts to px and takes into account LTR/RTL layout.
 */
val Float.dpToPxEnd: Float
	get() = (
			this * Resources.getSystem().displayMetrics.density *
					if (Resources.getSystem().isLTR) 1 else -1
			)


val Resources.isLTR
	get() = configuration.layoutDirection == View.LAYOUT_DIRECTION_LTR

val Context.notificationManager: NotificationManager
	get() = getSystemService()!!

val Context.connectivityManager: ConnectivityManager
	get() = getSystemService()!!

val Context.powerManager: PowerManager
	get() = getSystemService()!!

val Context.keyguardManager: KeyguardManager
	get() = getSystemService()!!

/**
 * Convenience method to acquire a partial wake lock.
 */
@SuppressLint("WakelockTimeout")
fun Context.acquireWakeLock(tag: String): PowerManager.WakeLock {
	val wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "$tag:WakeLock")
	wakeLock.acquire()
	return wakeLock
}

/**
 * Function used to send a local broadcast asynchronous
 *
 * @param intent intent that contains broadcast information
 */
fun Context.sendLocalBroadcast(intent: Intent) {
	LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
}

/**
 * Function used to send a local broadcast synchronous
 *
 * @param intent intent that contains broadcast information
 */
fun Context.sendLocalBroadcastSync(intent: Intent) {
	LocalBroadcastManager.getInstance(this).sendBroadcastSync(intent)
}

/**
 * Function used to register local broadcast
 *
 * @param receiver receiver that gets registered.
 */
fun Context.registerLocalReceiver(receiver: BroadcastReceiver, filter: IntentFilter) {
	LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter)
}

/**
 * Function used to unregister local broadcast
 *
 * @param receiver receiver that gets unregistered.
 */
fun Context.unregisterLocalReceiver(receiver: BroadcastReceiver) {
	LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
}

/**
 * Returns true if the given service class is running.
 */
fun Context.isServiceRunning(serviceClass: Class<*>): Boolean {
	val className = serviceClass.name
	val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
	@Suppress("DEPRECATION")
	return manager.getRunningServices(Integer.MAX_VALUE)
		.any { className == it.service.className }
}

/**
 * Returns if a network connection is available or not. [For more info](https://stackoverflow.com/a/58605532)
 */
@Suppress("DEPRECATION", "ObsoleteSdkInt")
fun Context.isOnline(): Boolean {
	val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		val n = cm.activeNetwork
		if (n != null) {
			val nc = cm.getNetworkCapabilities(n)
			//It will check for both wifi and cellular network
			return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
				NetworkCapabilities.TRANSPORT_WIFI
			)
		}
		return false
	} else {
		val netInfo = cm.activeNetworkInfo
		return netInfo != null && netInfo.isConnectedOrConnecting
	}
}

/**
 * Returns the theme resources
 */
@Deprecated("returns faded color", ReplaceWith("Context.getResourceColor"), DeprecationLevel.ERROR)
fun Context.getColorFromAttr(
	@AttrRes attrColor: Int,
	typedValue: TypedValue = TypedValue(),
	resolveRefs: Boolean = true
): Int {
	theme.resolveAttribute(attrColor, typedValue, resolveRefs)
	return typedValue.resourceId
}

/**
 * Opens a URL in a custom tab.
 */
fun Context.openInBrowser(url: String, @ColorInt toolbarColor: Int? = null) {
	this.openInBrowser(url.toUri(), toolbarColor)
}

fun Context.openInBrowser(uri: Uri, @ColorInt toolbarColor: Int? = null) {
	try {
		val intent = CustomTabsIntent.Builder()
			.setDefaultColorSchemeParams(
				CustomTabColorSchemeParams.Builder()
					.setToolbarColor(toolbarColor ?: getResourceColor(R.attr.colorPrimary))
					.build()
			)
			.build()
		intent.launchUrl(this, uri)
	} catch (e: Exception) {
		toast(e.message)
	}
}

/**
 * Returns the color for the given attribute.
 *
 * @param resource the attribute.
 * @param alphaFactor the alpha number [0,1].
 */
@ColorInt
fun Context.getResourceColor(@AttrRes resource: Int, alphaFactor: Float = 1f): Int {
	val typedArray = obtainStyledAttributes(intArrayOf(resource))
	val color = typedArray.getColor(0, 0)
	typedArray.recycle()

	if (alphaFactor < 1f) {
		val alpha = (color.alpha * alphaFactor).roundToInt()
		return Color.argb(alpha, color.red, color.green, color.blue)
	}

	return color
}

/**
 * extension function to get display metrics
 */
val Context.displayCompat: Display?
	get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
		display
	} else {
		@Suppress("DEPRECATION")
		getSystemService<WindowManager>()?.defaultDisplay
	}

/**
 * get current screen width in pixels
 */
fun Context.screenWidthPx() = resources.displayMetrics.widthPixels

/**
 * get current screen height in pixels
 */
fun Context.screenHeightPx() = resources.displayMetrics.heightPixels

/**
 * returns screen density
 */
fun Context.screenDensity() = resources.displayMetrics.density


/**
 * extension function to fix navigation double-click crash
 * Author: Abner Escócio
 * https://stackoverflow.com/a/65959445/11836178
 */
fun Fragment.safeNavigate(
	directions: NavDirections,
	extras: FragmentNavigator.Extras
) {
	val navController = findNavController()
	val destination = navController.currentDestination as FragmentNavigator.Destination
	if (javaClass.name == destination.className) {
		navController.navigate(directions, extras)
	}
}

fun Fragment.safeNavigate(directions: NavDirections) {
	val navController = findNavController()
	val destination = navController.currentDestination as FragmentNavigator.Destination
	if (javaClass.name == destination.className) {
		navController.navigate(directions)
	}
}
