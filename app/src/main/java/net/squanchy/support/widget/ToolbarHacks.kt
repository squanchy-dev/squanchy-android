package net.squanchy.support.widget

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.Px
import androidx.appcompat.widget.ActionMenuView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children

fun Toolbar.calculateMenuItemCenterCoordinates(@IdRes menuItemId: Int): OriginCoordinates? {
    val actionMenuView = children.find { it is ActionMenuView } as? ViewGroup
        ?: return null

    val menuItemView = actionMenuView.findViewById<View?>(menuItemId) ?: return null

    return OriginCoordinates(
        x = menuItemView.x + actionMenuView.x + menuItemView.width / 2,
        y = menuItemView.y + actionMenuView.y + menuItemView.height / 2
    )
}

data class OriginCoordinates(@Px val x: Float, @Px val y: Float) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readFloat(), parcel.readFloat())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(x)
        parcel.writeFloat(y)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OriginCoordinates> {
        override fun createFromParcel(parcel: Parcel): OriginCoordinates {
            return OriginCoordinates(parcel)
        }

        override fun newArray(size: Int): Array<OriginCoordinates?> {
            return arrayOfNulls(size)
        }
    }
}
