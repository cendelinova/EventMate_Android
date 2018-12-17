package gr.tei.erasmus.pp.eventmate.helpers

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.widget.ImageView
import java.io.ByteArrayOutputStream


object ImageHelper {
	fun getStringImage(bmp: Bitmap): String {
		val baos = ByteArrayOutputStream()
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
		val imageBytes = baos.toByteArray()
		return Base64.encodeToString(imageBytes, Base64.DEFAULT)
	}
	
	fun convertImageViewToBitmap(v: ImageView) = (v.drawable as BitmapDrawable).bitmap
}