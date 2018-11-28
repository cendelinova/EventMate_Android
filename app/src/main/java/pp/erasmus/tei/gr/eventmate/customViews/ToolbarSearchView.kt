package pp.erasmus.tei.gr.eventmate.customViews

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.Color
import android.support.annotation.ColorInt
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.search_view_layout.view.*
import pp.erasmus.tei.gr.eventmate.R
import timber.log.Timber

class ToolbarSearchView : LinearLayout {
	
	/* Constants **********************************************************************************/
	
	companion object {
		const val UNDEFINED = -1
		
		const val REVEAL_ANIMATION_DURATION = 500L
		const val ALFA_ANIMATION_DURATION = 250L
	}
	
	/* Private attributes *************************************************************************/
	
	private var searchWidth: Int = UNDEFINED
	private lateinit var layoutView: View
	
	/* Constructors *******************************************************************************/
	
	constructor(context: Context) : super(context) {
		initialize()
	}
	
	constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
		initialize(attributeSet)
	}
	
	constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
		initialize(attributeSet)
	}
	
	/* Public Methods *****************************************************************************/
	
	/**
	 * Register text watcher used to handle text input.
	 */
	fun registerTextWatcher(textWatcher: TextWatcher) {
		Timber.v("registerTextWatcher() called with: textWatcher = [$textWatcher]")
		
		search_text_input.addTextChangedListener(textWatcher)
	}
	
	/**
	 * Set correct input type so appropriate keyboard is displayed.
	 *
	 * @param inputType (@see android.text.InputType)
	 */
	fun setInputType(inputType: Int) {
		Timber.v("setInputType() called with: inputType = [$inputType]")
		
		search_text_input.inputType = inputType
	}
	
	/* Private methods ****************************************************************************/
	
	private fun initialize(attributeSet: AttributeSet? = null) {
		Timber.v("initialize() called with: attributeSet = [$attributeSet]")
		
		setWillNotDraw(false)
		layoutView = LayoutInflater.from(context).inflate(R.layout.search_view_layout, this, true)
		
		if (attributeSet != null) {
			displayAttributes(loadAttributes(attributeSet))
		}
		
		search_layout.post { enableClickableViews() }
		search_text_input.setOnEditorActionListener { textView: TextView, actionId: Int, event: KeyEvent? ->
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				val imm = textView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
				imm.hideSoftInputFromWindow(textView.windowToken, 0)
				true
			} else {
				false
			}
		}
	}
	
	/**
	 * Set attributes to correct views.
	 */
	private fun displayAttributes(attributes: SearchViewAttributes) {
		Timber.v("displayAttributes() called with: attributes = [$attributes]")
		
		with(search_text_input) {
			hint = attributes.hint
			setTextColor(attributes.textColor)
			setHintTextColor(attributes.hintColor)
		}
		
		with(search_title) {
			text = attributes.title
			setTextColor(attributes.titleTextColor)
		}
	}
	
	/**
	 * Load attributes from XML.
	 */
	private fun loadAttributes(attributeSet: AttributeSet): SearchViewAttributes {
		Timber.v("loadAttributes() called with: attributeSet = [$attributeSet]")
		
		val searchViewAttributes: SearchViewAttributes
		val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.ToolbarSearchView)
		
		try {
			searchViewAttributes = SearchViewAttributes(
				attributes.getString(R.styleable.ToolbarSearchView_hint)!!,
				attributes.getColor(R.styleable.ToolbarSearchView_hintTextColor, Color.WHITE),
				attributes.getColor(R.styleable.ToolbarSearchView_iconColor, Color.WHITE),
				attributes.getColor(R.styleable.ToolbarSearchView_searchTextColor, Color.WHITE),
				attributes.getString(R.styleable.ToolbarSearchView_titleText)!!,
				attributes.getColor(R.styleable.ToolbarSearchView_titleTextColor, Color.WHITE)
			)
		} finally {
			attributes.recycle()
		}
		
		return searchViewAttributes
	}
	
	/**
	 * Now we know view has been drawn so we can get its width and handle all button clicks.
	 */
	private fun enableClickableViews() {
		Timber.v("enableClickableViews() called")
		
		search_menu_icon.setOnClickListener { handleSearchIcon() }
		cancel_search.setOnClickListener { handleCloseIcon() }
	}
	
	private fun disableClickableViews() {
		Timber.v("disableClickableViews() called")
		
		search_menu_icon.setOnClickListener({})
		cancel_search.setOnClickListener({})
	}
	
	/**
	 * User clicked on search icon. We need to expand edit text and let him do the searching.
	 */
	private fun handleSearchIcon() {
		Timber.v("handleSearchIcon() called")
		
		disableClickableViews()
		
		if (searchWidth == UNDEFINED) {
			searchWidth = search_layout.width
		}
		
		search_layout.visibility = View.VISIBLE
		search_menu_icon.visibility = View.GONE
		search_title.visibility = View.GONE
		title_layout.visibility = View.GONE
		
		val listener = object : AnimatorListenerAdapter() {
			override fun onAnimationEnd(animation: Animator) {
				Timber.v("onAnimationEnd() called with: animation = [$animation]")
				enableClickableViews()
			}
		}
		
//		App.COMPONENTS.provideAnimationHelper().revealAnimation(
//			search_layout,
//			searchWidth,
//			true,
//			REVEAL_ANIMATION_DURATION,
//			listener)
		
		displayKeyboardOnSearchStart()
	}
	
	private fun displayKeyboardOnSearchStart() {
		Timber.v("displayKeyboardOnSearchStart() called")
		
		search_text_input.requestFocus()
//		App.COMPONENTS.provideUtilsHelper().forceOpenKeyboard(layoutView)
	}
	
	/**
	 * This button has 2 roles. User either wants to cancel searching (empty edit text),
	 * or he wants to erase search text (edit text not empty).
	 */
	private fun handleCloseIcon() {
		Timber.v("handleCloseIcon() called")
		
		if (search_text_input.text.toString().isEmpty()) {
			cancelSearch()
		} else {
			search_text_input.text.clear()
		}
	}
	
	/**
	 * User wants to cancel search. We need to hide edit text and display search icon.
	 */
	private fun cancelSearch() {
		Timber.v("cancelSearch() called")
		
		disableClickableViews()
		
//		App.COMPONENTS.provideUtilsHelper().forceHideKeyboard(layoutView)
		
		val listener = object : AnimatorListenerAdapter() {
			override fun onAnimationEnd(animation: Animator) {
				Timber.v("onAnimationEnd() called with: animation = [$animation]")
				
				search_layout.visibility = View.GONE
				title_layout.visibility = View.VISIBLE
				
				animateVisibility()
			}
		}
		
//		App.COMPONENTS.provideAnimationHelper().revealAnimation(
//			search_layout,
//			searchWidth,
//			false,
//			REVEAL_ANIMATION_DURATION,
//			listener)
	}
	
	private fun animateVisibility() {
//		App.COMPONENTS.provideAnimationHelper().visibilityAnimation(search_menu_icon,
//			ALFA_ANIMATION_DURATION)
//		App.COMPONENTS.provideAnimationHelper().visibilityAnimation(search_title,
//			ALFA_ANIMATION_DURATION,
//			object : AnimatorListenerAdapter() {
//				override fun onAnimationEnd(p0: Animator?) {
//					enableClickableViews()
//				}
//			})
	}
	
	/* Inner classes ******************************************************************************/
	
	data class SearchViewAttributes(val hint: String,
	                                @ColorInt val hintColor: Int,
	                                @ColorInt val iconColor: Int,
	                                @ColorInt val textColor: Int,
	                                val title: String,
	                                @ColorInt val titleTextColor: Int)
	
}