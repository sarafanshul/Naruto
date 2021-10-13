package com.projectdelta.naruto.ui.main.episode.list

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.projectdelta.naruto.R
import com.projectdelta.naruto.data.preference.PreferenceManager
import com.projectdelta.naruto.util.NotFound
import com.projectdelta.naruto.widgets.ExtendedNavigationView
import com.projectdelta.naruto.widgets.sheet.TabbedBottomSheetDialog

@Suppress("unused", "JoinDeclarationAndAssignment")
class EpisodeSettingSheet (
	fActivity : Activity,
	private val preferenceManager: PreferenceManager,
	onGroupClickListener: (ExtendedNavigationView.Group) -> Unit
) : TabbedBottomSheetDialog(fActivity) {

	private val filters: Filter
	private val sort: Sort

	init {
		filters = Filter(fActivity)
		filters.onGroupClicked = onGroupClickListener

		sort = Sort(fActivity)
		sort.onGroupClicked = onGroupClickListener

	}

	override fun getTabViews(): List<View> = listOf(
		filters,
		sort,
	)

	override fun getTabTitles(): List<Int> = listOf(
		R.string.action_filter,
		R.string.action_sort,
	)


	/**
	 * Filters group (cannon, ...).
	 */
	@Suppress("ImplicitNullableNothingType")
	inner class Filter @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
		Settings(context, attrs) {

		private val filterGroup = FilterGroup()

		init {
			setGroups(listOf(filterGroup))
		}

		/**
		 * Returns true if there's at least one filter from [FilterGroup] active.
		 */
		fun hasActiveFilters(): Boolean {
			return filterGroup.items.filterIsInstance<Item.CheckboxGroup>().any { it.checked }
		}

		inner class FilterGroup : Group {

			private val cannon = Item.CheckboxGroup(R.string.action_filter_cannon, this)

			override val header = null
			override val items: List<Item> = listOf( cannon )
			override val footer = null

			override fun initModels() {
				cannon.checked = preferenceManager.filterCannon()
			}

			override fun onItemClicked(item: Item) {
				item as Item.CheckboxGroup
				val newState = ! item.checked
				item.checked = newState
				when (item) {
					cannon -> preferenceManager.setFilterCannon(newState)
					else -> {
						throw IllegalStateException("Unknown arg clicked!")
					}
				}
				adapter.notifyItemChanged(item)
			}
		}
	}

	/**
	 * Sorting group (episode number, ...).
	 */
	@Suppress("ImplicitNullableNothingType")
	inner class Sort @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
		Settings(context, attrs) {

		private val sort = SortGroup()

		init {
			setGroups(listOf(sort))
		}

		// Refreshes Display Setting selections
		fun adjustDisplaySelection() {
			sort.initModels()
			sort.items.forEach { adapter.notifyItemChanged(it) }
		}

		inner class SortGroup : Group {

			private val airDate = Item.MultiSort(R.string.action_sort_air_date, this)

			override val header = null
			override val items = listOf(airDate)
			override val footer = null

			override fun initModels() {
				airDate.state = preferenceManager.sortAirDate()
			}

			override fun onItemClicked(item: Item) {
				item as Item.MultiStateGroup
				val prevState = item.state

				item.group.items.forEach {
					(it as Item.MultiStateGroup).state =
						Item.MultiSort.SORT_NONE
				}
				item.state = when (prevState) {
					Item.MultiSort.SORT_NONE -> Item.MultiSort.SORT_ASC
					Item.MultiSort.SORT_ASC -> Item.MultiSort.SORT_DESC
					Item.MultiSort.SORT_DESC -> Item.MultiSort.SORT_ASC
					else -> throw Exception("Unknown state")
				}

				item.group.items.forEach { adapter.notifyItemChanged(it) }

				when(item){
					airDate -> preferenceManager.setSortAirDate(item.state)
					else -> throw NotFound.TheFuckHappened("Unknown sort selected !")
				}
			}

		}
	}


	open inner class Settings(context: Context, attrs: AttributeSet?) :
		ExtendedNavigationView(context, attrs) {

		lateinit var adapter: Adapter

		/**
		 * Click listener to notify the parent fragment when an item from a group is clicked.
		 */
		var onGroupClicked: (Group) -> Unit = {}

		fun setGroups(groups: List<Group>) {
			adapter = Adapter(groups.map { it.createItems() }.flatten())
			recycler.adapter = adapter

			groups.forEach { it.initModels() }
			addView(recycler)
		}

		/**
		 * Adapter of the recycler view.
		 */
		inner class Adapter(items: List<Item>) : ExtendedNavigationView.Adapter(items) {

			override fun onItemClicked(item: Item) {
				if (item is GroupedItem) {
					item.group.onItemClicked(item)
					onGroupClicked(item.group)
				}
			}
		}
	}
}