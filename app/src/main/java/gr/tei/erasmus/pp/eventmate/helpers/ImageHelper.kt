package gr.tei.erasmus.pp.eventmate.helpers

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import java.io.ByteArrayOutputStream

object ImageHelper {
	fun convertImageToByteArray(bitmap: Bitmap) {
		val baos = ByteArrayOutputStream()
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		val imageInByte = baos.toByteArray()
	}
}