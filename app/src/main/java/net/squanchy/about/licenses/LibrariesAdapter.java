package net.squanchy.about.licenses;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import net.squanchy.R;

class LibrariesAdapter extends RecyclerView.Adapter {

    private static final List<Library> LIBRARIES = Libraries.LIBRARIES;

    private final Context context;
    private final LayoutInflater layoutInflater;

    LibrariesAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_library_license, parent, false);
        return new LibraryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((LibraryViewHolder) holder).updateWith(LIBRARIES.get(position));
    }

    @Override
    public int getItemCount() {
        return LIBRARIES.size();
    }

    private static class LibraryViewHolder extends RecyclerView.ViewHolder {

        private final TextView libraryNameView;
        private final TextView libraryLicenseLabelView;
        private final TextView libraryLicenseNoticeView;

        LibraryViewHolder(View itemView) {
            super(itemView);
            libraryNameView = (TextView) itemView.findViewById(R.id.library_name);
            libraryLicenseLabelView = (TextView) itemView.findViewById(R.id.library_license_label);
            libraryLicenseNoticeView = (TextView) itemView.findViewById(R.id.library_license_notice);
        }

        void updateWith(Library library) {
            libraryNameView.setText(library.name());

            License license = library.license();
            String licenseLabel = itemView.getContext().getString(R.string.license_label_template, library.author(), license.label());
            libraryLicenseLabelView.setText(licenseLabel);
            libraryLicenseNoticeView.setText(license.noticeResId());
        }
    }
}
