package gr.tei.erasmus.pp.eventmate.helpers

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.widget.ImageView
import com.makeramen.roundedimageview.RoundedDrawable
import java.io.ByteArrayOutputStream
import java.io.File


object FileHelper {
	fun encodeImage(bmp: Bitmap): String {
		val baos = ByteArrayOutputStream()
		bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos)
		val imageBytes = baos.toByteArray()
		return Base64.encodeToString(imageBytes, Base64.NO_WRAP)
	}
	
	fun encodeFile(filePath: String?): String {
		filePath?.let {
			val bytes = File(it).readBytes()
			return Base64.encodeToString(bytes, Base64.NO_WRAP)
		}
	}
	
	fun decodeImage(encodedImage: String): Bitmap {
		val decodedString = Base64.decode(encodedImage, Base64.NO_WRAP)
		return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
	}
	
	fun convertImageViewToBitmap(v: ImageView) = (v.drawable as RoundedDrawable).sourceBitmap
	
	fun getBitmap(v: ImageView): Bitmap {
		return (v.drawable as BitmapDrawable).bitmap
	}
	
	fun getRealPathFromURI(contentUri: Uri?, contentResolver: ContentResolver): String? {
		var absolutePath: String? = null
		contentUri?.run {
			contentResolver.query(this, arrayOf(MediaStore.Images.Media.DATA), null, null, null)?.use {
				with(it) {
					while (moveToNext()) {
						absolutePath = getString(getColumnIndex(android.provider.MediaStore.Images.Media.DATA))
					}
				}
			}
		}
		
		return absolutePath
	}
}