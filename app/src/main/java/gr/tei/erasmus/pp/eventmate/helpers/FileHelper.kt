package gr.tei.erasmus.pp.eventmate.helpers

import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.widget.ImageView
import com.makeramen.roundedimageview.RoundedDrawable
import gr.tei.erasmus.pp.eventmate.data.model.SubmissionFile
import org.joda.time.DateTime
import timber.log.Timber
import java.io.*


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
	
	fun decodeFile(context: Context, encodedString: String?): Uri {
		
		encodedString?.run {
			val decodedBytes = Base64.decode(toByteArray(), Base64.NO_WRAP)
			
			val timestamp =
				DateTimeHelper.convertDateTimeToString(DateTime.now(), DateTimeHelper.FULL_DATE_TIME_FORMAT_FILE)
			val outputFile = File.createTempFile(timestamp, null, context.cacheDir)
			
			val inputStream = ByteArrayInputStream(decodedBytes)
			val outputStream = FileOutputStream(outputFile)
			
			inputStream.use { input ->
				outputStream.use { output ->
					input.copyTo(output)
				}
			}
			
			return Uri.fromFile(outputFile)
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
	
	fun saveFileLocally(context: Context, submissionFile: SubmissionFile, suffix: String, mimeType: String) {
		var file: File? = null
		val outputStream: FileOutputStream
		try {
			file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), submissionFile.name + suffix)
			
			outputStream = FileOutputStream(file)
			outputStream.write(Base64.decode(submissionFile.data.toByteArray(), Base64.NO_WRAP))
			outputStream.close()
			showDownloadNotification(context, file, mimeType)
		} catch (e: IOException) {
			Timber.e(e)
		}
	}
	
	private fun showDownloadNotification(context: Context, file: File, mimeType: String) {
		val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
		with(file) {
			downloadManager.addCompletedDownload(
				name,
				name,
				true,
				mimeType,
				absolutePath,
				length(),
				true
			)
		}
	}
}