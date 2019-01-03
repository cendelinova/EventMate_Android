package gr.tei.erasmus.pp.eventmate.helpers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import com.makeramen.roundedimageview.RoundedDrawable
import java.io.ByteArrayOutputStream


object ImageHelper {
	fun getStringImage(bmp: Bitmap): String {
		val baos = ByteArrayOutputStream()
		bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos)
		val imageBytes = baos.toByteArray()
		return Base64.encodeToString(imageBytes, Base64.NO_WRAP)
	}
	
	fun getImageFromString(encodedImage: String): Bitmap {
		val decodedString = Base64.decode(encodedImage, Base64.NO_WRAP)
		return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
	}
	
	fun convertImageViewToBitmap(v: ImageView) = (v.drawable as RoundedDrawable).sourceBitmap
}