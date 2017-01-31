package com.connfa.ui.fragment;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.connfa.R;
import com.connfa.model.Model;
import com.connfa.model.PreferencesManager;
import com.connfa.model.UpdatesManager;
import com.connfa.utils.NetworkUtils;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 *
 * @deprecated Use {@link com.connfa.social.SocialFeedActivity} instead.
 */
public class SocialMediaFragment extends Fragment {

    public static final String TAG = "SocialMediaFragment";
    private View rootView;
    private View mLayoutPlaceholder;

    private UpdatesManager.DataUpdatedListener updateReceiver = requestIds -> updateData(requestIds);

    public SocialMediaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_social_media, container, false);
        mLayoutPlaceholder = rootView.findViewById(R.id.layout_placeholder);
        return rootView;
    }

    private void updateData(List<Integer> requestIds) {
        for (int id : requestIds) {
            if (UpdatesManager.SETTINGS_REQUEST_ID == id) {
                fillView();
                break;
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;

        Model.getInstance().getUpdatesManager().registerUpdateListener(updateReceiver);

        fillView();
    }

    private void fillView() {

        if (!NetworkUtils.isOn(getActivity())) {
            rootView.findViewById(R.id.txtNoConnection).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.list_view).setVisibility(View.GONE);
            rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
            mLayoutPlaceholder.setVisibility(View.GONE);
        }

        String searchQuery = PreferencesManager.create(getActivity()).getTwitterSearchQuery();

        final SearchTimeline userTimeline = new SearchTimeline.Builder()
                .query(searchQuery)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(rootView.getContext())
                .setTimeline(userTimeline)
                .build();

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
            }
        });

        ListView list = (ListView) rootView.findViewById(R.id.list_view);

        list.setEmptyView(mLayoutPlaceholder);
        list.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        Model.getInstance().getUpdatesManager().unregisterUpdateListener(updateReceiver);
        super.onDestroyView();
    }
}
