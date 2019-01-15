package gr.tei.erasmus.pp.eventmate.ui.gameRank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import gr.tei.erasmus.pp.eventmate.R
import gr.tei.erasmus.pp.eventmate.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_game_rank.*

class GameRankActivity : BaseActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_game_rank)
		setupToolbar(toolbar)
	}
}
