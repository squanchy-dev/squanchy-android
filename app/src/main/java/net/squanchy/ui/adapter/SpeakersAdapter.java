package net.squanchy.ui.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.model.data.Speaker;
import net.squanchy.ui.fragment.SpeakersListFragment;
import net.squanchy.ui.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class SpeakersAdapter extends BaseAdapter implements Filterable {

    private LayoutInflater mInflater;
    private List<Speaker> mSpeakers;
    private List<Speaker> mSpeakersFiltered;
    private SparseBooleanArray mLetterPositions;
    private SpeakerFilter mFilter = new SpeakerFilter();

    public SpeakersAdapter(Context context, List<Speaker> speakersList, SparseBooleanArray letterPositions) {
        mInflater = LayoutInflater.from(context);
        mSpeakers = speakersList;
        mSpeakersFiltered = speakersList;
        mLetterPositions = letterPositions;
    }

    public void setData(List<Speaker> speakersList, SparseBooleanArray letterPositions) {
        mSpeakers = speakersList;
        mSpeakersFiltered = speakersList;
        mLetterPositions = letterPositions;
    }

    @Override
    public int getCount() {
        return mSpeakersFiltered.size();
    }

    @Override
    public Speaker getItem(int position) {
        return mSpeakersFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mSpeakersFiltered.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            row = mInflater.inflate(R.layout.item_speaker, parent, false);

            holder = new ViewHolder();
            holder.imgPhoto = (CircleImageView) row.findViewById(R.id.imgPhoto);
            holder.txtName = (TextView) row.findViewById(R.id.txtName);
            holder.txtOrgAndJobTitle = (TextView) row.findViewById(R.id.txtOrgAndJobTitle);
            holder.txtFirstLetter = (TextView) row.findViewById(R.id.txtFirstLetter);
            holder.divider = row.findViewById(R.id.divider);

            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        fillView(holder, mSpeakersFiltered.get(position), position);
        return row;
    }

    private void fillView(final ViewHolder holder, Speaker speaker, int position) {
        String imageUrl = speaker.getAvatarImageUrl();
        holder.imgPhoto.setImageWithURL(imageUrl);

        String organization = speaker.getOrganization();
        String jobTitle = speaker.getJobTitle();
        String space = (isEmpty(organization) && isEmpty(jobTitle))
                || isEmpty(organization)
                || isEmpty(jobTitle)
                ? "" : " / ";

        holder.txtName.setText(speaker.getFirstName() + " " + speaker.getLastName());
        holder.txtOrgAndJobTitle.setText(organization + space + jobTitle);

        if (mLetterPositions.get(position)) {
            String letter = speaker.getFirstName().substring(0, 1).toUpperCase();
            holder.txtFirstLetter.setText(letter);
            holder.divider.setVisibility(View.VISIBLE);
        } else {
            holder.txtFirstLetter.setText("");
            holder.divider.setVisibility(View.INVISIBLE);
        }

    }

    private boolean isEmpty(String text) {
        return text == null || text.isEmpty();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ViewHolder {

        CircleImageView imgPhoto;
        TextView txtName;
        TextView txtOrgAndJobTitle;
        TextView txtFirstLetter;
        View divider;
    }

    private class SpeakerFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Speaker> speakers = mSpeakers;

            int count = speakers.size();

            final List<Speaker> filteredSpeakers = new ArrayList<>(count);

            String filterableString;

            for (Speaker speaker : speakers) {
                String firstName = speaker.getFirstName();
                String lastName = speaker.getLastName();

                filterableString = firstName + " " + lastName;
                if (filterableString.toLowerCase().contains(filterString)) {
                    filteredSpeakers.add(speaker);
                }
            }

            results.values = filteredSpeakers;
            results.count = filteredSpeakers.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mSpeakersFiltered = (ArrayList<Speaker>) results.values;
            mLetterPositions = SpeakersListFragment.generateFirstLetterPositions(mSpeakersFiltered);
            if (mListener != null) {
                mListener.onFilterChange(mSpeakersFiltered.size());
            }
            notifyDataSetChanged();
        }
    }

    public OnFilterChangeListener mListener;

    public interface OnFilterChangeListener {
        public void onFilterChange(int position);
    }

    public void setOnFilterChangeListener(OnFilterChangeListener listener) {
        if (listener != null) {
            mListener = listener;
        }
    }
}
