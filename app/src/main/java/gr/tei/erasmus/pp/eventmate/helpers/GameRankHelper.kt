package gr.tei.erasmus.pp.eventmate.helpers

import android.widget.ImageView
import gr.tei.erasmus.pp.eventmate.R

object GameRankHelper {
	
	fun showCorrectGameRankPhoto(imageView: ImageView, currentPoints: Int) {
		val resId = when(currentPoints) {
			in GameRank.LEVEL1.start..GameRank.LEVEL1.end -> GameRank.LEVEL1.picture
			in GameRank.LEVEL2.start..GameRank.LEVEL2.end -> GameRank.LEVEL2.picture
			in GameRank.LEVEL3.start..GameRank.LEVEL3.end -> GameRank.LEVEL3.picture
			
			else -> {
				R.drawable.ic_undefined
			}
		}
		
		imageView.setImageResource(resId)
	}
	enum class GameRank(val start: Int, val end: Int, val picture: Int) {
		LEVEL1(0, 20, R.drawable.level1_chicken),
		LEVEL2(21, 200, R.drawable.level2_fox),
		LEVEL3(200, Int.MAX_VALUE, R.drawable.level3_lion)
	}
}