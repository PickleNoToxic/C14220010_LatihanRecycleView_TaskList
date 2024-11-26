package uts.c14220010.latihanrecycleview_tasklist

import android.os.Parcel
import android.os.Parcelable

data class task(
    var nama : String?,
    var tanggal : String?,
    var deskripsi : String?,
    var status : String?,
    var isSaved : Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readBoolean()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nama)
        parcel.writeString(tanggal)
        parcel.writeString(deskripsi)
        parcel.writeString(status)
        parcel.writeBoolean(isSaved)
    }

    companion object CREATOR : Parcelable.Creator<task> {
        override fun createFromParcel(parcel: Parcel): task {
            return task(parcel)
        }

        override fun newArray(size: Int): Array<task?> {
            return arrayOfNulls(size)
        }
    }
}
