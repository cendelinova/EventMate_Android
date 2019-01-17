package gr.tei.erasmus.pp.eventmate.data.model

import android.os.Parcel
import android.os.Parcelable

data class SubmissionExtra(
	val userId: Long,
	val taskId: Long,
	val seeTaskOwner: Boolean
) : Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readLong(),
		parcel.readLong(),
		parcel.readByte() != 0.toByte()
	) {
	}
	
	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeLong(userId)
		parcel.writeLong(taskId)
		parcel.writeByte(if (seeTaskOwner) 1 else 0)
	}
	
	override fun describeContents(): Int {
		return 0
	}
	
	companion object CREATOR : Parcelable.Creator<SubmissionExtra> {
		override fun createFromParcel(parcel: Parcel): SubmissionExtra {
			return SubmissionExtra(parcel)
		}
		
		override fun newArray(size: Int): Array<SubmissionExtra?> {
			return arrayOfNulls(size)
		}
	}
}
