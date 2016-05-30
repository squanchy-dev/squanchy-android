package com.ls.ui.fragment;


import com.ls.drupalcon.R;
import com.ls.drupalcon.model.PreferencesManager;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.SearchTimeline;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SocialMediaFragment extends Fragment
{

    public static final String TAG = "SocialMediaFragment";

    public SocialMediaFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_social_media, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        String searchQuery = PreferencesManager.getInstance().getTwitterSearchQuery();

        final SearchTimeline userTimeline = new SearchTimeline.Builder()
                .query(searchQuery)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(view.getContext())
                .setTimeline(userTimeline)
                .build();
        ListView list = (ListView)view.findViewById(R.id.list_view);
        list.setAdapter(adapter);
    }
}
