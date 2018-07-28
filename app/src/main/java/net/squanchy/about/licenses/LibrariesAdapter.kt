package net.squanchy.about.licenses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.squanchy.R
import net.squanchy.about.licenses.Libraries.LIBRARIES

class LibrariesAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_library_license, parent, false)
        return LibraryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LibraryViewHolder).updateWith(LIBRARIES[position])
    }

    override fun getItemCount(): Int = LIBRARIES.size

    private class LibraryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val libraryNameView: TextView = itemView.findViewById(R.id.library_name)
        private val libraryLicenseLabelView: TextView = itemView.findViewById(R.id.library_license_label)
        private val libraryLicenseNoticeView: TextView = itemView.findViewById(R.id.library_license_notice)

        internal fun updateWith(library: Library) {
            libraryNameView.text = library.name

            val license = library.license
            val licenseLabel = itemView.context.getString(R.string.license_label_template, library.author, license.label)
            libraryLicenseLabelView.text = licenseLabel
            libraryLicenseNoticeView.setText(license.noticeResId)
        }
    }
}
