package com.ls.ui.fragment;


import com.ls.drupalcon.R;
import com.ls.drupalcon.model.Model;
import com.ls.drupalcon.model.PreferencesManager;
import com.ls.drupalcon.model.UpdatesManager;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.SearchTimeline;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SocialMediaFragment extends Fragment
{

    public static final String TAG = "SocialMediaFragment";
    private View rootView;
    private View mLayoutPlaceholder;

    private UpdatesManager.DataUpdatedListener updateReceiver = new UpdatesManager.DataUpdatedListener() {
        @Override
        public void onDataUpdated(List<Integer> requestIds) {
            updateData(requestIds);
        }
    };

    public SocialMediaFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
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
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;

        Model.instance().getUpdatesManager().registerUpdateListener(updateReceiver);

        fillView();
    }

    private void fillView() {
        String searchQuery = PreferencesManager.getInstance().getTwitterSearchQuery();

        final SearchTimeline userTimeline = new SearchTimeline.Builder()
                .query(searchQuery)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(rootView.getContext())
                .setTimeline(userTimeline)
                .build();
        ListView list = (ListView)rootView.findViewById(R.id.list_view);

        list.setEmptyView(mLayoutPlaceholder);
        list.setAdapter(adapter);
    }


    @Override
    public void onDestroyView() {
        Model.instance().getUpdatesManager().unregisterUpdateListener(updateReceiver);
        super.onDestroyView();
    }
}
